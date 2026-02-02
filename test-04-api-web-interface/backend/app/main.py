from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .routers import operadoras, estatisticas

app = FastAPI(
    title="API de Operadoras",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",
        "http://172.18.0.5:5173",
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(operadoras.router)
app.include_router(estatisticas.router)
