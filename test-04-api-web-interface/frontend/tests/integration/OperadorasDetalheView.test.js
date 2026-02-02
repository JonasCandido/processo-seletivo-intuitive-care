import { mount, flushPromises } from "@vue/test-utils";
import MockAdapter from "axios-mock-adapter";
import api from "../../src/api/client";
import OperadoraDetalheView from "../../src/views/OperadoraDetalheView.vue";
import { operadoraMock, despesasMock } from "../mocks/operadoras";

vi.mock("vue-router", () => ({
  useRoute: () => ({
    params: {
      cnpj: "12345678000100",
    },
  }),
}));

describe("OperadoraDetalheView – Integration Test", () => {
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(api);

    mock
      .onGet("/api/operadoras/12345678000100")
      .reply(200, operadoraMock);

    mock
      .onGet("/api/operadoras/12345678000100/despesas")
      .reply(200, despesasMock);
  });

  afterEach(() => {
    mock.restore();
  });

  it("renderiza dados principais da operadora", async () => {
    const wrapper = mount(OperadoraDetalheView);

    await flushPromises();

    expect(wrapper.text()).toContain("Operadora Teste A");
    expect(wrapper.text()).toContain("12345678000100");
    expect(wrapper.text()).toContain("SP");
    expect(wrapper.text()).toContain("Medicina de Grupo");
  });

  it("renderiza a tabela de despesas", async () => {
    const wrapper = mount(OperadoraDetalheView);

    await flushPromises();

    const rows = wrapper.findAll("tbody tr");
    expect(rows.length).toBe(2);

    expect(rows[0].text()).toContain("2023");
    expect(rows[0].text()).toContain("1");
    expect(rows[0].text()).toContain("R$");
  });

  it("exibe mensagem quando não há despesas", async () => {
    mock
      .onGet("/api/operadoras/12345678000100/despesas")
      .reply(404);

    const wrapper = mount(OperadoraDetalheView);

    await flushPromises();

    expect(wrapper.text()).toContain(
      "Esta operadora não possui despesas registradas"
    );
  });
});
