from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_listar_operadoras_status_code():
    response = client.get("/api/operadoras?page=1&limit=5")
    assert response.status_code == 200

def test_listar_operadoras_estrutura_resposta():
    response = client.get("/api/operadoras?page=1&limit=5")
    body = response.json()

    assert "data" in body
    assert "page" in body
    assert "limit" in body
    assert "total" in body

    assert isinstance(body["data"], list)
    assert body["page"] == 1
    assert body["limit"] == 5

def test_listar_operadoras_com_pesquisa():
    search = "SAU"  

    response = client.get(
        f"/api/operadoras?page=1&limit=10&search={search}"
    )

    assert response.status_code == 200

    body = response.json()
    assert "data" in body
    assert isinstance(body["data"], list)

    for operadora in body["data"]:
        cnpj = operadora.get("cnpj", "").upper()
        razao = operadora.get("razao_social", "").upper()

        assert search in cnpj or search in razao


def test_listar_operadoras_campos_minimos():
    response = client.get("/api/operadoras?page=1&limit=1")
    body = response.json()

    if body["data"]:
        operadora = body["data"][0]

        assert "cnpj" in operadora
        assert "razao_social" in operadora
        assert "uf" in operadora


def test_paginacao_funciona():
    r1 = client.get("/api/operadoras?page=1&limit=2").json()
    r2 = client.get("/api/operadoras?page=2&limit=2").json()

    if r1["data"] and r2["data"]:
        assert r1["data"] != r2["data"]
