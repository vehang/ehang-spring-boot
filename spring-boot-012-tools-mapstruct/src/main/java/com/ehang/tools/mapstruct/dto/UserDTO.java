package com.ehang.tools.mapstruct.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String name;

    private Integer age;

    private Date createTime;

    private Date updateTime;

    private Double wallet;

    private BigDecimal deposit;

    private AddressDTO addressDTO;
}
