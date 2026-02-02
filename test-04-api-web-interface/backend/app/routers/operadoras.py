from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import or_
from typing import List

from ..database import get_db
from .. import models, schemas

router = APIRouter(prefix="/api/operadoras", tags=["Operadoras"])


@router.get("/", response_model=schemas.OperadoraListResponse)
def listar_operadoras(
    page: int = Query(1, ge=1),
    limit: int = Query(10, le=100),
    search: str | None = Query(None),
    db: Session = Depends(get_db)
):
    offset = (page - 1) * limit

    query = db.query(models.Operadora)

    if search:
        query = query.filter(
            or_(
                models.Operadora.cnpj.ilike(f"%{search}%"),
                models.Operadora.razao_social.ilike(f"%{search}%")
            )
        )

    total = query.count()

    operadoras = (
        query
        .order_by(models.Operadora.razao_social)
        .offset(offset)
        .limit(limit)
        .all()
    )

    return {
        "data": operadoras,
        "page": page,
        "limit": limit,
        "total": total
    }

@router.get("/{cnpj}", response_model=schemas.OperadoraResponse)
def obter_operadora(
    cnpj: str,
    db: Session = Depends(get_db)
):
    operadora = (
        db.query(models.Operadora)
        .filter(models.Operadora.cnpj == cnpj)
        .first()
    )

    if not operadora:
        raise HTTPException(
            status_code=404,
            detail="Operadora não encontrada"
        )

    return operadora

@router.get(
    "/{cnpj}/despesas",
    response_model=List[schemas.DespesaResponse]
)
def listar_despesas_operadora(
    cnpj: str,
    db: Session = Depends(get_db)
):
    despesas = (
        db.query(models.DespesaConsolidada)
        .filter(models.DespesaConsolidada.cnpj == cnpj)
        .order_by(
            models.DespesaConsolidada.ano,
            models.DespesaConsolidada.trimestre
        )
        .all()
    )

    if not despesas:
        raise HTTPException(
            status_code=404,
            detail="Nenhuma despesa encontrada para este CNPJ"
        )

    return despesas
