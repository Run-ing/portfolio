## Context

仓库当前已经包含源码构建镜像的入口：`portfolio-backend/Dockerfile`、`portfolio-frontend/Dockerfile` 和 `deploy/nginx/Dockerfile`。`deploy/docker-compose.yml` 已经通过 `BACKEND_IMAGE`、`FRONTEND_IMAGE` 和 `NGINX_IMAGE` 支持以镜像方式启动服务，但目标服务器不应保存业务源码，因此服务器侧需要统一的“接收后端 JAR 和前端 `dist/`、从构建产物生成镜像、配置环境变量、启动 Compose、验证服务”的流程。

目标使用者是在服务器上部署项目的人，通常只有服务器 shell、Docker、Docker Compose 和上传后的构建产物。该流程需要兼容没有镜像仓库的场景，因此默认支持直接在服务器本机用 JAR 和静态文件构建镜像，同时保留后续接入私有仓库或 CI 构建的空间。

## Goals / Non-Goals

**Goals:**

- 提供服务器本机可执行的产物型前端、后端和 Nginx 镜像构建流程。
- 复用现有 Dockerfile 和 `deploy/docker-compose.yml`，避免引入新的编排系统。
- 明确服务器 `.env` 配置、镜像标签、构建产物目录、端口、数据库账号和验证命令。
- 支持重新构建和滚动更新当前 Compose 服务。
- 提供失败时的排查入口，包括构建失败、端口冲突、数据库连接失败和 Nginx 代理失败。

**Non-Goals:**

- 不建设完整 CI/CD 平台。
- 不引入 Kubernetes、Harbor、GitHub Actions 或其他外部基础设施作为必需依赖。
- 不改变业务 API、数据库 schema 或前端页面功能。
- 不把数据库镜像改为自定义构建；MySQL 继续使用官方镜像。

## Decisions

1. 服务器构建优先使用构建产物专用 Dockerfile。

   后端镜像在服务器上只需要复制 `deploy/artifacts/backend/app.jar`，前端镜像只需要复制 `deploy/artifacts/frontend/dist/` 并用 Nginx 托管，网关 Nginx Dockerfile 继续使用现有入口。备选方案是在服务器使用源码 Dockerfile 执行 Maven 和 npm 构建，但这会要求服务器保存源码并访问依赖源，不符合部署约束。

2. 镜像标签通过 `deploy/.env` 统一传入 Compose。

   Compose 已经支持 `BACKEND_IMAGE`、`FRONTEND_IMAGE` 和 `NGINX_IMAGE`，服务器构建流程只需要保证构建出的标签与 `.env` 一致。备选方案是把镜像名硬编码进 Compose，但这会降低不同服务器、不同版本标签和私有仓库场景的可配置性。

3. 提供脚本文档化入口，而不是只散落命令。

   对服务器部署而言，连续执行三条 `docker build`、一次 `docker compose up -d` 和多条验证命令容易遗漏。实现时新增 `deploy/build-images.sh`，检查 JAR、前端 `dist/index.html`、Docker 和 Compose 是否存在，并在文档中说明等价手工命令。备选方案是只修改 README，但可重复性和错误提示较弱。

4. 服务器启动仍使用现有 Compose。

   Compose 已经描述 MySQL、后端、前端静态服务和网关 Nginx 的服务关系。服务器构建镜像后用同一个 Compose 文件启动，可以让本地部署和服务器部署共享配置模型。备选方案是新增一个 `docker-compose.server.yml`，但当前需求没有足够差异需要维护第二份编排文件。

## Risks / Trade-offs

- 上传了错误版本的 JAR 或 `dist/` → 文档明确产物来源和目标路径，脚本检查 `app.jar` 与 `dist/index.html` 是否存在。
- 服务器误用源码型 Dockerfile → 服务器脚本固定使用 `deploy/images/backend/Dockerfile` 和 `deploy/images/frontend/Dockerfile`，不依赖 `portfolio-backend/` 或 `portfolio-frontend/`。
- 服务器端口已被占用 → 验证和故障排查中明确检查 `NGINX_HTTP_PORT`、`BACKEND_PORT`、`FRONTEND_PORT` 和 `MYSQL_PORT`。
- 数据库凭据使用示例默认值存在安全风险 → 文档必须要求服务器 `.env` 修改默认密码，不把真实敏感信息提交到仓库。
- 服务器本机构建与 CI 构建可能产生镜像标签差异 → 镜像命名统一由 `.env` 控制，后续可自然迁移到 `registry/project/service:tag` 格式。

## Migration Plan

1. 在本机或 CI 构建后端 JAR 和前端 `dist/`。
2. 在服务器安装 Docker 和 Docker Compose，并上传 `deploy/` 必要文件、`app.jar` 和前端 `dist/`。
3. 复制 `deploy/.env.example` 为 `deploy/.env`，修改镜像名、端口、数据库密码和域名。
4. 在服务器执行镜像构建脚本或等价 `docker build` 命令。
5. 执行 `docker compose --env-file deploy/.env -f deploy/docker-compose.yml up -d` 启动服务。
6. 使用 `docker compose ps`、`curl /` 和 `curl /api/portfolio/projects` 验证服务。
7. 如需回滚，恢复上一版 JAR 和 `dist/` 或上一版镜像标签，再执行 `docker compose up -d`。

## Open Questions

- 服务器是否有私有镜像仓库。如果有，脚本应额外支持 `docker push`；如果没有，本机构建即可满足当前需求。
- 生产环境域名和 HTTPS 证书是否已经准备。如果需要 HTTPS，应作为后续独立变更处理。
