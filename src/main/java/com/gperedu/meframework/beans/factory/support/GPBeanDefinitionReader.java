package com.gperedu.meframework.beans.factory.support;

import com.google.common.collect.Lists;
import com.gperedu.meframework.beans.factory.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 *
    读取器
 * Created by rogan on 2019/4/18.
 */
public class GPBeanDefinitionReader {

    Properties config = new Properties();

    private List<String> registryBeanClasses = Lists.newArrayList();

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public GPBeanDefinitionReader(String... contextLoactions) {
        InputStream resourceAsStream = null;
        try {
            String contextPath = contextLoactions[0].replace("classpath:","");
            resourceAsStream =    this.getClass().getClassLoader().getResourceAsStream(contextPath);
            config.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != resourceAsStream)
            {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         doScanner(config.getProperty("basePackage"));
    }

    public List<GPBeanDefinition> loadBeanDefinition()  {
        List<GPBeanDefinition> result = Lists.newArrayList();
        try {
            for(String className : registryBeanClasses)
            {
                Class<?> aClass = Class.forName(className);
                if(aClass.isInterface()){continue;}
                //className == aClass.getName()??
                GPBeanDefinition beanDefinition = doCreateBeanDefinition(toLowerFirstCase(aClass.getSimpleName()),className);
                result.add(beanDefinition);
                //??接口获取实现类？
                for(Class i:aClass.getInterfaces())
                {
                    result.add(doCreateBeanDefinition(i.getName(), aClass.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private GPBeanDefinition doCreateBeanDefinition(String beanName, String className) {
        GPBeanDefinition gpBeanDefinition = new GPBeanDefinition();
        gpBeanDefinition.setBeanClassName(className);
        gpBeanDefinition.setFactoryBeanName(beanName);
        return gpBeanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] = (char) (chars[0] + 32);
        return String.valueOf(chars);
    }



    private void doScanner(String basePackage) {
//        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        URL url = this.getClass().getResource("/" + basePackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for(File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(basePackage + "." + file.getName());
                continue;
            }
            if(!file.getName().endsWith(".class"))
            {
                continue;
            }
            String className = basePackage + "." + file.getName().replace(".class","");
            registryBeanClasses.add(className);
        }
    }
}
