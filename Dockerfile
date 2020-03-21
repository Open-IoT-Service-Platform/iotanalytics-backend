FROM gradle:6.2.2-jdk as build
USER root
RUN apt-get update && apt-get install -y make

ADD ./build.gradle /app/build.gradle

ADD . /app
WORKDIR /app
RUN make build
USER appuser
FROM openjdk:11-jre

RUN apt-get update && apt-get install make

COPY --from=build /app/build/libs /app/build/libs/
COPY --from=build /app/Makefile /app/wait-for-it.sh /app/
RUN rm -rf /app/build/distributions
WORKDIR /app

RUN useradd appuser
USER appuser

EXPOSE 8080
