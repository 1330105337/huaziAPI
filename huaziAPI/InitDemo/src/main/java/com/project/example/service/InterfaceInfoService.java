package com.project.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.huazicommon.model.entity.InterfaceInfo;


/**
* @author 86187
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-03-16 17:24:41
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {


    void validInterfacrInfo(InterfaceInfo interfaceInfo, boolean add);
}
