import pandas as pd
import re


def normalize_cnpj(cnpj: str) -> str:
    if pd.isna(cnpj):
        return None
    return re.sub(r"\D", "", str(cnpj))


def normalize_columns(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    df.columns = (
        df.columns
        .str.strip()
        .str.upper()
        .str.replace(" ", "_")
    )
    return df


def enrich_with_ans_data(
    df_consolidado: pd.DataFrame,
    df_ans: pd.DataFrame
) -> pd.DataFrame:

    df_consolidado = df_consolidado.copy()
    df_ans = df_ans.copy()

    df_ans = normalize_columns(df_ans)

    df_consolidado["CNPJ"] = df_consolidado["CNPJ"].apply(normalize_cnpj)
    df_ans["CNPJ"] = df_ans["CNPJ"].apply(normalize_cnpj)

    df_ans = df_ans.rename(columns={
        "REGISTRO_OPERADORA": "RegistroANS",
        "MODALIDADE": "Modalidade",
        "UF": "UF"
    })

    df_ans = (
        df_ans
        .dropna(subset=["CNPJ"])
        .drop_duplicates(subset=["CNPJ"], keep="first")
    )

    df_ans = df_ans[
        ["CNPJ", "RegistroANS", "Modalidade", "UF"]
    ]

    # -------- LEFT JOIN --------
    df_final = df_consolidado.merge(
        df_ans,
        on="CNPJ",
        how="left"
    )

    sem_match = df_final["RegistroANS"].isna().sum()
    print(f"Registros sem correspondência na ANS: {sem_match}")

    # -------- Padronização final de colunas --------
    df_final = df_final[
        [
            "CNPJ",
            "RazaoSocial",
            "Trimestre",
            "Ano",
            "ValorDespesas",
            "RegistroANS",
            "Modalidade",
            "UF",
        ]
    ]

    # Converte RegistroANS para string sem .0
    df_final["RegistroANS"] = df_final["RegistroANS"].astype(pd.Int64Dtype()).astype(str)
    return df_final
