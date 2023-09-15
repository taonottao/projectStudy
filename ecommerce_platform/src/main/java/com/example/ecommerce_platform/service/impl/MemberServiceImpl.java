package com.example.ecommerce_platform.service.impl;

import com.example.ecommerce_platform.entity.Member;
import com.example.ecommerce_platform.mapper.MemberMapper;
import com.example.ecommerce_platform.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wt
 * @since 2023-09-14
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public Member login(Member member) {
        return memberMapper.login(member);
    }
}
