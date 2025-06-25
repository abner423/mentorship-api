# Usa a imagem oficial do MySQL
FROM mysql:8.0

# Define variáveis de ambiente para configuração do MySQL
ENV MYSQL_ROOT_PASSWORD=123
ENV MYSQL_DATABASE=mentorship

# Expõe a porta padrão do MySQL
EXPOSE 3306