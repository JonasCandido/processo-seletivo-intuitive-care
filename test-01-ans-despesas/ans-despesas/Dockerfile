FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copia pom.xml e src primeiro para aproveitar cache do Maven
COPY pom.xml .
COPY src ./src

# Instala Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Compila o projeto / roda testes
RUN mvn clean package

# Cria pastas internas para downloads e temp
RUN mkdir -p /app/downloads /app/temp

# Define volumes para persistir os dados no host
VOLUME [ "/app/downloads", "/app/temp" ]

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/ans-despesas-0.0.1-SNAPSHOT.jar"]
