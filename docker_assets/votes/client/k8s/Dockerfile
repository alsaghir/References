FROM nginx:1.23.1-alpine
COPY ./k8s/nginx_default.conf /etc/nginx/conf.d/default.conf
COPY ./build/web /usr/share/nginx/html