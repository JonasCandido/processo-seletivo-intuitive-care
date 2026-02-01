FROM postgres:15

ENV POSTGRES_DB=operadoras
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

COPY schema.sql /docker-entrypoint-initdb.d/1-schema.sql
COPY load.sql /docker-entrypoint-initdb.d/2-load.sql
COPY fk_after.sql /docker-entrypoint-initdb.d/3-fk_after.sql
COPY data /data
