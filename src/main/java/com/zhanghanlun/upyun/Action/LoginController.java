package com.zhanghanlun.upyun.Action;


import com.zhanghanlun.upyun.Entity.User;
import com.zhanghanlun.upyun.Service.LoginService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @RequestMapping("/login")
    public Map<String,Object> login(@RequestBody User user){
        Map<String,Object> result = new HashMap<>();
        boolean flag = loginService.login(user);
        if (flag){
           result.put("loginFlag",true);
           result.put("msg","登陆成功");
        }else{
            result.put("loginFlag",false);
            result.put("msg","用户名或密码错误");
        }
        return result;
    }
}
