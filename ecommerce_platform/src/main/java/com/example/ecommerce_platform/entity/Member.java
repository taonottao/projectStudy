package com.example.ecommerce_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wt
 * @since 2023-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Member对象", description="")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String uname;

    private String upass;

    private String tname;

    private String sex;

    private String birthtime;

    private String tel;

    private String email;

    private String delstatus;

    private String filename;

    private String status;

    private String idcard;

    private String utype;


}
