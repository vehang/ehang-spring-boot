package com.ehang.responce.tree;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author ehang
 * @title: LrItem
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/4/27 11:35
 */
@Data
@RequiredArgsConstructor
public class LrItem {

    @NonNull
    private Integer id;

    @NonNull
    private String depName;

    @NonNull
    private Integer left;

    @NonNull
    private Integer right;

    private Integer level;

    private Integer parentId;

    /**
     * 是否是叶子
     */
    private Boolean isLeaf;

    private List<LrItem> childItem;
}
