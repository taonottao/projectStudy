package com.example.ecommerce_platform.service;

import com.example.ecommerce_platform.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wt
 * @since 2023-09-14
 */
public interface MemberService extends IService<Member> {

    Member login(Member member);

}
