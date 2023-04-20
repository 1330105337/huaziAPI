package com.project.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.example.common.ErrorCode;
import com.project.example.exception.BusinessException;
import com.project.example.mapper.UserInterfaceInfoMapper;
import com.project.example.model.dto.UserInterfaceInfo;
import com.project.example.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【user_interface_info】的数据库操作Service实现
* @createDate 2023-03-23 22:17:14
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfacrInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //Todo 此处需要获取多个参数进行校验
        Long userId = userInterfaceInfo.getUserId();
        if (add){
            if (userId<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
//        if (StringUtils.isNotBlank(name) && name.length()>50){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容过长");
//        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId<=0 || userId<=0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //下面代码执行的sql
        //update user_interface_info set leftNum=leftNum-1,totalNum=totalNum+1 where userId=1 and interfaceInfoId=1
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");
        boolean update = this.update(updateWrapper);
        return update;

    }
}




