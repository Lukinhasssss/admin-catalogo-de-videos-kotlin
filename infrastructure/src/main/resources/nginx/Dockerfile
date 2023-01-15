FROM nginx:latest

RUN rm -f /var/log/nginx/access.log && rm -f /var/log/nginx/error.log

COPY default.conf /etc/nginx/conf.d/default.conf