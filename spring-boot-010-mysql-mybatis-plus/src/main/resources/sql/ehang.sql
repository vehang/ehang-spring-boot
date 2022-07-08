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
INSERT INTO `user_info` VALUES (3, '一行Java 3', 30, 1);

SET FOREIGN_KEY_CHECKS = 1;
