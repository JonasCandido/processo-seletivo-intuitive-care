import pandas as pd
from aggregation.aggregate_data import aggregate_data

def test_aggregate_basic():
    df = pd.DataFrame({
        "RazaoSocial": ["A", "A", "B"],
        "UF": ["SP", "SP", "RJ"],
        "ValorDespesas": [100, 200, 300]
    })

    df_agg = aggregate_data(df)

    # Verifica total por operadora/UF
    total_a_sp = df_agg.loc[(df_agg.RazaoSocial=="A") & (df_agg.UF=="SP"), "TotalDespesas"].values[0]
    assert total_a_sp == 300

    # Verifica média
    media_a_sp = df_agg.loc[(df_agg.RazaoSocial=="A") & (df_agg.UF=="SP"), "MediaTrimestral"].values[0]
    assert media_a_sp == 150

    # Verifica desvio padrão
    desvio_a_sp = df_agg.loc[(df_agg.RazaoSocial=="A") & (df_agg.UF=="SP"), "DesvioPadrao"].values[0]
    assert round(desvio_a_sp, 2) == 70.71
