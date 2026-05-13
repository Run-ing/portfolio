## Purpose

定义个人在线作品集第一版的作品数据、查询接口、管理接口、前台展示和部署代理行为。该能力确保作品信息可以由数据库持久化维护，由后端统一输出给前台，并支持当前服务器项目和外部项目两种访问方式。

## Requirements

### Requirement: 作品数据存储

系统 SHALL 使用数据库存储在线作品信息，字段 MUST 覆盖名称、简介、封面图、技术栈、标签、部署类型、访问地址、源码地址、状态、推荐标记、排序值、创建时间和更新时间。

#### Scenario: 创建作品表

- **WHEN** 初始化数据库
- **THEN** 系统 MUST 创建 `portfolio_project` 表并支持保存作品信息

#### Scenario: 保存部署类型

- **WHEN** 作品被保存
- **THEN** 系统 MUST 支持 `LOCAL` 和 `EXTERNAL` 两种部署类型

#### Scenario: 保存作品状态

- **WHEN** 作品被保存
- **THEN** 系统 MUST 支持 `ONLINE`、`DEVELOPING` 和 `OFFLINE` 三种状态

### Requirement: 前台作品列表查询

系统 SHALL 提供前台作品列表查询接口，返回可展示作品的基础信息，并按推荐、排序值和创建时间排序。前端 MUST 支持通过默认代理路径或配置的 API 基址请求该接口。

#### Scenario: 查询作品列表

- **WHEN** 客户端请求 `GET /api/portfolio/projects`
- **THEN** 系统 MUST 返回统一响应结构和作品数组

#### Scenario: 使用配置的后端地址查询作品列表

- **WHEN** 前端配置了 `VITE_API_BASE_URL`
- **THEN** 前端 MUST 使用该地址作为后端 API 基址请求作品列表

#### Scenario: 过滤下线作品

- **WHEN** 作品状态为 `OFFLINE`
- **THEN** 系统 MUST 不在前台作品列表中返回该作品

### Requirement: 前台作品详情查询

系统 SHALL 提供作品详情查询接口，客户端可以根据作品 ID 获取完整作品信息。前端 MUST 支持通过默认代理路径或配置的 API 基址请求该接口。

#### Scenario: 查询存在的作品详情

- **WHEN** 客户端请求 `GET /api/portfolio/projects/{id}` 且作品存在
- **THEN** 系统 MUST 返回该作品详情

#### Scenario: 使用配置的后端地址查询作品详情

- **WHEN** 前端配置了 `VITE_API_BASE_URL`
- **THEN** 前端 MUST 使用该地址作为后端 API 基址请求作品详情

#### Scenario: 查询不存在的作品详情

- **WHEN** 客户端请求 `GET /api/portfolio/projects/{id}` 且作品不存在
- **THEN** 系统 MUST 返回 404 风格的错误响应

### Requirement: 管理端作品维护接口

系统 SHALL 提供管理端作品新增、修改和删除接口，第一版不建设管理页面。

#### Scenario: 新增作品

- **WHEN** 管理端请求 `POST /api/admin/portfolio/projects` 并提交有效作品信息
- **THEN** 系统 MUST 创建作品并返回创建后的数据

#### Scenario: 修改作品

- **WHEN** 管理端请求 `PUT /api/admin/portfolio/projects/{id}` 并提交有效作品信息
- **THEN** 系统 MUST 更新对应作品并返回更新后的数据

#### Scenario: 删除作品

- **WHEN** 管理端请求 `DELETE /api/admin/portfolio/projects/{id}` 且作品存在
- **THEN** 系统 MUST 删除该作品并返回成功响应

### Requirement: 前台作品展示和访问

系统 SHALL 在前台展示作品列表和作品详情，并在用户访问作品时统一使用 `projectUrl`。

#### Scenario: 展示作品卡片

- **WHEN** 前台加载到作品列表
- **THEN** 前台 MUST 展示作品名称、简介、封面图、技术栈、状态和标签

#### Scenario: 访问作品

- **WHEN** 用户点击访问作品入口
- **THEN** 前台 MUST 使用该作品的 `projectUrl` 打开访问地址

#### Scenario: 展示部署类型

- **WHEN** 用户查看作品详情
- **THEN** 前台 MUST 显示该作品属于当前服务器部署或外部地址

### Requirement: 部署代理支持

系统 SHALL 提供部署配置，使前端、后端 API 和当前服务器项目代理可以通过 Nginx 协同工作。

#### Scenario: 代理后端 API

- **WHEN** 浏览器请求 `/api/`
- **THEN** Nginx MUST 将请求代理到后端服务

#### Scenario: 代理当前服务器作品

- **WHEN** 浏览器请求 `/apps/`
- **THEN** Nginx MUST 按配置代理到当前服务器上的作品服务
