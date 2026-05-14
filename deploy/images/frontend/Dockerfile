FROM nginx:1.27-alpine
COPY artifacts/frontend/dist /usr/share/nginx/html
COPY images/frontend/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
