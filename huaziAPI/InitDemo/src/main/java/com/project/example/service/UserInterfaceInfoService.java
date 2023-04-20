package com.project.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.example.model.dto.UserInterfaceInfo;


/**
* @author 86187
* @description 针对表【user_interface_info】的数据库操作Service
* @createDate 2023-03-23 22:16:34
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfacrInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 用户次数统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount (long interfaceInfoId, long userId);
}
