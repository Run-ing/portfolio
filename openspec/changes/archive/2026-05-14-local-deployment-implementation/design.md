## Context

项目当前由 `portfolio-frontend`、`portfolio-backend`、MySQL 和 Nginx 组成，仓库中已经存在前后端 Dockerfile、`deploy/docker-compose.yml`、`deploy/nginx/default.conf` 和 MySQL 初始化脚本。README 已经提供基础启动命令，但本地部署仍缺少统一的配置模板、启动入口、验证流程和常见问题排查约定。

本设计将本地部署定义为“可在开发机或单机服务器上复现生产形态的部署方式”，优先复用现有 Docker Compose 编排，同时保留前后端以本机进程运行的说明，用于调试或缺少 Docker 的环境。

## Goals / Non-Goals

**Goals:**

- 提供一套明确的本地部署路径，覆盖依赖、配置、构建、启动、停止、验证和排障。
- 保持前端、后端、MySQL、Nginx 的职责清晰：前端由 Nginx 托管，`/api/` 转发到后端，`/apps/` 转发到当前服务器上的作品服务。
- 通过环境变量和示例文件管理可变配置，避免将密码、域名和机器相关配置硬编码到源码。
- 提供部署验证命令，确认首页、作品接口、后端健康状态和代理路径符合预期。

**Non-Goals:**

- 不引入 Kubernetes、云厂商托管服务或多节点高可用部署。
- 不改变现有作品管理 API、数据库表结构或前端业务展示行为。
- 不实现完整 CI/CD 流水线；本变更只定义本地部署可执行路径。
- 不把管理后台页面纳入部署能力范围。

## Decisions

1. 以 Docker Compose 作为推荐本地部署入口。
   - 理由：仓库已经包含 Compose、Dockerfile、Nginx 和 MySQL 初始化脚本，能最接近单机生产运行形态，并减少用户手工安装 Java、Node、MySQL、Nginx 的差异。
   - 替代方案：分别在宿主机运行 MySQL、Spring Boot、Vite/Nginx。该方式适合调试，但环境漂移更大，因此作为补充说明。

2. 将配置分为“示例模板”和“本地私有配置”。
   - 理由：`DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USERNAME`、`DB_PASSWORD`、`VITE_API_BASE_URL` 和作品代理目标都可能随部署机器变化，必须可覆盖且不应提交私密值。
   - 替代方案：继续在 `application.yml`、Compose 和 Nginx 示例中写死默认值。该方案上手快，但不利于迁移和排障。

3. Nginx 统一承担静态资源托管和反向代理。
   - 理由：前端生产构建产物需要静态服务，浏览器请求 `/api/` 和 `/apps/` 时保持同源更容易避免跨域问题，也符合现有 `deploy/nginx/default.conf` 的方向。
   - 替代方案：前端直接配置完整后端地址。该方式适合开发调试，但生产形态中会暴露更多环境差异。

4. 部署验证必须以 HTTP 结果为准。
   - 理由：容器启动成功不等于系统可用，必须检查首页、`/api/portfolio/projects`、后端端口和可选的 `/apps/` 代理路径。
   - 替代方案：只检查进程或容器状态。该方式无法发现 Nginx 代理、数据库连接和前端构建路径问题。

## Risks / Trade-offs

- [Risk] 本机 80、3306 或 9090 端口被占用 -> Mitigation: 文档和配置模板必须说明端口覆盖方式，并在验证失败时提示检查端口冲突。
- [Risk] Docker Desktop 或宿主机网络对 `host.docker.internal` 支持不一致 -> Mitigation: `/apps/` 代理目标必须可配置，并说明 Linux 环境下需要使用宿主机网关地址或显式 extra_hosts。
- [Risk] 数据库初始化只在首次创建数据卷时执行 -> Mitigation: 部署说明必须解释如何重建数据卷或手动导入初始化 SQL。
- [Risk] 前端构建时 API 基础路径配置错误 -> Mitigation: 推荐生产部署默认使用相对路径 `/api`，仅在绕过 Nginx 代理时设置 `VITE_API_BASE_URL`。
- [Risk] 本地部署脚本与已有手动命令重复 -> Mitigation: 脚本只封装常用流程，文档仍保留底层命令，便于定位问题。

## Migration Plan

1. 补充本地部署文档，说明 Docker Compose 推荐路径和本机进程备用路径。
2. 补充或调整配置示例，让数据库、端口、前端 API 基础路径和 `/apps/` 代理目标可被本地覆盖。
3. 增加部署验证入口，检查前端首页、作品 API、后端服务和代理配置。
4. 在现有环境中执行推荐部署命令并记录验证结果。
5. 如部署失败，可停止服务并回退到当前 README 中的手动开发启动方式。

## Open Questions

- `/apps/` 代理的默认宿主机端口是否固定为 `9000`，还是需要支持多个本地作品服务映射。
- 是否需要为 Windows PowerShell、Linux shell 分别提供启动脚本，还是先以跨平台 Docker Compose 命令为准。
