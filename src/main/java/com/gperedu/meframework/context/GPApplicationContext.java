package com.gperedu.meframework.context;

import com.gperedu.meframework.beans.factory.GPBeanFactory;
import com.gperedu.meframework.beans.factory.support.GPDefaultListableBeanFactory;

/**
 * 定义一个容器的实现
 * Created by rogan on 2019/4/15.
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
