/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : 博客系统

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 11/07/2024 13:36:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `blog_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '博客表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, '1', '1\n**粗体**', 1, '', '2024-07-10 13:47:35', '', '2024-07-10 23:17:18', '');
INSERT INTO `blog` VALUES (2, '测试标题', '测试内容2', 2, '', '2024-07-10 13:56:26', '', '2024-07-10 13:56:26', '');
INSERT INTO `blog` VALUES (3, '11', '<p>111</p>', 1, '1', '2024-07-10 15:48:01', '1', '2024-07-10 15:48:01', '');
INSERT INTO `blog` VALUES (4, '1', '<p>111</p>', 2, '1', '2024-07-10 15:50:20', '1', '2024-07-10 15:50:20', '');
INSERT INTO `blog` VALUES (5, '111', '1. [1111111- 无序列表](http://)\n2. ![Description](http://)\n3. ``` language safasf\n```', 1, '1', '2024-07-10 20:34:05', '1', '2024-07-10 20:34:05', NULL);
INSERT INTO `blog` VALUES (6, '测试', '测试``` ``` language\n[![Description](https://p.qpic.cn/dnfbbspic/0/dnfbbs_dnfbbs_dnf_gamebbs_qq_com_forum_201911_22_083548z4dn4295a5ej89ad.jpg/0)](https://p.qpic.cn/dnfbbspic/0/dnfbbs_dnfbbs_dnf_gamebbs_qq_com_forum_201911_22_083548z4dn4295a5ej89ad.jpg/0)', 1, '1', '2024-07-10 20:34:50', '1', '2024-07-10 23:46:42', NULL);
INSERT INTO `blog` VALUES (7, '1', '# 一级标题\n## 二级标题\n## 二级标题\n### 三级标题\n# 一级标题\n## 二级标题\n## 二级标题\n### 三级标题\n# 一级标题', 1, '1', '2024-07-10 22:35:15', '1', '2024-07-10 22:35:15', NULL);

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `blog_id` bigint NOT NULL COMMENT '博客ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `blog_id`(`blog_id`) USING BTREE,
  INDEX `tag_id`(`tag_id`) USING BTREE,
  CONSTRAINT `blog_tag_ibfk_1` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `blog_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '博客标签关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sender_id`(`sender_id`) USING BTREE,
  INDEX `receiver_id`(`receiver_id`) USING BTREE,
  CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------
INSERT INTO `chat` VALUES (1, 1, 2, '测试', '管理员', '2024-07-11 00:47:21', '管理员', '2024-07-11 00:47:28', NULL);
INSERT INTO `chat` VALUES (2, 1, 2, '回复消息', '1', '2024-07-11 12:36:05', '1', '2024-07-11 12:36:05', NULL);
INSERT INTO `chat` VALUES (3, 2, 1, '测试回复', '2', '2024-07-11 12:40:49', '2', '2024-07-11 12:40:49', NULL);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `blog_id` bigint NOT NULL COMMENT '博客ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `blog_id`(`blog_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, 1, '1', '1', '2024-07-10 18:58:29', '1', '2024-07-10 18:58:29', NULL);
INSERT INTO `comment` VALUES (2, 1, 1, '测试回复', '1', '2024-07-10 19:15:26', '1', '2024-07-10 19:15:26', NULL);
INSERT INTO `comment` VALUES (3, 1, 1, '测试评论2', '1', '2024-07-10 19:16:46', '1', '2024-07-10 19:16:46', NULL);
INSERT INTO `comment` VALUES (4, 1, 1, '测试评论3', '1', '2024-07-10 19:17:22', '1', '2024-07-10 19:17:22', NULL);
INSERT INTO `comment` VALUES (5, 2, 1, '测试评论', '1', '2024-07-10 19:38:58', '1', '2024-07-10 19:38:58', NULL);
INSERT INTO `comment` VALUES (6, 6, 1, '1', '1', '2024-07-10 20:39:51', '1', '2024-07-10 20:39:51', NULL);
INSERT INTO `comment` VALUES (7, 1, 2, '测试', '2', '2024-07-11 00:27:38', '2', '2024-07-11 00:27:38', NULL);

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `follower_id` bigint UNSIGNED NOT NULL COMMENT '关注者ID',
  `followed_id` bigint UNSIGNED NOT NULL COMMENT '被关注者ID',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_follow`(`follower_id`, `followed_id`) USING BTREE COMMENT '唯一关注关系'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (1, 1, 2, '管理员', '2024-07-11 12:00:31', '管理员', '2024-07-11 12:00:59', NULL);
INSERT INTO `follow` VALUES (2, 2, 1, NULL, '2024-07-11 12:40:07', NULL, '2024-07-11 12:40:09', NULL);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态(0禁用、1正常)',
  `role` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色(0管理员、1用户)',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '1', '管理员', '/files/ea3bee94-c535-4af3-b89e-44d161049b4c.jpg', '1', '0', '1', '1', 0.00, NULL, NULL, '1', '2024-07-10 15:10:25', NULL);
INSERT INTO `user` VALUES (2, '2', '2', '测试2', '/files/3660e62f-ed35-477e-8c3e-b84fda8706ae.jpg', '1', '1', '111', '222@qq.com', 0.00, '', '2024-07-10 13:48:40', '', '2024-07-11 12:58:29', NULL);

SET FOREIGN_KEY_CHECKS = 1;
