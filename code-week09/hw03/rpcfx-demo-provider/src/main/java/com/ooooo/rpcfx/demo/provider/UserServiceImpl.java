package com.ooooo.rpcfx.demo.provider;


import com.ooooo.rpcfx.demo.api.User;
import com.ooooo.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
