package com.gperedu.meframework.context;

import com.google.common.collect.Maps;
import com.gperedu.meframework.annotation.GPAutowired;
import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPService;
import com.gperedu.meframework.beans.GPBeanWrapper;
import com.gperedu.meframework.beans.factory.GPBeanFactory;
import com.gperedu.meframework.beans.factory.config.GPBeanDefinition;
import com.gperedu.meframework.beans.factory.config.GPBeanPostProcessor;
import com.gperedu.meframework.beans.factory.support.GPDefaultListableBeanFactory;
import com.gperedu.meframework.beans.factory.support.GPBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 定义一个容器的实现
 * Created by rogan on 2019/4/15.
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {


    private String[] configLoactions;

    GPBeanDefinitionReader reader;

    public GPApplicationContext(String ...configLocations){
        this.configLoactions = configLocations;
        try {
            this.refresh();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    singletonObjects
    private Map<String,Object> singletonBeanMap = Maps.newHashMap();
    //same as AbstractAutowireCapableBeanFactory.factoryBeanInstanceCache
    private Map<String,GPBeanWrapper> factoryBeanInstanceCache = Maps.newConcurrentMap();


    @Override
    public Object getBean(String beanName) {
        try {
            GPBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
            if (null == gpBeanDefinition) {
                return null;
            }
            GPBeanPostProcessor postProcessor = new GPBeanPostProcessor();
            Object instance = instantiateBean(beanName, gpBeanDefinition);
            postProcessor.postProcessBeforeInitialization(instance, beanName);
            GPBeanWrapper gpBeanWrapper = new GPBeanWrapper(instance);
            factoryBeanInstanceCache.put(beanName, gpBeanWrapper);
            postProcessor.postProcessAfterInitialization(instance, beanName);
            populateBean(beanName, gpBeanDefinition, gpBeanWrapper);
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName, GPBeanDefinition gpBeanDefinition, GPBeanWrapper gpBeanWrapper) {
        String beanClassName = gpBeanDefinition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            //只对这两个注解的类注入
            if(!aClass.isAnnotationPresent(GPController.class) && !aClass.isAnnotationPresent(GPService.class)){
                return;
            }
            Field[] fields = aClass.getDeclaredFields();
            for(Field field : fields)
            {
                if(!field.isAnnotationPresent(GPAutowired.class)){continue;}
                GPAutowired annotation = field.getAnnotation(GPAutowired.class);
                String autowireName = annotation.value().trim();
                if("".equals(autowireName))
                {
                    autowireName = field.getType().getName();
                }
                field.setAccessible(true);
                if(factoryBeanInstanceCache.get(autowireName) == null)
                {
                    continue;
                }
                //怎么保证被注入的属性已经实例化呢？？
                field.set(gpBeanWrapper.getWrappedInstance(), this.factoryBeanInstanceCache.get(autowireName).getWrappedInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Object instantiateBean(String beanName, GPBeanDefinition gpBeanDefinition) {
        String beanClassName = gpBeanDefinition.getBeanClassName();
        Object instance;
        try {
            //no singleton ??
            if (singletonBeanMap.containsKey(beanClassName)) {
                 instance = singletonBeanMap.get(beanClassName);
            } else {

                Class<?> aClass = Class.forName(beanClassName);
                instance = aClass.newInstance();
                singletonBeanMap.put(gpBeanDefinition.getFactoryBeanName(), instance);
                singletonBeanMap.put(gpBeanDefinition.getBeanClassName(), instance);
            }
            return instance;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }


    @Override
    public void refresh() throws Exception {
        //1.location
        reader = new GPBeanDefinitionReader(configLoactions);
        //2.load
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinition();

        //3.register
        doRegistrerBeanDefinition(beanDefinitions);

        //4.autowire

        doAutowired();
    }

    public Properties getConfig(){
        return reader.getConfig();
    }

    private void doAutowired() {
        Set<Map.Entry<String, GPBeanDefinition>> entries = beanDefinitionMap.entrySet();
        for(Map.Entry<String, GPBeanDefinition> entry : entries)
        {
            if(entry.getValue().isLazyInit()){
                getBean(entry.getKey());
            }
        }
    }

    private void doRegistrerBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception {
        for(GPBeanDefinition gpBeanDefinition:beanDefinitions)
        {
            if(beanDefinitionMap.containsKey(gpBeanDefinition.getFactoryBeanName()))
            {
//                throw new Exception("bean Name "+ gpBeanDefinition.getFactoryBeanName() + "has been registried");
                continue;
            }
            beanDefinitionMap.put(gpBeanDefinition.getFactoryBeanName(), gpBeanDefinition);
        }

    }

    public String[] getBeanDefinitionNames(){
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

    public Integer getBeanDefinitionSize(){
        return beanDefinitionMap.size();
    }
}
