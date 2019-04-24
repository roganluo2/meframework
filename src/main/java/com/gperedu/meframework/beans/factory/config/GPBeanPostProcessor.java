package com.gperedu.meframework.beans.factory.config;

/**
 * Created by rogan on 2019/4/19.
 */
public class GPBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
