package com.ehang.responce.tree;

import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ehang
 * @title: Main
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/4/26 17:41
 */
public class Main {
    public static void main(String[] args) {
        List<LrItem> deps = new ArrayList<>();
        deps.add(new LrItem(1, "董事会", 1, 22));
        deps.add(new LrItem(2, "总经理", 2, 19));
        deps.add(new LrItem(3, "董事会秘书", 20, 21));
        deps.add(new LrItem(4, "产品部", 3, 12));
        deps.add(new LrItem(5, "行政总监", 13, 18));
        deps.add(new LrItem(6, "设计部", 4, 5));
        deps.add(new LrItem(7, "技术部", 6, 11));
        deps.add(new LrItem(8, "财务部", 14, 15));
        deps.add(new LrItem(9, "行政部", 16, 17));
        deps.add(new LrItem(10, "客户端", 7, 8));
        deps.add(new LrItem(11, "服务端", 9, 10));

        //reverseFormat(deps);
        recursive(deps);
    }

    public static void reverseFormat(List<LrItem> deps) {
        init(deps);

        deps.sort(Comparator.comparing(LrItem::getLevel));
        deps.forEach(item -> System.out.println(JSON.toJSONString(item)));

        // 临时缓存各自节点的容器
        Map<Integer, List<LrItem>> childCache = new HashMap<>();

        // 当前节点
        LrItem lrItem = null;
        //int level = 0;
        // 采用倒序遍历，整理各个子节点的集合
        for (int i = deps.size() - 1; i >= 0; i--) {
            lrItem = deps.get(i);
            Integer parentId = lrItem.getParentId();
            if (null == lrItem || null == parentId) {
                continue;
            }

            List<LrItem> childItems = childCache.get(parentId);
            if (null == childItems) {
                childCache.put(parentId, childItems = new ArrayList<>());
            }
            childItems.add(lrItem);

            // 如果不是叶子节点的时候，说明他肯定有子节点，去缓存中找到，回填回去
            if (!lrItem.getIsLeaf()) {
                childItems = childCache.get(lrItem.getId());
                childItems.sort(Comparator.comparing(LrItem::getId));
                lrItem.setChildItem(childItems);
                childCache.remove(lrItem.getId());
            }
        }

        System.out.println(JSON.toJSONString(lrItem));
    }

    /**
     * @param deps 所有数据
     */
    public static void recursive(List<LrItem> deps) {
        init(deps);
        //获取父节点
        List<LrItem> collect = deps.stream()
                .filter(m -> m.getParentId() == 0)
                .map(m ->
                        {
                            m.setChildItem(getChildrens(m, deps));
                            return m;
                        }
                ).collect(Collectors.toList());

        System.out.println(JSON.toJSON(collect.get(0)));
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */
    private static List<LrItem> getChildrens(LrItem root, List<LrItem> all) {
        List<LrItem> children = all.stream()
                .filter(m -> Objects.equals(m.getParentId(), root.getId()))
                .map(m -> {
                            m.setChildItem(getChildrens(m, all));
                            return m;
                        }
                ).collect(Collectors.toList());
        return children;
    }

    public static void init(List<LrItem> deps) {
        // 如果数据库排序过了之后  这里就不用排序了
        deps.sort(Comparator.comparing(LrItem::getLeft));

        // 为计算层级 缓存节点右侧的值
        List<Integer> rights = new ArrayList<>();
        Map<Integer, Integer> mp = new HashMap<>();

        deps.forEach(item -> {
            if (rights.size() > 0) {
                // 一旦发现本次节点右侧的值比前一次的大，说明出现层级上移了 需要移除前一个底层及的值
                // 这里大部分情况下只存在移除前面一个值情况
                while (rights.get(rights.size() - 1) < item.getRight()) {
                    rights.remove(rights.size() - 1);//从rights末尾去除
                }
            }
            Integer _level = rights.size() + 1;
            item.setLevel(_level);
            mp.put(_level, item.getId());
            item.setParentId(mp.containsKey(_level - 1) ? mp.get(_level - 1) : 0); //计算出上级部门编号
            item.setIsLeaf(item.getLeft() == item.getRight() - 1);//判断是否叶子部门
            rights.add(item.getRight());
        });

        System.out.println(rights);
        System.out.println(mp);
    }
}
