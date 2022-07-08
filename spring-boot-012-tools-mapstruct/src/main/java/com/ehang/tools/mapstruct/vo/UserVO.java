package com.ehang.tools.mapstruct.vo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UserVO {
    private String name;

    private Integer age;

    private Date createTime;
}
