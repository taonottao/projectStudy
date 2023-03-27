package com.example.springbootdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TbUserInfo implements Serializable {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer age ;
    private String address;




}
