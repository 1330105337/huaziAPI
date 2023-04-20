package com.project.example.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.huazicommon.model.entity.User;
import com.project.example.common.ErrorCode;
import com.project.example.exception.BusinessException;
import com.project.example.mapper.UserMapper;


import com.project.example.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.example.constant.UserContant.USER_LOGIN_STATE;


/**
* @author 86187
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-03-16 21:54:03
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    private static final String SALT="huazi";

    @Resource
    private UserMapper userMapper;


    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return (User) attribute;
    }

    /**
     * 是否是管理员
     * @param loginUser
     * @return
     */

    

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword,String checkUserPassword) {
        //首先判断三个参数是否为空
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkUserPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断账户是否有特殊符号
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //将账户与自定义的validpattern进行匹配
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        //如果匹配包含，报异常
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"含特殊字符");
        }
        //判断三个参数长度
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户账号过短");
        }
        if (userPassword.length()<8 || checkUserPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户密码过短");
        }
        //判断输入密码与检验密码是否相同
        if (!userPassword.equals(checkUserPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码输入不正确");
        }
        //检验数据库是否有和已创建用户的相同的名字
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }
        //对密码进行加密（加盐）
        String newPassword= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //设置accessKey,secretKey
        String accessKey= DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(5));
        String secretKey= DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(8));
        //向数据库里插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        user.setUserName(userAccount);
        user.setUserAvater("555");
        boolean save = this.save(user);
        if (!save){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //返回
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断账户是否有特殊符号
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //将账户与自定义的validpattern进行匹配
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        //如果匹配包含，报异常
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"含特殊字符");
        }
        //判断三个参数长度
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户账号过短");
        }
        if (userPassword.length()<8 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户密码过短");
        }
        //对密码进行加密（加盐）
        String newPassword= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",newPassword);
        User user=userMapper.selectOne(queryWrapper);
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //用户脱敏
        User safeUser=getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return safeUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"不存在用户");
        }
        User safetyUser= new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setUserAvater(originUser.getUserAvater());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserName(originUser.getUserName());
        return safetyUser;
    }

    /**
     * 用户更新
     * @param user
     * @param loginUser
     * @return
     */
    @Override
    public int updateUser( User user, User loginUser) {
        Long userId = user.getId();
        if (userId<=0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //判断是否为管理员
        if ( !loginUser.getId().equals(userId)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User selectById = userMapper.selectById(userId);
        if (selectById==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @Override
    public int userLoginOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




