import pandas as pd

from validation.validators import validate_data
from enrichment.ans_enrichment import enrich_with_ans_data
from ingestion.download_ans import download_ans_csv


def main():
    print("Iniciando Teste 02 - Pipeline de Transformação")

    # -------- DOWNLOAD ANS --------
    download_ans_csv()

    # -------- ETAPA 2.1 - Validação --------
    df = pd.read_csv(
        "data/consolidado_despesas.csv",
        sep=";",
        dtype={"CNPJ": str}
    )

    df_validado = validate_data(df)

    df_validado.to_csv(
        "data/consolidado_validado.csv",
        index=False,
        sep=";"
    )

    # -------- ETAPA 2.2 - Enriquecimento --------
    df_ans = pd.read_csv(
        "data/operadoras_ativas.csv",
        sep=";",
        encoding="utf-8",
        dtype={"CNPJ": str}
    )

    df_enriquecido = enrich_with_ans_data(
        df_consolidado=df_validado,
        df_ans=df_ans
    )

    df_enriquecido.to_csv(
        "data/consolidado_enriquecido.csv",
        index=False,
        sep=";",
        encoding="utf-8"
    )

    print("Pipeline finalizado com sucesso")


if __name__ == "__main__":
    main()
