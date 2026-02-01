from fastapi import FastAPI
from .routers import operadoras, estatisticas

app = FastAPI(
    title="API de Operadoras",
    version="1.0.0"
)

app.include_router(operadoras.router)
app.include_router(estatisticas.router)
