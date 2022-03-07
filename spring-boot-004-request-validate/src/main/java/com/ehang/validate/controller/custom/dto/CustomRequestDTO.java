package com.ehang.validate.controller.custom.dto;

import com.ehang.validate.controller.custom.utils.UpperOrLowerCase;
import com.ehang.validate.controller.custom.utils.UpperOrLowerCaseAnno;
import lombok.Data;

/**
 * @author ehang
 * @title: CustomRequestDto
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 14:02
 */
@Data
public class CustomRequestDTO {

    /**
     * 用户名
     * 必须是全部大写的字母
     */
    @UpperOrLowerCaseAnno
    public String userName;

    /**
     * 昵称
     * 必须是全部小写的字母
     */
    @UpperOrLowerCaseAnno(UpperOrLowerCase.LOWER)
    public String nickName;
}
