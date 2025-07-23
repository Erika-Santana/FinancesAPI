const API_BASE = "http://localhost:8080/FInancialApp";
const tbody = document.getElementById("corpoTabela");
const formFiltro = document.getElementById("formFiltro");
const btnAbrirFormulario = document.getElementById("btnAbrirFormulario");
const btnFecharFormulario = document.getElementById("btnFecharFormulario");
const modalForm = document.getElementById("formModal");
const formAdicionar = document.getElementById("formAdicionar");

const inputFields = {
  id: document.getElementById("transacaoId"),
  descricao: document.getElementById("descricao"),
  valor: document.getElementById("valor"),
  tipoTransacao: document.getElementById("tipoTransacao"),
  tipoCategoria: document.getElementById("tipoCategoria"),
};

const resetFormulario = () => {
  formAdicionar.reset();
  inputFields.id.value = "";
  document.querySelector("#formAdicionar h3").textContent = "Nova Transação";
};

const toggleModal = (mostrar) => {
  modalForm.classList.toggle("hidden", !mostrar);
};

const formatarData = (isoString) =>
  new Date(isoString).toLocaleDateString("pt-BR");

const criarLinha = (t) => {
  const cor = t.tipoTransacao === "RECEITAS" ? "limegreen" : "crimson";
  return `
    <tr style="color: ${cor};">
      <td>${t.descricao}</td>
      <td>R$ ${t.valor.toFixed(2)}</td>
      <td>${t.tipoTransacao}</td>
      <td>${t.tipoCategoria}</td>
      <td>${formatarData(t.dateCreation)}</td>
      <td><button class="btn-atualizar" data-id="${t.ID}">Atualizar</button></td>
      <td><button class="btn-excluir" data-id="${t.ID}">Excluir</button></td>
    </tr>
  `;
};

async function buscarTransacoes(params = {}) {
  const query = new URLSearchParams(params).toString();
  const url = `${API_BASE}/transacoes${query ? `?${query}` : ""}`;

  tbody.innerHTML = "<tr><td colspan='7'>Carregando...</td></tr>";

  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Erro HTTP ${response.status}`);
    const transacoes = await response.json();

    if (!transacoes.length) {
      tbody.innerHTML =
        "<tr><td colspan='7'>Nenhuma transação encontrada.</td></tr>";
      return;
    }

    tbody.innerHTML = transacoes.map(criarLinha).join("");
  } catch (error) {
    console.error("Erro ao buscar transações:", error);
    tbody.innerHTML =
      "<tr><td colspan='7'>Erro ao carregar transações.</td></tr>";
  }

  carregarResumo();
}

formFiltro.addEventListener("submit", (e) => {
  e.preventDefault();

  const params = {
    tipo: document.getElementById("tipo").value,
    categoria: document.getElementById("categoria").value,
    mes: document.getElementById("mes").value,
    ano: document.getElementById("ano").value,
  };

  Object.keys(params).forEach((key) => !params[key] && delete params[key]);

  buscarTransacoes(params);
});

btnAbrirFormulario.addEventListener("click", () => {
  resetFormulario();
  toggleModal(true);
});

btnFecharFormulario.addEventListener("click", () => {
  resetFormulario();
  toggleModal(false);
});

formAdicionar.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = inputFields.id.value;
  const body = {
    descricao: inputFields.descricao.value,
    valor: parseFloat(inputFields.valor.value),
    tipoTransacao: inputFields.tipoTransacao.value,
    tipoCategoria: inputFields.tipoCategoria.value,
  };

  const url = `${API_BASE}/transacoes${id ? `/${id}` : ""}`;
  const method = id ? "PUT" : "POST";

  try {
    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    if (!res.ok) throw new Error("Erro ao salvar transação");

    alert(id ? "Transação atualizada!" : "Transação adicionada!");
    resetFormulario();
    toggleModal(false);
    buscarTransacoes();
  } catch (err) {
    alert("Erro ao salvar transação.");
    console.error(err);
  }
});

document.addEventListener("click", async (event) => {
  const atualizarBtn = event.target.closest(".btn-atualizar");
  const excluirBtn = event.target.closest(".btn-excluir");

  if (atualizarBtn) {
    const id = atualizarBtn.dataset.id;
    try {
      const res = await fetch(`${API_BASE}/transacoes/${id}`);
      if (!res.ok) throw new Error();

      const t = await res.json();
      inputFields.id.value = t.ID;
      inputFields.descricao.value = t.descricao;
      inputFields.valor.value = t.valor;
      inputFields.tipoTransacao.value = t.tipoTransacao;
      inputFields.tipoCategoria.value = t.tipoCategoria;

      document.querySelector("#formAdicionar h3").textContent =
        "Atualizar Transação";
      toggleModal(true);
    } catch (err) {
      alert("Erro ao carregar transação.");
      console.error(err);
    }
  }

  if (excluirBtn) {
    const id = excluirBtn.dataset.id;
    if (!confirm("Deseja realmente excluir esta transação?")) return;

    try {
      const res = await fetch(`${API_BASE}/transacoes/${id}`, {
        method: "DELETE",
      });

      if (!res.ok) throw new Error();
      alert("Transação excluída!");
      excluirBtn.closest("tr").remove();
      carregarResumo();
    } catch (err) {
      alert("Erro ao excluir transação.");
      console.error(err);
    }
  }
});

async function carregarResumo() {
  try {
    const res = await fetch(`${API_BASE}/transacoes/resumo`);
    if (!res.ok) throw new Error();

    const data = await res.json();
    document.getElementById("saldoTotal").textContent =
      data.saldoTotal.toFixed(2);
    renderizarGrafico(data);
  } catch (e) {
    console.error("Erro ao carregar resumo:", e);
  }
}

let graficoCategorias = null;

function renderizarGrafico(data) {
  const categorias = [
    ...new Set([
      ...Object.keys(data.receitasPorCategoria),
      ...Object.keys(data.despesasPorCategoria),
    ]),
  ];

  const receitas = categorias.map((cat) => data.receitasPorCategoria[cat] || 0);
  const despesas = categorias.map((cat) => data.despesasPorCategoria[cat] || 0);

  const ctx = document
    .getElementById("graficoCategorias")
    .getContext("2d");

  if (graficoCategorias) graficoCategorias.destroy();

  graficoCategorias = new Chart(ctx, {
    type: "bar",
    data: {
      labels: categorias,
      datasets: [
        {
          label: "Receitas",
          backgroundColor: "limegreen",
          data: receitas,
        },
        {
          label: "Despesas",
          backgroundColor: "crimson",
          data: despesas,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Resumo por Categoria",
        },
      },
    },
  });
}

buscarTransacoes();
