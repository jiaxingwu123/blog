package com.wjx.blog1.service;

import com.wjx.blog1.po.User;

public interface UserService {

    User checkUser(String username, String password);
}