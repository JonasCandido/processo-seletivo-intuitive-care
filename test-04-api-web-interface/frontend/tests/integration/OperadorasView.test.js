import { mount, flushPromises } from "@vue/test-utils";
import MockAdapter from "axios-mock-adapter";
import api from "../../src/api/client";
import OperadorasView from "../../src/views/OperadorasView.vue";
import { operadorasMock } from "../mocks/operadoras";

describe("OperadorasView – Integration Test", () => {
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(api);

    mock.onGet("/api/operadoras").reply(200, operadorasMock);
  });

  afterEach(() => {
    mock.restore();
  });

  it("renderiza título e input de busca", async () => {
    const wrapper = mount(OperadorasView);

    expect(wrapper.text()).toContain("Operadoras");
    expect(wrapper.find("input").exists()).toBe(true);
  });

  it("renderiza corretamente os dados da operadora", async () => {
    const wrapper = mount(OperadorasView);

    await flushPromises();

    const firstRow = wrapper.findAll("tbody tr")[0];

    expect(firstRow.text()).toContain("12345678000100");
    expect(firstRow.text()).toContain("Operadora Teste A");
    expect(firstRow.text()).toContain("SP");
    const formattedDate = new Date("2010-01-01").toLocaleDateString();
    expect(firstRow.text()).toContain(formattedDate);
  });

  it("exibe '-' para campos opcionais nulos", async () => {
    const wrapper = mount(OperadorasView);

    await flushPromises();

    const secondRow = wrapper.findAll("tbody tr")[1];

    expect(secondRow.text()).toContain("-");
  });

  it("desabilita botão Anterior na primeira página", async () => {
    const wrapper = mount(OperadorasView);

    await flushPromises();

    const btnAnterior = wrapper.find("button");
    expect(btnAnterior.element.disabled).toBe(true);
  });
});
