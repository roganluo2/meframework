package com.gperedu.meframework.demo.controller;

import com.gperedu.meframework.annotation.GPAutowired;
import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.annotation.GPRequetParam;
import com.gperedu.meframework.demo.service.IQueryService;
import com.gperedu.meframework.webmvc.servlet.GPModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rogan on 2019/4/25.
 */
@GPRequestMapping("/")
@GPController
public class PageAction {

    @GPAutowired
    IQueryService queryService;


    public GPModelAndView query(@GPRequetParam("teacher") String teacher)
    {
        String result = queryService.query(teacher);
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("teacher", teacher);
        model.put("token", 1234);
        model.put("data", result);
        return new GPModelAndView("first.htm",model);

    }

}
