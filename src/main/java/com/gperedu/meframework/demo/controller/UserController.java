package com.gperedu.meframework.demo.controller;

import com.gperedu.meframework.annotation.GPAutowired;
import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.annotation.GPRequetParam;
import com.gperedu.meframework.demo.service.IModifyService;
import com.gperedu.meframework.demo.service.IQueryService;
import com.gperedu.meframework.webmvc.servlet.GPModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description TODO
 * @Date 2019/3/27 19:31
 * @Created by rogan.luo
 */
@GPController
@GPRequestMapping("/user")
public class UserController {

    @GPAutowired
    private IQueryService queryService;
    @GPAutowired
    private IModifyService modifyService;

    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response, @GPRequetParam("name") String name)
    {
        String result = queryService.query(name);

        return out(response, result);
    }


    @GPRequestMapping("/add*.json")
    public GPModelAndView add(HttpServletRequest req, HttpServletResponse res, @GPRequetParam("name") String name, @GPRequetParam("addr") String addr)

    {
        String result = modifyService.add(name, addr);
        return out(res, result);
    }

    private GPModelAndView out(HttpServletResponse res, String result) {
        try {
            res.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GPRequestMapping("/remove.json")
    public GPModelAndView remove(HttpServletRequest req, HttpServletResponse res, @GPRequetParam("id") Integer id)
    {
        String result = modifyService.remove(id);
        return out(res, result);
    }

    @GPRequestMapping("/remove.json")
    public GPModelAndView edit(HttpServletRequest req, HttpServletResponse res, @GPRequetParam("id") Integer id, @GPRequetParam("name") String name)
    {
        String result = modifyService.edit(id, name);
        return out(res, result);
    }


}
