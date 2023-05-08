FROM eclipse-temurin:17-jdk-jammy
ENV PROFILE=${PROFILE:-docker-compose}
ENV PORT=${PORT:-8080}
COPY src /src
COPY pom.xml /pom.xml
COPY mvnw /mvnw
COPY .mvn /.mvn
RUN set -ex; \
     ./mvnw -f /pom.xml -Dspring.profiles.active=${PROFILE} clean package; \
     mkdir /app || true; \
     mv /target/*.jar /app/; \
     rm -rf /target; \
     rm -rf /src; \
     rm -rf /pom.xml; \
     rm -rf /mvnw; \
     rm -rf /.mvn;

EXPOSE ${PORT}

CMD set -eux; \
    java -jar -Dspring.profiles.active=${PROFILE} /app/*.jar;


# Build like this:
# docker build  -f Dockerfile -t java-app .

# Run like this:
# docker network create superhero || true
# docker run -it --rm --name java-app -p 8080:8080 --network superhero java-app

#docker run \
#  -v $(git rev-parse --show-toplevel):/repo:rw \
#  --workdir /repo \
#  --name java-app \
#  -d eclipse-temurin:17-jdk-jammy
