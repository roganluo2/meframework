package com.gperedu.meframework.beans.factory.support;

import com.gperedu.meframework.beans.factory.config.GPBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化IOC容器
 * Created by rogan on 2019/4/15.
 */
public class GPDefaultListableBeanFactory {


    private final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


}
