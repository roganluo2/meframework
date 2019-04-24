package com.gperedu.meframework.demo.service.impl;

import com.gperedu.meframework.annotation.GPService;
import com.gperedu.meframework.demo.service.IModifyService;

/**
 * Created by rogan on 2019/4/25.
 */
@GPService
public class ModifyService implements IModifyService {

    @Override
    public String add(String name, String addr) {
        return "add user:{name=" + name + ",addr = " + addr + "}";
    }

    @Override
    public String edit(Integer id, String name) {
        return "edit ,id = " + id  + ",name = " + name;
    }

    @Override
    public String remove(Integer id) {
        return "delete id =" + id;
    }
}
