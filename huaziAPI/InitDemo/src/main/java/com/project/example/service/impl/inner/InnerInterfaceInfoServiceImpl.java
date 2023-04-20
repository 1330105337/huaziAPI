package com.project.example.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.huazicommon.model.entity.InterfaceInfo;
import com.example.huazicommon.service.InnerInterfaceInfoService;
import com.project.example.common.ErrorCode;
import com.project.example.exception.BusinessException;
import com.project.example.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfo;
    }
}
