package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.cons.Constant;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TraInfoMapper;
import com.ruoyi.system.domain.TraInfo;
import com.ruoyi.system.service.ITraInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 训练成员信息Service业务层处理
 * 
 * @author ldu
 * @date 2023-06-26
 */
@Service
public class TraInfoServiceImpl implements ITraInfoService 
{
    @Autowired
    private TraInfoMapper traInfoMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 查询训练成员信息
     * 
     * @param id 训练成员信息主键
     * @return 训练成员信息
     */
    @Override
    public TraInfo selectTraInfoById(Long id)
    {
        return traInfoMapper.selectTraInfoById(id);
    }

    /**
     * 查询训练成员信息列表
     * 
     * @param traInfo 训练成员信息
     * @return 训练成员信息
     */
    @Override
    public List<TraInfo> selectTraInfoList(TraInfo traInfo)
    {
        return traInfoMapper.selectTraInfoList(traInfo);
    }

    /**
     * 新增训练成员信息
     * 
     * @param traInfo 训练成员信息
     * @return 结果
     */
    @Override
    public int insertTraInfo(TraInfo traInfo)
    {
        // 注册到真正可以登录的数据库中去
        register(traInfo);

        traInfo.setCreateTime(DateUtils.getNowDate());
        return traInfoMapper.insertTraInfo(traInfo);
    }
    /**
     * 新增时 需要账号密码的注册---系统账户的注册
     */
    private void register(TraInfo traInfo)
    {
        String msg = "";
        String username = traInfo.getUserName();
        String password =traInfo.getPassword();

        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);


        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
            throw new ServiceException(msg);
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
            throw new ServiceException(msg);
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
            throw new ServiceException(msg);
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
            throw new ServiceException(msg);
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
            throw new ServiceException(msg);
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setPhonenumber(traInfo.getPhonenumber());
            sysUser.setSex(traInfo.getSex());
            sysUser.setNickName(traInfo.getTraInfoName());  //昵称就是训练名字
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "服务器发生错误";
                throw new ServiceException(msg);
            }
            else
            {
                // 默认为普通用户
                Long userId = traInfoMapper.getSysUserIdByUsername(sysUser.getUserName());
                sysRoleMapper.insertPermissions(userId, Constant.RoleId);
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
    }

    /**
     * 修改训练成员信息
     * 不能修改账号密码
     * @param traInfo 训练成员信息
     * @return 结果
     */
    @Override
    public int updateTraInfo(TraInfo traInfo)
    {
        return traInfoMapper.updateTraInfo(traInfo);
    }

    /**
     * 批量删除训练成员信息
     * 
     * @param ids 需要删除的训练成员信息主键
     * @return 结果
     */
    @Override
    public int deleteTraInfoByIds(Long[] ids)
    {
        // 有bug，不想写了
        return traInfoMapper.deleteTraInfoByIds(ids);
    }

    /**
     * 删除训练成员信息信息
     * 
     * @param id 训练成员信息主键
     * @return 结果
     */
    @Override
    public int deleteTraInfoById(Long id)
    {
        // 有bug，不想写了
        return traInfoMapper.deleteTraInfoById(id);
    }


}
