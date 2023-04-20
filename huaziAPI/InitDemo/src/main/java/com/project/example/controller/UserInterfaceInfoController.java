package com.project.example.controller;

import com.example.huazicommon.model.entity.User;
import com.huazi.huaziclientsdk.client.YuApiClient;
import com.project.example.annotation.AuthCheck;
import com.project.example.common.BaseResponse;
import com.project.example.common.ErrorCode;
import com.project.example.common.ResultUtils;
import com.project.example.exception.BusinessException;

import com.project.example.model.dto.UserInterfaceInfo;
import com.project.example.model.dto.userInterface.AddUserInterfaceRequest;
import com.project.example.model.dto.userInterface.DeleteUserInterfaceRequest;
import com.project.example.model.dto.userInterface.UpdateUserInterfaceRequest;
import com.project.example.service.UserInterfaceInfoService;
import com.project.example.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private YuApiClient yuApiClient;

    /**
     * //添加接口
     *
     * @param addUserInterfaceRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody AddUserInterfaceRequest addUserInterfaceRequest, HttpServletRequest request) {
        if (addUserInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(addUserInterfaceRequest, userInterfaceInfo);
        userInterfaceInfoService.validUserInterfacrInfo(userInterfaceInfo, true);
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        //设置创建者
        userInterfaceInfo.setUserId(loginUser.getId());
        //保存用户信息
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        //判断是否存在
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(interfaceInfoId);
    }

    /**
     * //删除接口
     *
     * @param deleteUserInterfaceRequest
     * @param request
     * @return
     */

    @PostMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteInterface(@RequestBody DeleteUserInterfaceRequest deleteUserInterfaceRequest, HttpServletRequest request) {
        if (deleteUserInterfaceRequest == null && deleteUserInterfaceRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteUserInterfaceRequest.getId();
        User loginUser = userService.getLoginUser(request);
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldInterfaceInfo.getId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * //修改接口
     *
     * @param updateUserInterfaceRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateInterface(@RequestBody UpdateUserInterfaceRequest updateUserInterfaceRequest, HttpServletRequest request) {
        if (updateUserInterfaceRequest == null || updateUserInterfaceRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(updateUserInterfaceRequest, userInterfaceInfo);
        userInterfaceInfoService.validUserInterfacrInfo(userInterfaceInfo, false);
        long id = updateUserInterfaceRequest.getId();
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldInterfaceInfo.getId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);

        return ResultUtils.success(result);

    }

    /**
     * //根据id查询接口
     *
     * @param id
     * @return
     */
    @PostMapping("/select")
    public BaseResponse<UserInterfaceInfo> selectById(int id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoServiceById = userInterfaceInfoService.getById(id);
        if (userInterfaceInfoServiceById == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userInterfaceInfoServiceById);
    }

}
