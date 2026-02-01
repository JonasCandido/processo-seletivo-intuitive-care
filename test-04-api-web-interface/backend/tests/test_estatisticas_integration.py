from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_obter_estatisticas():
    response = client.get("/api/estatisticas")

    assert response.status_code == 200

    body = response.json()

    assert "total_despesas" in body
    assert "media_despesas" in body
    assert "top_5_operadoras" in body

    assert isinstance(body["total_despesas"], (int, float, str))
    assert isinstance(body["media_despesas"], (int, float, str))
    assert isinstance(body["top_5_operadoras"], list)

    if len(body["top_5_operadoras"]) > 0:
        item = body["top_5_operadoras"][0]

        assert "razao_social" in item
        assert "uf" in item
        assert "total_despesas" in item
        assert "media_trimestral" in item
        assert "desvio_padrao" in item
