## MODIFIED Requirements

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

