package com.gperedu.meframework.beans.factory.config;

/**
 * beanDefinition的实现
 * Created by rogan on 2019/4/15.
 */
public class GPBeanDefinition {

    //是否延时加载
    private boolean lazyInit = false;

    //class类型
    private volatile Object beanClass;

    //获取bean在 ioc 中的名字
    private String factoryBeanName;



}
