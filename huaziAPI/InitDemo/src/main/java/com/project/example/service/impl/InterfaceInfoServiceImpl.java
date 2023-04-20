package com.project.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.huazicommon.model.entity.InterfaceInfo;
import com.project.example.common.ErrorCode;
import com.project.example.exception.BusinessException;
import com.project.example.mapper.InterfaceInfoMapper;


import com.project.example.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-03-16 17:24:41
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfacrInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name=interfaceInfo.getName();
        if (add){
            if (StringUtils.isAnyBlank(name)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length()>50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容过长");
        }


    }

}




