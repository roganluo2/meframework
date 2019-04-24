package com.gperedu.meframework.beans;

/**
 * Created by rogan on 2019/4/19.
 */
public class GPBeanWrapper {

    public GPBeanWrapper(){}

    public GPBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
    }

    private Object wrappedInstance;

    private Class wrappedClass;

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Class getWrappedClass() {
        return wrappedClass;
    }

    public void setWrappedClass(Class wrappedClass) {
        this.wrappedClass = wrappedClass;
    }
}
