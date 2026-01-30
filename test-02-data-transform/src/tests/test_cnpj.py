import numpy as np
from validation.validators import is_valid_cnpj

def test_cnpj_valido():
    assert is_valid_cnpj("04.252.011/0001-10") is True

def test_cnpj_invalido_digito():
    assert is_valid_cnpj("04.252.011/0001-11") is False

def test_cnpj_todos_iguais():
    assert is_valid_cnpj("11111111111111") is False

def test_cnpj_tamanho_errado():
    assert is_valid_cnpj("123") is False

def test_cnpj_none():
    assert is_valid_cnpj(None) is False

def test_cnpj_nan():
    assert is_valid_cnpj(np.nan) is False
