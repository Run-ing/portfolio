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

默认情况下前端使用相对路径 `/api` 请求后端。如果需要绕过 Vite/Nginx 代理，也可以在 `portfolio-frontend/.env` 中指定后端地址：

```bash
VITE_API_BASE_URL=http://localhost:9090
```

如果暂时没有启动后端和 MySQL，可以另开一个终端运行前端 mock API：

```bash
cd portfolio-frontend
npm run mock
```

## 本地部署

推荐使用 Docker Compose 部署本项目。Compose 会启动 MySQL、后端服务、前端服务和单独的 Docker 化 Nginx 代理网关。部署服务器不需要保存业务源码；后端、前端和代理 Nginx 都从已构建镜像启动。前端服务自己暴露 HTTP 端口，代理 Nginx 不挂载前端文件，只把 `/` 转发到前端服务，把 `/api/` 转发到后端，把 `/apps/chat/`、`/apps/admin/`、`/apps/ai/` 转发到宿主机上的本地项目端口。

### 配置

复制部署配置示例：

```bash
cp deploy/env.example deploy/env
```

常用配置项：

- `NGINX_HTTP_PORT`：Nginx 暴露到宿主机的端口，默认 `80`
- `NGINX_IMAGE`：代理 Nginx 镜像，默认使用官方 `nginx:1.27-alpine`
- `NGINX_SERVER_NAME`：Nginx `server_name`，本地可保持 `_`
- `MYSQL_PORT`：MySQL 暴露到宿主机的端口，默认 `13306`
- `MYSQL_ROOT_PASSWORD`：MySQL root 密码，默认示例值为 `root`
- `DB_NAME`：数据库名，默认 `lzf_studio`
- `DB_USERNAME` / `DB_PASSWORD`：后端连接数据库使用的账号和密码
- `BACKEND_IMAGE`：后端服务镜像，默认 `lzf-studio-backend:latest`
- `BACKEND_PORT`：后端暴露到宿主机的端口，默认 `9090`
- `FRONTEND_IMAGE`：前端服务镜像，默认 `lzf-studio-frontend:latest`
- `FRONTEND_PORT`：前端服务暴露到宿主机的端口，默认 `8080`
- `APP_CHAT_UPSTREAM`：chat 项目地址，默认 `http://host.docker.internal:3001`
- `APP_ADMIN_UPSTREAM`：admin 项目地址，默认 `http://host.docker.internal:8082`
- `APP_AI_UPSTREAM`：ai 项目地址，默认 `http://host.docker.internal:5173`

因为 Nginx 运行在 Docker 容器中，访问宿主机本地端口时不能使用 `127.0.0.1`，应使用 `host.docker.internal`。Compose 已配置 `host.docker.internal:host-gateway`，用于 Linux Docker 环境兼容。

### Docker Compose

```bash
docker compose --env-file deploy/env -f deploy/docker-compose.yml pull
docker compose --env-file deploy/env -f deploy/docker-compose.yml up -d
```

启动后访问：

- 前台：http://localhost
- 后端接口：http://localhost/api/portfolio/projects
- chat 项目：http://localhost/apps/chat/
- admin 项目：http://localhost/apps/admin/
- ai 项目：http://localhost/apps/ai/

停止服务并保留数据库数据卷：

```bash
docker compose --env-file deploy/env -f deploy/docker-compose.yml down
```

停止服务并删除数据库数据卷，下一次启动会重新执行 `deploy/mysql/init.sql`：

```bash
docker compose --env-file deploy/env -f deploy/docker-compose.yml down -v
```

### 部署验证

启动后可以执行：

```bash
curl -I http://localhost/
curl -i http://localhost/api/portfolio/projects
curl -I http://localhost/apps/chat/
curl -I http://localhost/apps/admin/
curl -I http://localhost/apps/ai/
```

首页应返回成功 HTTP 状态；`/api/portfolio/projects` 应返回 JSON 响应。如果验证失败，优先检查：

- `docker compose --env-file deploy/env -f deploy/docker-compose.yml ps` 查看容器状态
- `docker compose --env-file deploy/env -f deploy/docker-compose.yml logs nginx` 查看 Nginx 配置和代理错误
- `docker compose --env-file deploy/env -f deploy/docker-compose.yml logs portfolio-backend` 查看数据库连接错误
- `NGINX_HTTP_PORT`、`MYSQL_PORT`、`BACKEND_PORT` 是否与本机已有服务冲突
- chat/admin/ai 是否已经在宿主机对应端口启动

### 本机进程部署

如果不使用 Docker Compose，也可以分别启动服务：

1. 启动 MySQL，并导入 `deploy/mysql/init.sql`
2. 设置后端环境变量：

```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=lzf_studio
DB_USERNAME=root
DB_PASSWORD=root
```

3. 启动后端：

```bash
cd portfolio-backend
mvn spring-boot:run
```

4. 构建前端：

```bash
cd portfolio-frontend
npm install
npm run build
```

5. 使用本机 Nginx 托管 `portfolio-frontend/dist`，并按 `deploy/nginx/default.conf` 的结构配置 `/api/` 和 `/apps/.../` 代理。本机 Nginx 直接访问宿主机项目端口时可以使用 `127.0.0.1`。

### 服务器使用构建产物构建镜像并部署

如果不希望服务器保存业务源码，可以先在本机或 CI 构建后端 JAR 和前端静态文件，再只把部署目录、JAR 包和前端 `dist/` 上传到服务器。服务器只负责把后端和前端构建产物打成 Docker 镜像；代理 Nginx 使用官方镜像。

服务器前置条件：

- 已安装 Docker，并且当前用户可以执行 `docker`
- 已安装 Docker Compose v2，可以执行 `docker compose version`
- 服务器端口未被占用，重点检查 `NGINX_HTTP_PORT`、`MYSQL_PORT`、`BACKEND_PORT` 和 `FRONTEND_PORT`

在本机或 CI 生成构建产物：

```bash
cd portfolio-backend
mvn -DskipTests package

cd ../portfolio-frontend
npm ci
npm run build
```

上传到服务器时，服务器至少需要这些文件和目录：

```text
deploy/
  env.example
  build-images.sh
  docker-compose.yml
  mysql/init.sql
  nginx/templates/default.conf.template
  images/backend/Dockerfile
  images/frontend/Dockerfile
  images/frontend/nginx.conf
  artifacts/backend/app.jar
  artifacts/frontend/dist/
```

其中：

- `deploy/artifacts/backend/app.jar` 来自本机的 `portfolio-backend/target/portfolio-backend-0.0.1-SNAPSHOT.jar`，上传后重命名为 `app.jar`
- `deploy/artifacts/frontend/dist/` 来自本机的 `portfolio-frontend/dist/` 整个目录，必须包含 `index.html`、`assets/` 和 `covers/`

服务器目录准备示例：

```bash
mkdir -p deploy/artifacts/backend deploy/artifacts/frontend
cp portfolio-backend/target/portfolio-backend-0.0.1-SNAPSHOT.jar deploy/artifacts/backend/app.jar
cp -r portfolio-frontend/dist deploy/artifacts/frontend/dist
```

准备服务器配置：

```bash
cp deploy/env.example deploy/env
```

服务器上的 `deploy/env` 必须至少检查这些值：

- `BACKEND_IMAGE`、`FRONTEND_IMAGE`：构建脚本会按这些标签生成镜像，Compose 也会按这些标签启动容器
- `NGINX_IMAGE`：代理 Nginx 使用的官方镜像，默认 `nginx:1.27-alpine`
- `NGINX_HTTP_PORT`：对外访问端口，生产服务器通常是 `80`
- `NGINX_SERVER_NAME`：服务器域名；没有域名时可保持 `_`
- `MYSQL_ROOT_PASSWORD`、`DB_USERNAME`、`DB_PASSWORD`：服务器部署必须修改默认示例密码
- `APP_CHAT_UPSTREAM`、`APP_ADMIN_UPSTREAM`、`APP_AI_UPSTREAM`：网关代理到宿主机项目时使用 `host.docker.internal`

构建镜像：

```bash
sh deploy/build-images.sh
```

该脚本会读取 `deploy/env`，并依次构建：

- `BACKEND_IMAGE`：使用 `deploy/artifacts/backend/app.jar`
- `FRONTEND_IMAGE`：使用 `deploy/artifacts/frontend/dist/`

Nginx 网关不需要自定义构建，Compose 会直接拉取并运行 `NGINX_IMAGE` 指定的官方镜像。

不使用脚本时，可以手工执行等价命令。镜像标签必须和 `deploy/env` 保持一致：

```bash
docker build -t lzf-studio-backend:latest -f deploy/images/backend/Dockerfile deploy
docker build -t lzf-studio-frontend:latest -f deploy/images/frontend/Dockerfile deploy
```

启动服务：

```bash
docker compose --env-file deploy/env -f deploy/docker-compose.yml up -d
```

重新发布新版本时，重新上传新的 `app.jar` 和 `dist/` 后再次执行：

```bash
sh deploy/build-images.sh
docker compose --env-file deploy/env -f deploy/docker-compose.yml up -d --force-recreate
```

如果需要回滚，切回上一版代码或恢复上一版镜像标签，重新构建后再执行 Compose 启动命令。

部署后验证：

```bash
docker compose --env-file deploy/env -f deploy/docker-compose.yml ps
curl -I http://localhost/
curl -i http://localhost/api/portfolio/projects
```

首页应返回成功 HTTP 状态；`/api/portfolio/projects` 应返回成功 HTTP 状态和 JSON 响应。如果服务器对外使用域名，把 `localhost` 替换为实际域名或服务器 IP。

常见故障排查：

- 脚本提示缺少 `deploy/artifacts/backend/app.jar`：确认已经上传后端 JAR，并重命名为 `app.jar`
- 脚本提示缺少 `deploy/artifacts/frontend/dist/index.html`：确认已经上传前端 `dist/` 整个目录，而不是只上传其中一部分
- Compose 启动时报找不到镜像：核对 `deploy/build-images.sh` 使用的 `deploy/env` 与 Compose 启动时的 `--env-file deploy/env` 是否一致
- 端口绑定失败：检查 `NGINX_HTTP_PORT`、`MYSQL_PORT`、`BACKEND_PORT`、`FRONTEND_PORT` 是否被占用
- 后端启动失败：执行 `docker compose --env-file deploy/env -f deploy/docker-compose.yml logs portfolio-backend`，重点看数据库连接配置
- MySQL 未就绪：执行 `docker compose --env-file deploy/env -f deploy/docker-compose.yml logs mysql`，检查密码、初始化脚本和数据卷状态
- 网关无法访问页面或 API：执行 `docker compose --env-file deploy/env -f deploy/docker-compose.yml logs nginx`，检查 upstream 和 Nginx 模板渲染结果

### 构建并发布镜像

部署服务器使用镜像模式，不需要源码。也可以在本机或 CI 中构建并推送后端、前端镜像：

```bash
docker build -t your-registry/lzf-studio-backend:latest portfolio-backend
docker build -t your-registry/lzf-studio-frontend:latest portfolio-frontend

docker push your-registry/lzf-studio-backend:latest
docker push your-registry/lzf-studio-frontend:latest
```

然后在服务器的 `deploy/env` 中配置：

```bash
BACKEND_IMAGE=your-registry/lzf-studio-backend:latest
FRONTEND_IMAGE=your-registry/lzf-studio-frontend:latest
NGINX_IMAGE=nginx:1.27-alpine
```
