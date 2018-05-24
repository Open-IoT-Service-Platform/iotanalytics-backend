FROM ubuntu:xenial
RUN apt-get update -qq && apt-get upgrade -y

RUN apt-get install -y curl unzip build-essential openjdk-8-jdk-headless gradle

ADD ./build.gradle /app/build.gradle
RUN cd /app && gradle build -x :bootRepackage -x test --continue

ADD . /app
WORKDIR /app
RUN make build

FROM anapsix/alpine-java:8

RUN apk add make

COPY --from=0 /app /app
RUN rm -rf /app/build/distributions
WORKDIR /app
EXPOSE 8080