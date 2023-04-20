package com.project.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.huazicommon.model.entity.InterfaceInfo;
import com.example.huazicommon.model.entity.User;
import com.google.gson.Gson;
import com.huazi.huaziclientsdk.client.YuApiClient;
import com.project.example.annotation.AuthCheck;
import com.project.example.common.BaseResponse;
import com.project.example.common.ErrorCode;
import com.project.example.common.ResultUtils;
import com.project.example.constant.CommonConstant;
import com.project.example.exception.BusinessException;


import com.project.example.model.dto.userInterface.AddUserInterfaceRequest;
import com.project.example.model.enums.InterfaceInfoStatusEnum;
import com.project.example.model.vo.*;
import com.project.example.service.InterfaceInfoService;
import com.project.example.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

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
    public BaseResponse<Long> addInterfaceInfo(@RequestBody AddUserInterfaceRequest addUserInterfaceRequest, HttpServletRequest request) {
        if (addUserInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(addUserInterfaceRequest, interfaceInfo);
        interfaceInfoService.validInterfacrInfo(interfaceInfo, true);
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        //设置创建者
        interfaceInfo.setUserId(loginUser.getId());
        //保存用户信息
        boolean result = interfaceInfoService.save(interfaceInfo);
        //判断是否存在
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(interfaceInfoId);
    }

    /**
     * //删除接口
     *
     * @param deleteInterfaceRequest
     * @param request
     * @return
     */

    @PostMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteInterface(@RequestBody DeleteInterfaceRequest deleteInterfaceRequest, HttpServletRequest request) {
        if (deleteInterfaceRequest == null && deleteInterfaceRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteInterfaceRequest.getId();
        User loginUser = userService.getLoginUser(request);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldInterfaceInfo.getId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * //修改接口
     *
     * @param updateInterfaceRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateInterface(@RequestBody UpdateInterfaceRequest updateInterfaceRequest, HttpServletRequest request) {
        if (updateInterfaceRequest == null || updateInterfaceRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(updateInterfaceRequest, interfaceInfo);
        interfaceInfoService.validInterfacrInfo(interfaceInfo, false);
        long id = updateInterfaceRequest.getId();
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldInterfaceInfo.getId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(result);

    }

    /**
     * //根据id查询接口
     *
     * @param id
     * @return
     */
    @PostMapping("/select")
    public BaseResponse<InterfaceInfo> selectById(int id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo userInterfaceInfoServiceById = interfaceInfoService.getById(id);
        if (userInterfaceInfoServiceById == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userInterfaceInfoServiceById);
    }

    /**
     * 分页查询
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> selectPageInterface(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        long current = interfaceInfoQueryRequest.getCurrent();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        //防止爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>(interfaceInfoQuery);

        //模糊查询描述
        interfaceInfoQueryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        //以升序进行排序
        interfaceInfoQueryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, pageSize), interfaceInfoQueryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 列表查询（仅管理员可见）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfo>> selectListInterface(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (interfaceInfo != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        }
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>(interfaceInfo);
        List<InterfaceInfo> list = interfaceInfoService.list(interfaceInfoQueryWrapper);
        return ResultUtils.success(list);
    }


    /**
     * //上线接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        //判断接口是否存在
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        InterfaceInfo byId = interfaceInfoService.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //判断接口是否可以调用
        com.huazi.huaziclientsdk.model.User user = new  com.huazi.huaziclientsdk.model.User();
        user.setUsername("test");
        String username = yuApiClient.getUsernameByPost(user);
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setUserId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(result);

    }

    /**
     * //下线接口
     *
     * @param idRequest
     * @param request
     * @return
     */

    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        //判断接口是否存在
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        InterfaceInfo byId = interfaceInfoService.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setUserId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(result);

    }
    @PostMapping("/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Object> invokeInterface(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request){
        //判断接口是否存在
        if(interfaceInfoInvokeRequest==null || interfaceInfoInvokeRequest.getId() <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();
        InterfaceInfo oldInterface = interfaceInfoService.getById(id);
        if (oldInterface==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterface.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口已关闭");
        }
       //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        String secretKey = loginUser.getSecretKey();
        String accessKey = loginUser.getAccessKey();
        YuApiClient client = new YuApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        //将user转换为json格式
        com.huazi.huaziclientsdk.model.User user=gson.fromJson(requestParams, com.huazi.huaziclientsdk.model.User.class);
        String usernameByPost = client.getUsernameByPost(user);
        return ResultUtils.success(usernameByPost);

    }
}
