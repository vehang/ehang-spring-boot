/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.237
 Source Server Type    : MySQL
 Source Server Version : 50713
 Source Host           : 192.168.1.237:3306
 Source Schema         : ehang

 Target Server Type    : MySQL
 Target Server Version : 50713
 File Encoding         : 65001

 Date: 30/11/2021 00:08:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info`  (
  `id` int(11) NOT NULL,
  `class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `class_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `school_id` int(11) NOT NULL COMMENT '隶属的学校',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_info
-- ----------------------------
INSERT INTO `class_info` VALUES (1, '一年级1班', NULL, 1);
INSERT INTO `class_info` VALUES (2, '一年级2班', NULL, 1);

-- ----------------------------
-- Table structure for school_info
-- ----------------------------
DROP TABLE IF EXISTS `school_info`;
CREATE TABLE `school_info`  (
  `id` int(11) NOT NULL,
  `school_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校名称',
  `school_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of school_info
-- ----------------------------
INSERT INTO `school_info` VALUES (1, 'XXX小学', 'xx区xx街道80号');

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
  `id` int(11) NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `class_id` int(11) NULL DEFAULT NULL,
  `school_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_info
-- ----------------------------
INSERT INTO `student_info` VALUES (1, '张三', 7, 1, 1);
INSERT INTO `student_info` VALUES (2, '李四', 7, 2, 1);
INSERT INTO `student_info` VALUES (3, '王五', 8, 1, 1);
INSERT INTO `student_info` VALUES (4, '赵六', 8, 1, 1);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `source` tinyint(4) NULL DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `source_id`(`source`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1008 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, '一行Java 1', 10, 1);
INSERT INTO `user_info` VALUES (2, '一行Java 2', 20, 1);
INSERT INTO `user_info` VALUES (3, '一行Java 1', 30, 1);
INSERT INTO `user_info` VALUES (4, '张三（改）', 31, 2);
INSERT INTO `user_info` VALUES (5, '张三（改）', 18, 2);
INSERT INTO `user_info` VALUES (6, '张三（改）', 96, 0);
INSERT INTO `user_info` VALUES (7, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (8, '张三（改）', 85, 0);
INSERT INTO `user_info` VALUES (9, '张三（改）', 83, 2);
INSERT INTO `user_info` VALUES (10, '张三（改）', 88, 0);
INSERT INTO `user_info` VALUES (11, '张三（改）', 21, 0);
INSERT INTO `user_info` VALUES (12, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (13, '张三（改）', 32, 0);
INSERT INTO `user_info` VALUES (14, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (15, '张三（改）', 96, 2);
INSERT INTO `user_info` VALUES (16, '张三（改）', 11, 0);
INSERT INTO `user_info` VALUES (17, '张三（改）', 78, 0);
INSERT INTO `user_info` VALUES (18, '张三（改）', 69, 0);
INSERT INTO `user_info` VALUES (19, '张三（改）', 61, 0);
INSERT INTO `user_info` VALUES (20, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (21, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (22, '张三（改）', 49, 2);
INSERT INTO `user_info` VALUES (23, '张三（改）', 14, 0);
INSERT INTO `user_info` VALUES (24, '张三（改）', 99, 2);
INSERT INTO `user_info` VALUES (25, '张三（改）', 68, 2);
INSERT INTO `user_info` VALUES (26, '张三（改）', 0, 0);
INSERT INTO `user_info` VALUES (27, '张三（改）', 57, 0);
INSERT INTO `user_info` VALUES (28, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (29, '张三（改）', 93, 0);
INSERT INTO `user_info` VALUES (30, '张三（改）', 3, 2);
INSERT INTO `user_info` VALUES (31, '张三（改）', 79, 0);
INSERT INTO `user_info` VALUES (32, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (33, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (34, '张三（改）', 46, 0);
INSERT INTO `user_info` VALUES (35, '张三（改）', 15, 2);
INSERT INTO `user_info` VALUES (36, '张三（改）', 23, 2);
INSERT INTO `user_info` VALUES (37, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (38, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (39, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (40, '张三（改）', 64, 0);
INSERT INTO `user_info` VALUES (41, '张三（改）', 42, 2);
INSERT INTO `user_info` VALUES (42, '张三（改）', 52, 0);
INSERT INTO `user_info` VALUES (43, '张三（改）', 43, 2);
INSERT INTO `user_info` VALUES (44, '张三（改）', 67, 2);
INSERT INTO `user_info` VALUES (45, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (46, '张三（改）', 26, 0);
INSERT INTO `user_info` VALUES (47, '张三（改）', 3, 2);
INSERT INTO `user_info` VALUES (48, '张三（改）', 70, 2);
INSERT INTO `user_info` VALUES (49, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (50, '张三（改）', 89, 0);
INSERT INTO `user_info` VALUES (51, '张三（改）', 45, 0);
INSERT INTO `user_info` VALUES (52, '张三（改）', 13, 0);
INSERT INTO `user_info` VALUES (53, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (54, '张三（改）', 80, 0);
INSERT INTO `user_info` VALUES (55, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (56, '张三（改）', 13, 2);
INSERT INTO `user_info` VALUES (57, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (58, '张三（改）', 15, 2);
INSERT INTO `user_info` VALUES (59, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (60, '张三（改）', 51, 2);
INSERT INTO `user_info` VALUES (61, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (62, '张三（改）', 62, 2);
INSERT INTO `user_info` VALUES (63, '张三（改）', 6, 2);
INSERT INTO `user_info` VALUES (64, '张三（改）', 2, 0);
INSERT INTO `user_info` VALUES (65, '张三（改）', 89, 2);
INSERT INTO `user_info` VALUES (66, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (67, '张三（改）', 62, 2);
INSERT INTO `user_info` VALUES (68, '张三（改）', 68, 2);
INSERT INTO `user_info` VALUES (69, '张三（改）', 57, 2);
INSERT INTO `user_info` VALUES (70, '张三（改）', 46, 2);
INSERT INTO `user_info` VALUES (71, '张三（改）', 33, 2);
INSERT INTO `user_info` VALUES (72, '张三（改）', 77, 2);
INSERT INTO `user_info` VALUES (73, '张三（改）', 31, 0);
INSERT INTO `user_info` VALUES (74, '张三（改）', 78, 0);
INSERT INTO `user_info` VALUES (75, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (76, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (77, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (78, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (79, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (80, '张三（改）', 59, 0);
INSERT INTO `user_info` VALUES (81, '张三（改）', 36, 2);
INSERT INTO `user_info` VALUES (82, '张三（改）', 84, 0);
INSERT INTO `user_info` VALUES (83, '张三（改）', 53, 2);
INSERT INTO `user_info` VALUES (84, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (85, '张三（改）', 71, 0);
INSERT INTO `user_info` VALUES (86, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (87, '张三（改）', 78, 2);
INSERT INTO `user_info` VALUES (88, '张三（改）', 88, 2);
INSERT INTO `user_info` VALUES (89, '张三（改）', 46, 0);
INSERT INTO `user_info` VALUES (90, '张三（改）', 53, 2);
INSERT INTO `user_info` VALUES (91, '张三（改）', 18, 2);
INSERT INTO `user_info` VALUES (92, '张三（改）', 28, 0);
INSERT INTO `user_info` VALUES (93, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (94, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (95, '张三（改）', 17, 2);
INSERT INTO `user_info` VALUES (96, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (97, '张三（改）', 37, 2);
INSERT INTO `user_info` VALUES (98, '张三（改）', 20, 1);
INSERT INTO `user_info` VALUES (99, '张三（改）', 68, 2);
INSERT INTO `user_info` VALUES (100, '张三（改）', 26, 2);
INSERT INTO `user_info` VALUES (1004, '张三', 10, 1);
INSERT INTO `user_info` VALUES (1005, '李四', 10, 1);
INSERT INTO `user_info` VALUES (1006, '王五（改2）', 10, 1);
INSERT INTO `user_info` VALUES (1007, '张三', 10, 1);

SET FOREIGN_KEY_CHECKS = 1;
