## ADDED Requirements

### Requirement: 服务器镜像构建前置条件
系统 SHALL 提供服务器构建前端、后端和网关镜像所需的前置条件说明，覆盖 Docker、Docker Compose、源码目录、网络访问、端口和环境变量配置。说明 MUST 明确服务器部署不能提交真实敏感凭据，且 MUST 要求部署人员从示例配置复制并修改服务器专用配置。

#### Scenario: 准备服务器配置
- **WHEN** 部署人员准备在服务器上构建镜像
- **THEN** 系统 MUST 提供可复制的服务器部署配置模板，并说明镜像名、端口、数据库名称、数据库账号和数据库密码的用途

#### Scenario: 检查服务器依赖
- **WHEN** 部署人员按照文档执行服务器构建流程
- **THEN** 文档或脚本 MUST 指明 Docker 和 Docker Compose 是必需依赖，并在缺失时给出可理解的失败提示

### Requirement: 前后端与网关镜像构建
系统 SHALL 支持在服务器本机从已上传构建产物构建后端和前端镜像。后端构建产物 MUST 为 `deploy/artifacts/backend/app.jar`，前端构建产物 MUST 为 `deploy/artifacts/frontend/dist/`，并且构建出的镜像标签 MUST 与 Compose 启动时读取的环境变量保持一致。网关 Nginx MUST 使用现成官方 Nginx 镜像，不进行自定义镜像构建。

#### Scenario: 构建后端镜像
- **WHEN** 部署人员执行服务器镜像构建流程
- **THEN** 系统 MUST 使用 `deploy/artifacts/backend/app.jar` 构建后端镜像，并使用 `BACKEND_IMAGE` 指定的标签

#### Scenario: 构建前端镜像
- **WHEN** 部署人员执行服务器镜像构建流程
- **THEN** 系统 MUST 使用 `deploy/artifacts/frontend/dist/` 构建前端镜像，并使用 `FRONTEND_IMAGE` 指定的标签

#### Scenario: 使用官方网关镜像
- **WHEN** 部署人员执行服务器镜像构建流程
- **THEN** 系统 MUST 不构建网关 Nginx 镜像，并由 Compose 使用 `NGINX_IMAGE` 指定的现成 Nginx 镜像

### Requirement: 服务器 Compose 启动
系统 SHALL 支持使用服务器构建出的镜像启动完整服务栈。启动流程 MUST 使用 `deploy/docker-compose.yml` 和服务器 `.env`，并启动 MySQL、后端、前端静态服务和网关 Nginx。

#### Scenario: 启动服务器服务栈
- **WHEN** 部署人员完成镜像构建并执行 Compose 启动命令
- **THEN** 系统 MUST 使用服务器 `.env` 中的镜像标签启动对应容器

#### Scenario: 等待数据库健康
- **WHEN** Compose 启动后端服务
- **THEN** 后端服务 MUST 在 MySQL 健康检查通过后启动

#### Scenario: 更新服务器镜像
- **WHEN** 部署人员在服务器上重新构建镜像并再次执行 Compose 启动命令
- **THEN** Compose MUST 使用新的本地镜像重建并启动受影响服务

### Requirement: 服务器部署验证
系统 SHALL 提供服务器部署后的验证步骤，用于确认前端页面、后端 API、容器状态和网关代理可用。验证步骤 MUST 给出成功判断标准，并在失败时指向可检查的日志或配置项。

#### Scenario: 验证前端入口
- **WHEN** 部署人员执行服务器部署验证
- **THEN** 验证步骤 MUST 检查网关根路径 `/` 是否返回成功 HTTP 状态

#### Scenario: 验证后端接口
- **WHEN** 部署人员执行服务器部署验证
- **THEN** 验证步骤 MUST 检查 `/api/portfolio/projects` 是否返回成功 HTTP 状态和 JSON 响应

#### Scenario: 查看容器状态
- **WHEN** 部署验证失败
- **THEN** 文档 MUST 指导部署人员查看 Compose 服务状态以及 Nginx、后端和 MySQL 日志

### Requirement: 服务器构建故障排查
系统 SHALL 提供服务器镜像构建和启动失败的排查说明。排查说明 MUST 覆盖依赖下载失败、端口冲突、数据库连接失败、镜像标签不一致和 Nginx 代理失败。

#### Scenario: 构建依赖下载失败
- **WHEN** Maven 或 npm 依赖在服务器构建过程中下载失败
- **THEN** 文档 MUST 提示部署人员检查服务器网络、代理、镜像源或依赖仓库访问权限

#### Scenario: 镜像标签不一致
- **WHEN** Compose 启动时找不到前端、后端或网关镜像
- **THEN** 文档 MUST 提示部署人员核对构建命令中的镜像标签与 `deploy/.env` 中的镜像变量

#### Scenario: 端口或代理不可用
- **WHEN** 服务启动后无法通过网关访问页面或 API
- **THEN** 文档 MUST 提示部署人员检查端口占用、Compose 服务状态、Nginx upstream 配置和后端数据库连接日志
