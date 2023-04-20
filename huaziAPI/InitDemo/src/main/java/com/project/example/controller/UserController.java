package com.project.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.huazicommon.model.entity.User;
import com.project.example.annotation.AuthCheck;
import com.project.example.common.BaseResponse;
import com.project.example.common.ErrorCode;
import com.project.example.common.ResultUtils;
import com.project.example.exception.BusinessException;

import com.project.example.model.vo.UserLoginRequest;
import com.project.example.model.vo.UserRegisterRequest;
import com.project.example.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.example.constant.UserContant.USER_LOGIN_STATE;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     *  //用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword,request);
        return ResultUtils.success(user);
    }

    /**
     *  //用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkUserPassword = userRegisterRequest.getCheckUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkUserPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不存在");
        }
        long result=userService.userRegister(userAccount,userPassword,checkUserPassword);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户的信息
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"不存在");
        }
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) attribute;
        if (currentUser==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"不存在");
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        //进行脱敏
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 根据用户名查询用户
     * @param userName
     * @param request
     * @return
     */
    @GetMapping("/search")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<User>> searchUser( String userName ,HttpServletRequest request) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> list = userService.list(queryWrapper);
        List<User> userList = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    /**
     * 用户删除
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteUser( long id,HttpServletRequest request){
        if (id<=0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新角色
     * @param user
     * @param request
     * @return
     */
    @GetMapping("/update")
    @AuthCheck(mustRole ="admin")
    public BaseResponse<Integer> updateUser( User user,HttpServletRequest request){
        if (user==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (user.equals(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Integer updateUser = userService.updateUser(user, loginUser);
        return ResultUtils.success(updateUser);
    }


    /**
     * 用户注销
     * @param request
     * @return
     */
    @GetMapping("/loginout")
    public BaseResponse<Integer> userLoginOut(HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int loginOut = userService.userLoginOut(request);
        return ResultUtils.success(loginOut);
    }
}
