import pandas as pd
from pathlib import Path
import zipfile

def aggregate_data(df: pd.DataFrame) -> pd.DataFrame:

    df_agg = (
        df.groupby(["RazaoSocial", "UF"])
          .agg(
              TotalDespesas=("ValorDespesas", "sum"),
              MediaTrimestral=("ValorDespesas", "mean"),
              DesvioPadrao=("ValorDespesas", "std")
          )
          .reset_index()
    )

    df_agg["TotalDespesas"] = df_agg["TotalDespesas"].round(2)
    df_agg["MediaTrimestral"] = df_agg["MediaTrimestral"].round(2)
    df_agg["DesvioPadrao"] = df_agg["DesvioPadrao"].round(2)

    df_agg = df_agg.sort_values("TotalDespesas", ascending=False)

    return df_agg

def save_csv_and_zip(df: pd.DataFrame, output_csv: str, zip_name: str):
    output_file = Path(output_csv)
    output_file.parent.mkdir(parents=True, exist_ok=True)

    df.to_csv(output_file, sep=";", index=False, encoding="utf-8")
    print(f"Arquivo CSV salvo: {output_file}")

    # Compacta
    zip_path = Path(zip_name)
    with zipfile.ZipFile(zip_path, mode="w", compression=zipfile.ZIP_DEFLATED) as zf:
        zf.write(output_file, arcname=output_file.name)
    print(f"Arquivo compactado: {zip_path}")
