package com.example.wms2.service.impl;

import com.example.wms2.entity.User;
import com.example.wms2.mapper.UserMapper;
import com.example.wms2.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wt
 * @since 2023-10-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
