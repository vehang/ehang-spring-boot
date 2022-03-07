package com.ehang.common.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LENOVO
 * @title: ParamErrDto
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 16:58
 */
@Data
@AllArgsConstructor
public class ParamErrDto {
    private String param;

    private String errMsg;
}
