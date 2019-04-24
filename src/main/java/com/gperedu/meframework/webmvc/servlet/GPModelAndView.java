package com.gperedu.meframework.webmvc.servlet;

import java.util.Map;

/**
 * Created by rogan on 2019/4/21.
 */
public class GPModelAndView {


    private String viewName;

    private Map<String,?> model;

    public GPModelAndView(String viewName) {
        this(viewName, null);
    }

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
