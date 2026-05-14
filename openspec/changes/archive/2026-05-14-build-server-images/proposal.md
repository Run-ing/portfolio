## Why

当前项目已经具备前端、后端和 Nginx 的 Dockerfile 与 Compose 编排，但“在服务器上构建前后端镜像并启动服务”的操作路径还不够明确。需要补齐服务器侧构建、配置、启动和验证流程，降低部署时对本地构建或人工猜测命令的依赖。

## What Changes

- 增加服务器基于已上传构建产物构建前端、后端和网关 Nginx 镜像的说明与脚本入口。
- 明确服务器所需的 Docker、Docker Compose、环境变量和端口前置条件。
- 增加镜像标签、构建产物目录、启动命令和更新流程，保证服务器不保存业务源码也可以构建镜像并运行。
- 增加部署后的健康验证与常见故障排查步骤。
- 不改变现有业务 API、数据库结构或前端展示行为。

## Capabilities

### New Capabilities
- `server-image-build`: 定义在服务器上使用后端 JAR 和前端 `dist/` 构建前端、后端和网关镜像，并通过 Compose 启动和验证服务的能力。

### Modified Capabilities
无。

## Impact

- 影响 `deploy/` 下的 Compose、环境变量示例、Nginx 镜像构建说明和部署文档。
- 影响 `portfolio-backend/Dockerfile` 与 `portfolio-frontend/Dockerfile` 的服务器构建可用性验证。
- 可能新增部署脚本或 README 部署章节。
- 不引入新的运行时服务；继续使用 MySQL、后端 Spring Boot、前端 Nginx 静态服务和网关 Nginx。
