package com.project.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.huazicommon.model.entity.User;


import javax.servlet.http.HttpServletRequest;

/**
* @author 86187
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-03-16 21:54:03
*/
public interface UserService extends IService<User> {


    User getLoginUser(HttpServletRequest request);
    

    long userRegister(String userAccount, String userPassword, String checkUserPassword);

    User userLogin(String userAccount, String userPassword,HttpServletRequest request);

    User getSafetyUser(User user);

    int updateUser(User user, User loginUser);

    int userLoginOut(HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @return
     */

}
