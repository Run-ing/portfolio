## Why

当前工作区只有需求分析文档，还没有可运行的个人作品集网站。需要先交付一个可上线的第一版，让在线作品信息可以被数据库维护、后端查询，并在前台清晰展示与跳转访问。

## What Changes

- 新增个人在线作品集前台，展示作品列表、推荐数量、部署类型、状态、技术栈、标签、详情与访问入口。
- 新增 Spring Boot 3 后端，提供前台作品列表与详情查询接口。
- 新增管理端 REST 接口，通过 API 完成作品新增、修改、删除。
- 新增 MySQL 表结构、初始化样例数据、Docker Compose 与 Nginx 配置。
- 暂不实现用户注册、评论、点赞、博客、复杂权限、文件管理系统。

## Capabilities

### New Capabilities

- `portfolio-projects`: 管理和展示个人在线作品，覆盖作品数据结构、前台查询、详情查询、管理 CRUD、部署地址类型和状态枚举。

### Modified Capabilities

- 无。

## Impact

- 新增 `portfolio-backend/` Spring Boot 后端工程。
- 新增 `portfolio-frontend/` Vue 3 前台工程。
- 新增 `deploy/` 下 MySQL 初始化脚本、Nginx 配置和 Docker Compose 编排。
- 新增接口：
  - `GET /api/portfolio/projects`
  - `GET /api/portfolio/projects/{id}`
  - `POST /api/admin/portfolio/projects`
  - `PUT /api/admin/portfolio/projects/{id}`
  - `DELETE /api/admin/portfolio/projects/{id}`

