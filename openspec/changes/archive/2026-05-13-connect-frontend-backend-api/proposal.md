## Why

前端虽然已经有相对路径接口调用，但本地运行时容易受到 Vite 旧代理进程或部署代理配置影响，导致看起来没有从后端获取数据。需要把后端 API 对接方式显式化，并保留默认代理方案。

## What Changes

- 前端 API 请求支持 `VITE_API_BASE_URL` 配置。
- 默认仍使用 `/api` 相对路径，兼容 Vite 开发代理和 Nginx 部署代理。
- 补充前端 `.env.example` 和 README 说明，明确如何直接指向后端 `9090`。

## Capabilities

### New Capabilities

- 无。

### Modified Capabilities

- `portfolio-projects`: 前台作品列表和详情查询支持可配置后端 API 基址。

## Impact

- 修改 `portfolio-frontend/src/api.ts` 的请求 URL 构建方式。
- 新增 `portfolio-frontend/.env.example`。
- 更新 README 中的前后端接口对接说明。

