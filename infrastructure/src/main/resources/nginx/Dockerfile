FROM nginx:1.18

RUN rm -f /var/log/nginx/access.log && rm -f /var/log/nginx/error.log

ARG OTEL=https://github.com/open-telemetry/opentelemetry-cpp-contrib/releases/download/webserver%2Fv1.0.0/opentelemetry-webserver-sdk-x64-linux.tgz.zip

ADD ${OTEL} /opt

RUN \
  apt update && \
  apt install unzip && \
  cd /opt && \
  unzip opentelemetry-webserver-sdk-x64-linux.tgz.zip && \
  tar xvfz opentelemetry-webserver-sdk-x64-linux.tgz && \
  cd /opt/opentelemetry-webserver-sdk; ./install.sh && \
  echo "load_module /opt/opentelemetry-webserver-sdk/WebServerModule/Nginx/ngx_http_opentelemetry_module.so;\n$(cat /etc/nginx/nginx.conf)" > /etc/nginx/nginx.conf

ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/opt/opentelemetry-webserver-sdk/sdk_lib/lib

COPY default.conf /etc/nginx/conf.d/default.conf
COPY opentelemetry_module.conf /etc/nginx/conf.d
