/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : slider_verification_code

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 12/05/2020 19:43:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for djr_t_users
-- ----------------------------
# DROP TABLE IF EXISTS `djr_t_users`;
CREATE TABLE IF NOT EXISTS `djr_t_users`  (
  `id` bigint(20) NOT NULL COMMENT '信息主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of djr_t_users
-- ----------------------------
INSERT IGNORE INTO `djr_t_users` (`id`, `name`, `account`, `password`, `email`, `telephone`) VALUES (24814027838345218, '贾荣', 'admin', 'ef20cd999bf1f9129250f3a2a6767ae1', '852203465@qq.com', '15019202295');

SET FOREIGN_KEY_CHECKS = 1;
