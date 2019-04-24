package com.gperedu.meframework.beans.factory.support;

import com.gperedu.meframework.beans.factory.config.GPBeanDefinition;
import com.gperedu.meframework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化IOC容器
 * Created by rogan on 2019/4/15.
 */
public class GPDefaultListableBeanFactory  extends GPAbstractApplicationContext{


    public final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


}
