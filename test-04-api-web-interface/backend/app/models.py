from sqlalchemy import Column, Integer, String, Date, Numeric, SmallInteger, CHAR
from .database import Base


class Operadora(Base):
    __tablename__ = "operadoras"

    cnpj = Column(String(14), primary_key=True, index=True)
    razao_social = Column(String)
    nome_fantasia = Column(String)
    modalidade = Column(String)
    uf = Column(String(2))
    data_registro_ans = Column(Date)


class DespesaConsolidada(Base):
    __tablename__ = "despesas_consolidadas"

    id = Column(Integer, primary_key=True, index=True)
    cnpj = Column(String(14))
    razao_social = Column(String(255))
    trimestre = Column(SmallInteger)
    ano = Column(Integer)
    valor_despesas = Column(Numeric(20, 2))

class DespesaAgregada(Base):
    __tablename__ = "despesas_agregadas"

    id = Column(Integer, primary_key=True, index=True)
    razao_social = Column(String(255))
    uf = Column(CHAR(2))
    total_despesas = Column(Numeric(20, 2))
    media_trimestral = Column(Numeric(20, 2))
    desvio_padrao = Column(Numeric(20, 2))