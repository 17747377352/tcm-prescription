-- 中药智能抓配平台数据库表结构
-- Database: tcm-prescription

-- 1. 中药材表
CREATE TABLE IF NOT EXISTS `tcm_herb` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '药材名称',
    `name_latin` VARCHAR(100) COMMENT '拉丁名',
    `name_alias` VARCHAR(200) COMMENT '别名(JSON数组)',
    `source` VARCHAR(200) COMMENT '来源(植物/动物/矿物)',
    `processing` VARCHAR(100) COMMENT '炮制规格',
    `nature` VARCHAR(10) COMMENT '四气(寒/热/温/凉/平)',
    `flavor` VARCHAR(50) COMMENT '五味(JSON数组: 酸苦甘辛咸淡涩)',
    `meridian` VARCHAR(100) COMMENT '归经(JSON数组)',
    `direction` VARCHAR(20) COMMENT '升降浮沉',
    `efficacy` TEXT COMMENT '功效描述',
    `indications` TEXT COMMENT '主治病症',
    `pharmacology` TEXT COMMENT '现代药理',
    `toxicity_level` TINYINT DEFAULT 0 COMMENT '毒性等级(0无毒/1小毒/2有毒/3大毒)',
    `dosage_min` DECIMAL(10,2) COMMENT '最小用量(g)',
    `dosage_max` DECIMAL(10,2) COMMENT '最大用量(g)',
    `dosage_unit` VARCHAR(10) DEFAULT 'g' COMMENT '用量单位',
    `contraindications` TEXT COMMENT '禁忌人群',
    `incompatibility` VARCHAR(500) COMMENT '配伍禁忌(JSON数组: 十八反十九畏)',
    `classic_formulas` TEXT COMMENT '经典方剂TOP10(JSON数组)',
    `image_url` VARCHAR(500) COMMENT '药材图片URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0禁用/1启用)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_nature` (`nature`),
    KEY `idx_toxicity` (`toxicity_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中药材库';

-- 2. 方剂表
CREATE TABLE IF NOT EXISTS `tcm_formula` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '方剂名称',
    `source` VARCHAR(100) COMMENT '出处(伤寒论/金匮要略等)',
    `composition` TEXT NOT NULL COMMENT '组成(JSON数组: [{herb_id, name, dosage, unit}])',
    `preparation` VARCHAR(200) COMMENT '制法',
    `usage` VARCHAR(200) COMMENT '用法',
    `indication` TEXT COMMENT '主治',
    `pathology` VARCHAR(200) COMMENT '病机',
    `principle` VARCHAR(200) COMMENT '治则',
    `analysis` TEXT COMMENT '方解(君臣佐使)',
    `modifications` TEXT COMMENT '加减变化(JSON数组)',
    `contraindications` TEXT COMMENT '禁忌',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='经典方剂库';

-- 3. 配伍禁忌规则表
CREATE TABLE IF NOT EXISTS `tcm_incompatibility` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `rule_type` VARCHAR(20) NOT NULL COMMENT '规则类型(十八反/十九畏/妊娠禁忌)',
    `herb_a` VARCHAR(50) NOT NULL COMMENT '药材A',
    `herb_b` VARCHAR(50) NOT NULL COMMENT '药材B',
    `description` VARCHAR(200) COMMENT '禁忌说明',
    `severity` TINYINT DEFAULT 2 COMMENT '严重程度(1警告/2禁止)',
    `source` VARCHAR(100) COMMENT '出处',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配伍禁忌规则';

-- 4. 证型表
CREATE TABLE IF NOT EXISTS `tcm_syndrome` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '证型名称',
    `category` VARCHAR(50) COMMENT '分类(脏腑/气血/六淫等)',
    `symptoms` TEXT COMMENT '主要症状(JSON数组)',
    `tongue_signs` VARCHAR(100) COMMENT '舌象',
    `pulse_signs` VARCHAR(100) COMMENT '脉象',
    `principle` VARCHAR(100) COMMENT '治则',
    `recommended_formulas` TEXT COMMENT '推荐方剂(JSON数组)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中医证型';

-- 5. 用户表
CREATE TABLE IF NOT EXISTS `tcm_user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `openid` VARCHAR(100) COMMENT '微信OpenID',
    `phone` VARCHAR(20) COMMENT '手机号',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `constitution_type` VARCHAR(20) COMMENT '体质类型(平和质/气虚质/阳虚质等)',
    `health_profile` TEXT COMMENT '健康档案(JSON)',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_openid` (`openid`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 6. 问诊记录表
CREATE TABLE IF NOT EXISTS `tcm_consultation` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `chief_complaint` TEXT COMMENT '主诉',
    `symptoms` TEXT COMMENT '症状(JSON数组)',
    `tongue_image` VARCHAR(500) COMMENT '舌象图片URL',
    `tongue_analysis` TEXT COMMENT '舌象分析结果(JSON)',
    `pulse_type` VARCHAR(50) COMMENT '脉象类型',
    `ai_syndrome` VARCHAR(50) COMMENT 'AI辨证结果',
    `ai_confidence` DECIMAL(5,2) COMMENT 'AI置信度(0-100)',
    `ai_principle` VARCHAR(100) COMMENT 'AI治则',
    `ai_formula` VARCHAR(100) COMMENT 'AI推荐方剂',
    `ai_reasoning` TEXT COMMENT 'AI推理过程',
    `risk_warning` TEXT COMMENT '风险提示',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待审核/1已确认/2已驳回)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_user` (`user_id`),
    KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问诊记录';

-- 7. 处方表
CREATE TABLE IF NOT EXISTS `tcm_prescription` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `consultation_id` BIGINT COMMENT '关联问诊ID',
    `prescription_no` VARCHAR(50) NOT NULL COMMENT '处方编号',
    `type` TINYINT DEFAULT 1 COMMENT '类型(1AI开方/2手动抓配)',
    `composition` TEXT NOT NULL COMMENT '药材组成(JSON数组)',
    `total_doses` INT DEFAULT 7 COMMENT '总剂数',
    `usage` VARCHAR(200) COMMENT '用法',
    `precautions` TEXT COMMENT '注意事项',
    `ai_review` TEXT COMMENT 'AI审核结果(JSON)',
    `review_status` TINYINT DEFAULT 0 COMMENT '审核状态(0待审/1通过/2需修改)',
    `pharmacist_id` BIGINT COMMENT '审核药师ID',
    `pharmacist_review` TEXT COMMENT '药师审核意见',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1有效/0无效)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_prescription_no` (`prescription_no`),
    KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='处方表';

-- 8. 操作日志表
CREATE TABLE IF NOT EXISTS `tcm_operation_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT COMMENT '操作用户',
    `action` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `target_type` VARCHAR(50) COMMENT '目标类型',
    `target_id` BIGINT COMMENT '目标ID',
    `detail` TEXT COMMENT '操作详情',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_user` (`user_id`),
    KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志';

-- 初始化十八反数据
INSERT INTO `tcm_incompatibility` (`rule_type`, `herb_a`, `herb_b`, `description`, `severity`, `source`) VALUES
('十八反', '甘草', '甘遂', '甘草反甘遂', 2, '《本草经集注》'),
('十八反', '甘草', '大戟', '甘草反大戟', 2, '《本草经集注》'),
('十八反', '甘草', '芫花', '甘草反芫花', 2, '《本草经集注》'),
('十八反', '甘草', '海藻', '甘草反海藻', 2, '《本草经集注》'),
('十八反', '乌头', '贝母', '乌头反贝母', 2, '《本草经集注》'),
('十八反', '乌头', '瓜蒌', '乌头反瓜蒌', 2, '《本草经集注》'),
('十八反', '乌头', '半夏', '乌头反半夏', 2, '《本草经集注》'),
('十八反', '乌头', '白蔹', '乌头反白蔹', 2, '《本草经集注》'),
('十八反', '乌头', '白及', '乌头反白及', 2, '《本草经集注》'),
('十八反', '藜芦', '人参', '藜芦反人参', 2, '《本草经集注》'),
('十八反', '藜芦', '沙参', '藜芦反沙参', 2, '《本草经集注》'),
('十八反', '藜芦', '丹参', '藜芦反丹参', 2, '《本草经集注》'),
('十八反', '藜芦', '玄参', '藜芦反玄参', 2, '《本草经集注》'),
('十八反', '藜芦', '苦参', '藜芦反苦参', 2, '《本草经集注》'),
('十八反', '藜芦', '细辛', '藜芦反细辛', 2, '《本草经集注》'),
('十八反', '藜芦', '芍药', '藜芦反芍药', 2, '《本草经集注》');

-- 初始化十九畏数据
INSERT INTO `tcm_incompatibility` (`rule_type`, `herb_a`, `herb_b`, `description`, `severity`, `source`) VALUES
('十九畏', '硫黄', '朴硝', '硫黄畏朴硝', 2, '《宝庆本草折衷》'),
('十九畏', '水银', '砒霜', '水银畏砒霜', 2, '《宝庆本草折衷》'),
('十九畏', '狼毒', '密陀僧', '狼毒畏密陀僧', 2, '《宝庆本草折衷》'),
('十九畏', '巴豆', '牵牛', '巴豆畏牵牛', 2, '《宝庆本草折衷》'),
('十九畏', '丁香', '郁金', '丁香畏郁金', 2, '《宝庆本草折衷》'),
('十九畏', '川乌', '犀角', '川乌畏犀角', 2, '《宝庆本草折衷》'),
('十九畏', '草乌', '犀角', '草乌畏犀角', 2, '《宝庆本草折衷》'),
('十九畏', '牙硝', '三棱', '牙硝畏三棱', 2, '《宝庆本草折衷》'),
('十九畏', '官桂', '石脂', '官桂畏石脂', 2, '《宝庆本草折衷》'),
('十九畏', '人参', '五灵脂', '人参畏五灵脂', 2, '《宝庆本草折衷»');