package com.ehang.responce.tree;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author ehang
 * @title: Item
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/4/26 17:41
 */
@Data
@RequiredArgsConstructor
public class Item {

    @NonNull
    private Integer id;

    @NonNull
    private String depName;

    @NonNull
    private Integer level;

    @NonNull
    private Integer prientId;

    /**
     * 是否是叶子
     */
    private Boolean isLeaf;

    private List<Item> childItem;
}
