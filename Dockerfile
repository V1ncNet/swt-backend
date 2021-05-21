FROM node:lts-alpine AS client-builder

RUN apk --no-cache add \
      git

WORKDIR /usr/src/client

ARG PDFJS_UPSTREAM=https://github.com/MartinKotschmar/swt.git
ARG PDFJS_REF=HEAD
RUN git clone $PDFJS_UPSTREAM . \
 && git checkout $PDFJS_REF

RUN npm install --silent
ARG NODE_ENV=production
ENV NODE_ENV=$NODE_ENV
RUN npm run build

FROM maven:3-adoptopenjdk-11 AS service-builder

WORKDIR /usr/src/service

COPY pom.xml              ./
RUN mvn --batch-mode -q dependency:go-offline

COPY --from=client-builder /usr/src/client/build \
                          ./src/main/resources/static/
COPY .                    ./
RUN mvn --batch-mode -q -DskipTests package

FROM openjdk:11-jre-slim AS application-runner

WORKDIR /opt/byob

ARG JAR_FILE=swt-*.jar
COPY --from=service-builder /usr/src/service/target/${JAR_FILE} ./app.jar

ARG PORT=8080
ENV PORT=$PORT

EXPOSE ${PORT}
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar"]
