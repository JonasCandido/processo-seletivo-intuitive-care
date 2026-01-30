import requests
from pathlib import Path

ANS_URL = (
    "https://dadosabertos.ans.gov.br/FTP/PDA/"
    "operadoras_de_plano_de_saude_ativas/Relatorio_cadop.csv"
)


def download_ans_csv(output_path: str = "data/operadoras_ativas.csv") -> None:
    """
    Baixa o CSV de operadoras ativas da ANS sempre que o pipeline é executado,
    sobrescrevendo qualquer arquivo existente. 
    """

    output_file = Path(output_path)
    output_file.parent.mkdir(parents=True, exist_ok=True)

    print("Baixando arquivo da ANS (sobrescrevendo se existir)...")

    try:
        response = requests.get(ANS_URL, timeout=60)
        response.raise_for_status()
    except requests.exceptions.Timeout:
        print("❌ Erro: tempo de espera esgotado (timeout).")
        return
    except requests.exceptions.ConnectionError:
        print("❌ Erro: falha na conexão com a ANS.")
        return
    except requests.exceptions.HTTPError as e:
        print(f"❌ Erro HTTP ao baixar o arquivo: {e}")
        return
    except requests.exceptions.RequestException as e:
        print(f"❌ Erro genérico ao baixar o arquivo: {e}")
        return

    # Salva o arquivo
    try:
        with open(output_file, "wb") as f:
            f.write(response.content)
        print(f"✅ Download concluído com sucesso: {output_file}")
    except Exception as e:
        print(f"❌ Erro ao salvar o arquivo: {e}")
