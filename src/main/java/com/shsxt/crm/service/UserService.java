package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("all")
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    public UserModel login(String userName,String userPwd){
        /**
         * 1.参数校验
         *      用户名  非空
         *      密码 非空
         * 2.根据用户名查询用户记录
         * 3.校验用户是否存在
         *      不存在-->记录不存在，方法结束
         *      存在-->校验密码，
         *          密码错误-->密码不正确，方法结束
         *          密码正确-->登录成功，返回用户相关信息
         */
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户已注销或不存在");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"密码错误！");
        return buildUserModelInfo(user);




    }

    private UserModel buildUserModelInfo(User user) {
        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());

    }

    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        /**
         * 1.参数校验
         * userId 非空 数据库中存在
         * oldPassword 非空 必须与数据库一致
         * newPassword 非空 不能与原始密码相同
         * comfirmPassword 非空 与新密码一致
         * 2.设置新密码
         *  新密码加密
         * 3.执行更新
         */
        checkParams(userId,oldPassword,newPassword,confirmPassword);
        User user = selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"密码修改失败");



    }

    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null==userId||null==user,"用户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码！");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码！");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码与旧密码不一致！");

        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确！");

        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能与旧密码相同！");
    }
}
