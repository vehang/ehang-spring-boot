package com.ehang.tools.mapstruct.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressDTO {
    private String country;

    private String province;

    private String city;
}
