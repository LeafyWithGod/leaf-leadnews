package com.leaf.user.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.model.common.dtos.ResponseResult;
import com.leaf.model.common.enums.AppHttpCodeEnum;
import com.leaf.model.user.dtos.LoginDto;
import com.leaf.model.user.pojos.ApUser;
import com.leaf.user.mapper.ApUserMapper;
import com.leaf.user.service.ApUserService;
import com.leaf.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Autowired
    private ApUserMapper apUserMapper;

    @Override
    public ResponseResult login(LoginDto loginDto) {
        //游客登录
        if (StringUtils.isEmpty(loginDto.getPhone()) || StringUtils.isEmpty(loginDto.getPassword())) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
        //正常登录
        //根据手机号查询用户
        ApUser apUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, loginDto.getPhone()));
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }
        //加盐比对
        String salt = apUser.getSalt();
        String password = loginDto.getPassword();
        String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        if (!pswd.equals(apUser.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //密码没问题返回数据
        String token = AppJwtUtil.getToken(apUser.getId().longValue());
        Map<String, Object> map = new HashMap<>();
        //数据做空处理，避免被拦截盗取
        apUser.setSalt("");
        apUser.setPassword("");
        map.put("user", apUser);
        map.put("token", token);
        return ResponseResult.okResult(map);
    }
}
