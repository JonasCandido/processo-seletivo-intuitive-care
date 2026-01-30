import pandas as pd
import re


def is_valid_cnpj(cnpj) -> bool:
    if pd.isna(cnpj):
        return False

    cnpj = re.sub(r"\D", "", str(cnpj))

    if len(cnpj) != 14:
        return False

    if cnpj == cnpj[0] * 14:
        return False

    def calc_digit(cnpj, weights):
        total = sum(int(d) * w for d, w in zip(cnpj, weights))
        rest = total % 11
        return 0 if rest < 2 else 11 - rest

    weights_1 = [5,4,3,2,9,8,7,6,5,4,3,2]
    weights_2 = [6] + weights_1

    d1 = calc_digit(cnpj[:12], weights_1)
    d2 = calc_digit(cnpj[:12] + str(d1), weights_2)

    return cnpj[-2:] == f"{d1}{d2}"


def validate_data(df: pd.DataFrame) -> pd.DataFrame:
    initial_size = len(df)

    mask_razao = df["RazaoSocial"].notna() & (df["RazaoSocial"].str.strip() != "")
    razao_invalidas = len(df[~mask_razao])
    df = df[mask_razao].copy()

    df["ValorDespesas"] = (
        df["ValorDespesas"]
        .astype(str)
        .str.replace(".", "", regex=False)
        .str.replace(",", ".", regex=False)
        .astype(float)
    )

    mask_valor = df["ValorDespesas"] > 0
    valores_invalidos = len(df[~mask_valor])
    df = df[mask_valor].copy()

    df["CNPJ_VALIDO"] = df["CNPJ"].apply(is_valid_cnpj)
    cnpjs_invalidos = len(df[~df["CNPJ_VALIDO"]])
    df = df[df["CNPJ_VALIDO"]].drop(columns=["CNPJ_VALIDO"])

    print(f"Linhas iniciais: {initial_size}")
    print(f"Razões sociais vazias descartadas: {razao_invalidas}")
    print(f"Valores zero ou negativos descartados: {valores_invalidos}")
    print(f"CNPJs inválidos descartados: {cnpjs_invalidos}")
    print(f"Linhas finais válidas: {len(df)}")

    return df
