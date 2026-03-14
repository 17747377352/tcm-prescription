# TCM Prescription - 中药智能抓配平台

## 项目简介

基于 Spring Boot + Vue 的中药智能抓配平台，集成 AI 辨证开方、配伍审核、禁忌检查等功能。

## 技术栈

**后端:**
- Java 17 + Spring Boot 3.2
- MySQL 8.0 + Druid
- Redis
- Kimi K2.5 (AI 模型)

**前端:**
- Vue 3 + TypeScript
- uni-app (小程序)

## 项目结构

```
tcm-prescription/
├── sql/                    # 数据库脚本
│   └── schema.sql         # 表结构
├── src/main/java/
│   └── com/tcm/prescription/
│       ├── controller/    # 控制器
│       ├── service/       # 业务服务
│       ├── repository/    # 数据访问
│       └── entity/        # 实体类
└── src/main/resources/
    └── application.yml    # 配置文件
```

## API 接口

### 药材管理
- `GET /api/herbs/search?keyword=` - 搜索药材
- `GET /api/herbs/{id}` - 获取药材详情
- `POST /api/herbs/init` - 初始化药材数据
- `GET /api/herbs/by-nature/{nature}` - 按四气分类

### 配方审核
- `POST /api/prescription/check-compatibility` - 配伍禁忌检查

## 快速开始

```bash
# 1. 初始化数据库
mysql -h HOST -u USER -p < sql/schema.sql

# 2. 启动项目
mvn spring-boot:run

# 3. 初始化中药材数据
curl -X POST http://localhost:8080/api/herbs/init
```

## 核心功能

1. **智能问诊开方** - AI 辨证论治，推荐方剂
2. **手动抓配** - 药材库检索，配伍禁忌实时校验
3. **配伍审核** - 十八反、十九畏、毒性药材检查
4. **体质辨识** - 九种体质问卷

## 免责声明

本平台仅供中医药知识参考学习，不构成医疗诊断建议。
如有疾病请前往正规医疗机构就诊。