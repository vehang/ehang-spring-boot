package com.ehang.responce.service.dto;

import com.ehang.common.base.dto.BaseResponceDto;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * @author LENOVO
 * @title: UserInfoDto
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 22:16
 */
@Data
public class UserInfoDto {
    @JsonView(UserInfoListView.class)
    private Integer id;

    @JsonView(UserInfoListView.class)
    private String userName;

    @JsonView(UserInfoListView.class)
    private Integer age;

    @JsonView(UserInfoDetailView.class)
    private String passWord;

    public interface UserInfoListView extends BaseResponceDto.ResponceBaseDtoView {
    }

    public interface UserInfoDetailView extends UserInfoListView {
    }
}
