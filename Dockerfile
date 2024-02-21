# Use a imagem do OpenJDK 11 com JDK (inclui o compilador javac)
FROM openjdk:11-jdk-slim

# Instale o curl
RUN apt-get update && apt-get install -y curl

# Crie o diretório de trabalho
WORKDIR /app

# Copie os arquivos .java e .class para o diretório de trabalho
COPY ./src /app/src

# Baixe a biblioteca javax.json e copie para o diretório de trabalho
RUN curl -o javax.json.jar -L https://repo1.maven.org/maven2/org/glassfish/javax.json/1.1.4/javax.json-1.1.4.jar

# Compile todos os arquivos .java usando find e inclua a biblioteca javax.json
RUN find . -name "*.java" -exec javac -cp .:javax.json.jar -d ./out {} +

# Copie o arquivo javax.json.jar diretamente para o diretório out
RUN cp javax.json.jar ./out/

# Compile todos os arquivos .java usando find
# RUN find . -name "*.java" -exec javac -d ./out {} +

# Exponha a porta 8080 para o mundo externo
EXPOSE 8083

# Execute a aplicação quando o contêiner for iniciado
CMD ["java", "-cp", "out:javax.json.jar", "api.LPAApiManager"]