package com.gperedu.meframework.beans.factory.config;

/**
 * beanDefinition的实现
 * Created by rogan on 2019/4/15.
 */
//@Data
public class GPBeanDefinition {

    //是否延时加载
    private boolean lazyInit = false;

    //class类型
    private String beanClassName;

    //获取bean在 ioc 中的名字
    private String factoryBeanName;

    private boolean singleton = true;

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
