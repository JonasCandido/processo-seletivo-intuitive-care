import pandas as pd
import pytest
from enrichment.ans_enrichment import enrich_with_ans_data, normalize_cnpj, normalize_columns

def test_normalize_cnpj():
    assert normalize_cnpj("04.252.011/0001-10") == "04252011000110"
    assert normalize_cnpj("12345678000199") == "12345678000199"
    assert normalize_cnpj(None) is None
    assert normalize_cnpj(pd.NA) is None

def test_normalize_columns():
    df = pd.DataFrame({
        " Razao Social ": ["Empresa A"],
        "CNPJ": ["123"]
    })
    df_norm = normalize_columns(df)
    assert "RAZAO_SOCIAL" in df_norm.columns
    assert "CNPJ" in df_norm.columns

import pandas as pd

def test_enrich_with_ans_data_basic():
    # Consolidado de despesas
    df_consolidado = pd.DataFrame({
        "CNPJ": ["12345678000199", "98765432000188"],
        "RazaoSocial": ["Empresa A", "Empresa B"],
        "Trimestre": [1, 1],
        "Ano": [2025, 2025],
        "ValorDespesas": [1000.0, 2000.0]
    })

    # Cadastro ANS
    df_ans = pd.DataFrame({
        "REGISTRO_OPERADORA": [11111],
        "CNPJ": ["12345678000199"],
        "MODALIDADE": ["Cooperativa Médica"],
        "UF": ["SP"]
    })

    df_enriq = enrich_with_ans_data(df_consolidado, df_ans)

    expected_cols = [
        "CNPJ", "RazaoSocial", "Trimestre", "Ano", "ValorDespesas",
        "RegistroANS", "Modalidade", "UF"
    ]
    assert list(df_enriq.columns) == expected_cols

    # RegistroANS correto e sem .0
    assert df_enriq.loc[0, "RegistroANS"] == "11111"

    # Verifica que o registro sem match é NaN
    assert pd.isna(df_enriq.loc[1, "RegistroANS"])


def test_enrich_with_ans_data_duplicates():
    # Consolidado
    df_consolidado = pd.DataFrame({
        "CNPJ": ["12345678000199"],
        "RazaoSocial": ["Empresa A"],
        "Trimestre": [1],
        "Ano": [2025],
        "ValorDespesas": [1000.0]
    })

    # ANS com duplicados
    df_ans = pd.DataFrame({
        "REGISTRO_OPERADORA": [11111, 22222],
        "CNPJ": ["12345678000199", "12345678000199"],
        "MODALIDADE": ["Cooperativa Médica", "Autogestão"],
        "UF": ["SP", "SP"]
    })

    df_enriq = enrich_with_ans_data(df_consolidado, df_ans)

    # Mantém apenas a primeira ocorrência
    assert df_enriq.loc[0, "RegistroANS"] == "11111"
    assert df_enriq.loc[0, "Modalidade"] == "Cooperativa Médica"
