## Context

第一版前端已经通过 `/api/portfolio/projects` 请求作品列表和详情，并由 Vite/Nginx 将 `/api` 代理到后端。为了减少本地调试时的误判，本次变更增加可选的 API 基址配置。

## Goals / Non-Goals

**Goals:**

- 保持默认 `/api` 相对路径，继续支持现有代理模式。
- 支持通过 `VITE_API_BASE_URL` 直接指定后端地址，例如 `http://localhost:9090`。
- 不改变后端接口结构和前端展示逻辑。

**Non-Goals:**

- 不新增作品接口。
- 不改动后端业务逻辑。
- 不引入前端状态管理库。

## Decisions

- 使用 Vite 环境变量 `VITE_API_BASE_URL`，因为只有 `VITE_` 前缀变量会暴露给浏览器端代码。
- 请求路径仍由 `api.ts` 集中构建，避免组件里散落后端地址。

## Risks / Trade-offs

- 如果直接配置跨域后端地址，需要后端 CORS 允许对应来源；当前后端已允许 `/api/**` 跨域访问。

