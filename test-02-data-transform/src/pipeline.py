import pandas as pd
from validation.validators import validate_data


def main():
    print("Iniciando Teste 02 - Pipeline de Transformação")

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

    print("Pipeline finalizado com sucesso")


if __name__ == "__main__":
    main()
