package com.leaf.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.model.common.dtos.ResponseResult;
import com.leaf.model.user.dtos.LoginDto;
import com.leaf.model.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser> {

    ResponseResult login(LoginDto loginDto);
}
