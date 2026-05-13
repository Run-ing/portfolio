## Context

当前仓库尚未包含可运行的应用代码，需求分析要求第一版只围绕“在线作品管理与展示”交付闭环。系统需要同时包含前台展示、后端接口、数据库存储和部署示例，但不引入用户体系、评论点赞、博客或复杂权限。

## Goals / Non-Goals

**Goals:**

- 使用 Vue 3 + Vite + TypeScript 构建前台作品展示界面。
- 使用 Spring Boot 3 + MyBatis-Plus 构建 REST API。
- 使用 MySQL 存储作品信息，并提供初始化表结构和样例数据。
- 管理端第一版仅提供接口，不建设后台页面。
- Nginx 同时支持前端 SPA、后端 `/api` 代理和 `/apps` 本机项目代理示例。

**Non-Goals:**

- 不实现登录、注册、鉴权、角色权限。
- 不实现文件上传或封面管理系统。
- 不实现评论、点赞、博客等社交或内容模块。
- 不实现复杂搜索、分页、统计分析。

## Decisions

1. 前后端分离，使用 `portfolio-frontend/` 和 `portfolio-backend/` 两个工程目录。
   - 原因：便于独立开发、构建和部署，符合需求里的推荐技术栈。
   - 替代方案：单体后端直接渲染页面。该方案更简单，但不利于后续扩展前台交互和独立发布。

2. 数据库中技术栈和标签第一版继续使用逗号分隔字符串，接口层转换为数组。
   - 原因：需求文档已经定义字段为 `VARCHAR(500)`，第一版无需引入关联表。
   - 替代方案：拆分 `project_tech_stack`、`project_tag` 表。该方案规范性更强，但第一版成本更高。

3. 对外 API 使用统一响应结构 `{ code, message, data }`。
   - 原因：与需求文档示例一致，前端处理简单。
   - 替代方案：直接返回资源对象。该方案更贴近 REST，但会偏离已有需求。

4. 前台点击作品时只依赖 `projectUrl`。
   - 原因：无论 `LOCAL` 还是 `EXTERNAL`，前端不需要理解部署细节，只负责跳转访问。
   - 替代方案：前端按部署类型拼接地址。该方案耦合部署规则，后续维护成本更高。

## Risks / Trade-offs

- 管理接口暂不鉴权，不能直接暴露在公网生产环境 → 第一版用于本地或受控网络，生产前需要增加认证和授权。
- 技术栈和标签用字符串存储，不适合复杂筛选和统计 → 后续若需要标签管理，可迁移为关联表。
- Docker Compose 中 `/apps` 代理只是示例 → 实际部署时需要根据当前服务器上的应用端口和路径调整 Nginx。
- 前端依赖后端接口可用 → 本地开发通过 Vite 代理 `/api`，部署环境通过 Nginx 代理 `/api`。

## Migration Plan

1. 初始化 MySQL，执行 `deploy/mysql/init.sql`。
2. 启动后端服务，确认 `/api/portfolio/projects` 返回样例数据。
3. 构建并部署前端，确认列表、详情和访问跳转可用。
4. 如使用 Docker Compose，运行 `docker compose -f deploy/docker-compose.yml up --build`。
5. 回滚时停止服务并保留数据库数据；如需清空第一版数据，删除 `portfolio_project` 表或 Docker volume。

## Open Questions

- 管理接口是否需要在下一版接入登录鉴权。
- 封面图片是否继续使用 URL 字段，还是引入上传和对象存储。

