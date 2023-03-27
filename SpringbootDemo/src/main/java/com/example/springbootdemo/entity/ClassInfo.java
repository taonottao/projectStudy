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
public class ClassInfo implements Serializable {
    private static final Long serialVersionUID = 1111L;

    private Long id;
    private String className;
    private String teacherName;
}
