export const operadorasMock = {
  data: [
    {
      cnpj: "12345678000100",
      razao_social: "Operadora Teste A",
      nome_fantasia: "Teste A",
      modalidade: "Medicina de Grupo",
      uf: "SP",
      data_registro_ans: "2010-01-01",
    },
    {
      cnpj: "98765432000199",
      razao_social: "Operadora Teste B",
      nome_fantasia: null,
      modalidade: "Cooperativa",
      uf: "RJ",
      data_registro_ans: null,
    },
  ],
  page: 1,
  limit: 10,
  total: 2,
};

export const operadoraMock = {
  cnpj: "12345678000100",
  razao_social: "Operadora Teste A",
  nome_fantasia: "Teste A",
  modalidade: "Medicina de Grupo",
  uf: "SP",
  data_registro_ans: "2010-01-01",
};

export const despesasMock = [
  {
    cnpj: "12345678000100",
    razao_social: "Operadora Teste A",
    ano: 2023,
    trimestre: 1,
    valor_despesas: 1500000.5,
  },
  {
    cnpj: "12345678000100",
    razao_social: "Operadora Teste A",
    ano: 2023,
    trimestre: 2,
    valor_despesas: 1700000.75,
  },
];
