import pandas as pd
from validation.validators import validate_data

def test_validate_data_filtra_corretamente():
    df = pd.DataFrame({
        "RazaoSocial": ["Empresa A", "", "Empresa C"],
        "ValorDespesas": ["1.000,00", "-50,00", "500,00"],
        "CNPJ": [
            "04.252.011/0001-10",  # válido
            "04.252.011/0001-10",  # válido, mas razão vazia e valor negativo
            "11.111.111/1111-11",  # inválido
        ]
    })

    df_result = validate_data(df)

    assert len(df_result) == 1
    assert df_result.iloc[0]["RazaoSocial"] == "Empresa A"
    assert df_result.iloc[0]["ValorDespesas"] == 1000.0
