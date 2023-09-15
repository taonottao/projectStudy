package com.example.ecommerce_platform.mapper;

import com.example.ecommerce_platform.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wt
 * @since 2023-09-14
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    Member login(Member member);

}
