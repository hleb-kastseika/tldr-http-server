FROM alpine:latest

#We install here two things:
# 1) JRE to be able run our Java app
# 2) tldr using npm
RUN apk --update add openjdk11-jre npm \
    && npm install -g tldr \
    && tldr --update

COPY target/tldrserver-1.0.jar tldrserver-1.0.jar

EXPOSE 8080

CMD java -jar tldrserver-1.0.jar

