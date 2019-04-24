package com.gperedu.meframework.beans.factory;

/**
 * Created by rogan on 2019/4/14.
 */
public interface GPBeanFactory {


    /**
     * 根据beanName从IOC容器中获取一个bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    Object getBean(Class className);
}
