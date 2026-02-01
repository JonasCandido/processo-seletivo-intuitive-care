from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_listar_despesas_operadora_com_despesas():
    # CNPJ que EXISTE em despesas_consolidadas
    cnpj = "00012698000165"

    response = client.get(f"/api/operadoras/{cnpj}/despesas")
    assert response.status_code == 200

    data = response.json()
    assert isinstance(data, list)
    assert len(data) > 0

    item = data[0]
    assert item["cnpj"] == cnpj
    assert "razao_social" in item
    assert "ano" in item
    assert "trimestre" in item
    assert "valor_despesas" in item


def test_listar_despesas_operadora_existente_sem_despesas():
    # CNPJ que EXISTE em operadoras, mas NÃO tem despesas
    cnpj = "19541931000125"

    response = client.get(f"/api/operadoras/{cnpj}/despesas")
    assert response.status_code == 404

    body = response.json()
    assert body["detail"] == "Nenhuma despesa encontrada para este CNPJ"


def test_listar_despesas_operadora_inexistente():
    cnpj = "00000000000000"

    response = client.get(f"/api/operadoras/{cnpj}/despesas")
    assert response.status_code == 404
