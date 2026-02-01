from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from sqlalchemy import func

from ..database import get_db
from .. import models, schemas

router = APIRouter(
    prefix="/api/estatisticas",
    tags=["Estatísticas"]
)

@router.get("/", response_model=schemas.EstatisticasResponse)
def obter_estatisticas(db: Session = Depends(get_db)):
    total_despesas = (
        db.query(func.coalesce(func.sum(models.DespesaAgregada.total_despesas), 0))
        .scalar()
    )

    media_despesas = (
        db.query(func.coalesce(func.avg(models.DespesaAgregada.total_despesas), 0))
        .scalar()
    )

    top_5 = (
        db.query(models.DespesaAgregada)
        .order_by(models.DespesaAgregada.total_despesas.desc())
        .limit(5)
        .all()
    )

    return {
        "total_despesas": total_despesas,
        "media_despesas": media_despesas,
        "top_5_operadoras": top_5
    }
