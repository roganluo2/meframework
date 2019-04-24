package com.gperedu.meframework.demo.service;

/**
 * Created by rogan on 2019/4/25.
 */
public interface IModifyService {

    String add(String name, String addr);

    String edit(Integer id, String name);

    String remove(Integer id);
}
