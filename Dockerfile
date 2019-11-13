FROM alpine

ARG HUGO_VERSION=0.59.1

COPY frontend/hugo_${HUGO_VERSION}_Linux-64bit.tar.gz /tmp/hugo.tar.gz

RUN mkdir -p /tmp/hugo/ && tar -xzf /tmp/hugo.tar.gz -C /tmp/hugo/

RUN ls -la /tmp/hugo/