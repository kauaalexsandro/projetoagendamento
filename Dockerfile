FROM eclipse-temurin:25-jdk

WORKDIR /app

# copia tudo
COPY . .

# builda o projeto
RUN ./mvnw clean package -DskipTests

# roda o jar
CMD ["java", "-jar", "target/*.jar"]
