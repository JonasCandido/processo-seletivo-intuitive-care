from pydantic import BaseModel, ConfigDict
from typing import Optional, List
from datetime import date
from decimal import Decimal


class OperadoraBase(BaseModel):
    cnpj: str
    razao_social: str
    nome_fantasia: Optional[str]
    modalidade: Optional[str]
    uf: Optional[str]

class OperadoraResponse(OperadoraBase):
    data_registro_ans: Optional[date]

    model_config = ConfigDict(from_attributes=True)

class OperadoraListResponse(BaseModel):
    data: List[OperadoraResponse]
    page: int
    limit: int
    total: int

class DespesaResponse(BaseModel):
    cnpj: str
    razao_social: str
    trimestre: int
    ano: int
    valor_despesas: Decimal

    model_config = ConfigDict(from_attributes=True)

class DespesaAgregadaResponse(BaseModel):
    razao_social: str
    uf: Optional[str] = None
    total_despesas: Decimal
    media_trimestral: Decimal
    desvio_padrao: Optional[Decimal] = None

    model_config = ConfigDict(from_attributes=True)

class EstatisticasResponse(BaseModel):
    total_despesas: Decimal
    media_despesas: Decimal
    top_5_operadoras: List[DespesaAgregadaResponse]

