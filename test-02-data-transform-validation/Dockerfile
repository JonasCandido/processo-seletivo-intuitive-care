FROM python:3.13-slim

WORKDIR /app

# Copia código e testes
COPY src/ ./src/
COPY requirements.txt ./requirements.txt

# Instala dependências
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install --no-cache-dir pytest

# Cria pasta data
RUN mkdir -p /app/data

# Volume para persistência de arquivos CSV e ZIP no host
VOLUME ["/app/data", "app/"]

# Comando default: roda os testes na pasta src/tests/
CMD ["pytest", "src/tests", "--maxfail=1", "--disable-warnings", "-v"]
