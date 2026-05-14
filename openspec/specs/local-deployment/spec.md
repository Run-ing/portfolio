## Purpose

Defines local deployment configuration, Docker Compose startup, Nginx proxying, and local process deployment guidance.

## Requirements

### Requirement: 本地部署配置

系统 SHALL 提供本地部署所需的配置模板和说明，覆盖数据库连接、后端端口、前端 API 基础路径、Nginx 监听端口和作品代理目标。配置模板 MUST 不包含真实敏感凭据，并且 MUST 说明每个必填配置的用途和默认值。

#### Scenario: 复制本地配置模板

- **WHEN** 部署人员按文档准备本地部署配置
- **THEN** 系统 MUST 提供可复制的示例配置，并标明哪些值需要按本机环境修改

#### Scenario: 使用默认同源 API 路径

- **WHEN** 前端以本地部署模式构建且未显式配置 `VITE_API_BASE_URL`
- **THEN** 前端 MUST 使用相对路径 `/api` 请求后端接口

#### Scenario: 覆盖数据库连接

- **WHEN** 部署人员配置 `DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USERNAME` 和 `DB_PASSWORD`
- **THEN** 后端 MUST 使用这些配置连接数据库

### Requirement: Docker Compose 本地部署

系统 SHALL 提供 Docker Compose 本地部署方式，用于启动 MySQL、后端服务和前端 Nginx 服务。Compose 部署 MUST 确保后端在数据库健康后启动，前端服务 MUST 能通过 Nginx 访问后端 API。

#### Scenario: 启动 Compose 部署

- **WHEN** 部署人员执行本地部署启动命令
- **THEN** 系统 MUST 构建并启动 MySQL、后端和前端服务

#### Scenario: 等待数据库健康

- **WHEN** Compose 编排启动后端服务
- **THEN** 后端服务 MUST 在 MySQL 健康检查通过后再启动

#### Scenario: 停止 Compose 部署

- **WHEN** 部署人员执行本地部署停止命令
- **THEN** 系统 MUST 停止前端、后端和数据库服务，并保留默认数据卷中的数据库数据

### Requirement: Nginx 反向代理

系统 SHALL 在本地部署中使用 Nginx 托管前端构建产物，并代理 `/api/` 和 `/apps/` 请求。`/api/` 请求 MUST 转发到后端服务，`/apps/` 请求 MUST 转发到配置的本地作品服务入口。

#### Scenario: 访问前端页面

- **WHEN** 用户访问本地部署的根路径 `/`
- **THEN** Nginx MUST 返回前端应用入口页面

#### Scenario: 代理后端 API

- **WHEN** 用户访问 `/api/portfolio/projects`
- **THEN** Nginx MUST 将请求代理到后端服务并返回作品列表响应

#### Scenario: 代理本地作品服务

- **WHEN** 用户访问 `/apps/` 下的路径
- **THEN** Nginx MUST 将请求代理到配置的本地作品服务入口

### Requirement: 部署验证

系统 SHALL 提供本地部署验证步骤，用于确认部署后的前端、后端、数据库和代理路径可用。验证步骤 MUST 输出明确的成功或失败结果，并在失败时指向可检查的配置项。

#### Scenario: 验证前端首页

- **WHEN** 部署人员执行部署验证
- **THEN** 验证 MUST 检查本地部署首页是否返回成功 HTTP 状态

#### Scenario: 验证作品接口

- **WHEN** 部署人员执行部署验证
- **THEN** 验证 MUST 检查 `/api/portfolio/projects` 是否返回成功 HTTP 状态和 JSON 响应

#### Scenario: 验证失败提示

- **WHEN** 部署验证发现服务不可用
- **THEN** 验证 MUST 提示部署人员检查端口占用、数据库连接、容器状态或 Nginx 代理配置

### Requirement: 本机进程部署说明

系统 SHALL 提供不依赖 Docker Compose 的本机进程部署说明，用于在本机分别运行数据库、后端和前端静态服务。该说明 MUST 标明与推荐 Docker Compose 部署的差异和适用场景。

#### Scenario: 使用本机后端进程

- **WHEN** 部署人员选择本机进程方式部署后端
- **THEN** 文档 MUST 说明如何设置数据库环境变量并启动 Spring Boot 服务

#### Scenario: 使用本机前端构建产物

- **WHEN** 部署人员选择本机进程方式部署前端
- **THEN** 文档 MUST 说明如何构建前端产物并通过静态服务或 Nginx 托管
