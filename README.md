# lzf-studio

个人在线作品集网站第一版。

## 目录

- `portfolio-backend/`：Spring Boot 3 后端，提供前台查询接口和管理端 CRUD 接口。
- `portfolio-frontend/`：Vue 3 + Vite 前台，展示作品列表和详情。
- `deploy/`：MySQL 初始化脚本、Nginx 示例配置、Docker Compose 编排。
- `openspec/changes/build-portfolio-site-v1/`：本次变更说明。

## 本地开发

### 后端

```bash
cd portfolio-backend
mvn spring-boot:run
```

默认连接本机 MySQL：

```text
jdbc:mysql://localhost:3306/lzf_studio
```

可通过环境变量覆盖：

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

### 前端

```bash
cd portfolio-frontend
npm install
npm run dev
```

前端开发服务器会把 `/api` 代理到 `http://localhost:9090`。

如果暂时没有启动后端和 MySQL，可以另开一个终端运行前端 mock API：

```bash
cd portfolio-frontend
npm run mock
```

### Docker Compose

```bash
docker compose -f deploy/docker-compose.yml up --build
```

启动后访问：

- 前台：http://localhost
- 后端接口：http://localhost/api/portfolio/projects
