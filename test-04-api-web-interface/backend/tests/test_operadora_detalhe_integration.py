from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_obter_operadora_existente():
    # usa um CNPJ que você SABE que existe no CSV
    cnpj = "19541931000125"

    response = client.get(f"/api/operadoras/{cnpj}")
    assert response.status_code == 200

    body = response.json()
    assert body["cnpj"] == cnpj
    assert "razao_social" in body
    assert "uf" in body


def test_obter_operadora_inexistente():
    cnpj = "00000000000000"

    response = client.get(f"/api/operadoras/{cnpj}")
    assert response.status_code == 404

    body = response.json()
    assert body["detail"] == "Operadora não encontrada"
