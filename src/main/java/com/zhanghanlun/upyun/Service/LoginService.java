package com.zhanghanlun.upyun.Service;


import com.zhanghanlun.upyun.Entity.User;
import com.zhanghanlun.upyun.Mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginService {

    @Resource private UserMapper userMapper;

    public boolean login(User user) {
        User u = userMapper.selectByUserName(user.getUsername());
        if (u != null && u.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}
