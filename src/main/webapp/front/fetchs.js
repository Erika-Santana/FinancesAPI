const API_BASE = "http://localhost:8080/FInancialApp";
const tbody = document.getElementById("corpoTabela");
const formFiltro = document.getElementById("formFiltro");
const btnAbrirFormulario = document.getElementById("btnAbrirFormulario");
const btnFecharFormulario = document.getElementById("btnFecharFormulario");
const modalForm = document.getElementById("formModal");
const formAdicionar = document.getElementById("formAdicionar");


async function buscarTransacoes(params = {}) {
  const query = new URLSearchParams(params).toString();
  const url = `${API_BASE}/transacoes${query ? `?${query}` : ""}`;

  tbody.innerHTML = "<tr><td colspan='7'>Carregando...</td></tr>";

  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Erro HTTP ${response.status}`);

    const data = await response.json();

    if (!data.length) {
      tbody.innerHTML = "<tr><td colspan='7'>Nenhuma transação encontrada.</td></tr>";
      return;
    }

    tbody.innerHTML = "";

    data.forEach(t => {
      const cor = t.tipoTransacao === "RECEITAS" ? "limegreen" : "crimson";
      const dataFormatada = new Date(t.dateCreation).toLocaleDateString("pt-BR");

      const linha = `
          <tr style="color: ${cor};">
          <td>${t.descricao}</td>
          <td>R$ ${t.valor.toFixed(2)}</td>
          <td>${t.tipoTransacao}</td>
          <td>${t.tipoCategoria}</td>
          <td>${dataFormatada}</td>
          <td><button class="btn-atualizar" data-id="${t.ID}">Atualizar</button></td>
          <td><button class="btn-excluir" data-id="${t.ID}">Excluir</button></td>
        </tr>
      `;
      tbody.innerHTML += linha;
    });
  } catch (error) {
    console.error("Erro ao buscar transações:", error);
    tbody.innerHTML = "<tr><td colspan='7'>Erro ao carregar transações.</td></tr>";
  }

  carregarResumo();
}

formFiltro.addEventListener("submit", (event) => {
  event.preventDefault();

  const params = {
    tipo: document.getElementById("tipo").value,
    categoria: document.getElementById("categoria").value,
    mes: document.getElementById("mes").value,
    ano: document.getElementById("ano").value
  };


  Object.keys(params).forEach(k => !params[k] && delete params[k]);

  buscarTransacoes(params);
});


btnAbrirFormulario.addEventListener("click", () => {
  formAdicionar.reset();
  document.getElementById("transacaoId").value = "";
  document.querySelector("#formAdicionar h3").textContent = "Nova Transação";
  modalForm.classList.remove("hidden");
});

btnFecharFormulario.addEventListener("click", () => {
  formAdicionar.reset();
  document.getElementById("transacaoId").value = "";
  document.querySelector("#formAdicionar h3").textContent = "Nova Transação";
  modalForm.classList.add("hidden");
});

formAdicionar.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("transacaoId").value;
  const body = {
    descricao: document.getElementById("descricao").value,
    valor: parseFloat(document.getElementById("valor").value),
    tipoTransacao: document.getElementById("tipoTransacao").value,
    tipoCategoria: document.getElementById("tipoCategoria").value
  };

  try {
    const url = `${API_BASE}/transacoes${id ? `/${id}` : ""}`;
    const method = id ? "PUT" : "POST";

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (res.ok) {
      alert(id ? "Transação atualizada com sucesso!" : "Transação adicionada com sucesso!");
      formAdicionar.reset();
      modalForm.classList.add("hidden");
      document.getElementById("transacaoId").value = "";
      document.querySelector("#formAdicionar h3").textContent = "Nova Transação";
      buscarTransacoes();
      carregarResumo();
    } else {
      alert("Erro ao salvar transação.");
    }
  } catch (err) {
    console.error("Erro ao enviar transação:", err);
  }
});

document.addEventListener("click", async (event) => {
  const atualizarBtn = event.target.closest(".btn-atualizar");
  const excluirBtn = event.target.closest(".btn-excluir");

  if (atualizarBtn) {
    const id = atualizarBtn.dataset.id;
    try {
      const res = await fetch(`${API_BASE}/transacoes/${id}`);
      if (!res.ok) throw new Error("Erro ao buscar dados para atualização");

      const transacao = await res.json();

      document.getElementById("transacaoId").value = transacao.ID;
      document.getElementById("descricao").value = transacao.descricao;
      document.getElementById("valor").value = transacao.valor;
      document.getElementById("tipoTransacao").value = transacao.tipoTransacao;
      document.getElementById("tipoCategoria").value = transacao.tipoCategoria;

      document.querySelector("#formAdicionar h3").textContent = "Atualizar Transação";
      modalForm.classList.remove("hidden");
    } catch (error) {
      alert("Erro ao carregar transação para atualizar.");
      console.error(error);
    }
  }

  if (excluirBtn) {
    const id = excluirBtn.dataset.id;
    try {
      const res = await fetch(`${API_BASE}/transacoes/${id}`, {
        method: "DELETE"
      });

      if (res.ok) {
        alert("Transação excluída com sucesso!");
        excluirBtn.closest("tr").remove();
        carregarResumo();
      } else {
        alert("Erro ao excluir transação.");
      }
    } catch (e) {
      console.error("Erro DELETE:", e);
    }
  }
});


async function carregarResumo() {
  try {
    const res = await fetch(`${API_BASE}/transacoes/resumo`);
    if (!res.ok) throw new Error("Erro ao carregar resumo");

    const data = await res.json();

    document.getElementById("saldoTotal").textContent = data.saldoTotal.toFixed(2);
    renderizarGrafico(data);
  } catch (e) {
    console.error("Erro ao carregar resumo:", e);
  }
}

let graficoCategorias = null;

function renderizarGrafico(data) {
  const categorias = [...new Set([
    ...Object.keys(data.receitasPorCategoria),
    ...Object.keys(data.despesasPorCategoria)
  ])];

  const valoresReceitas = categorias.map(cat => data.receitasPorCategoria[cat] || 0);
  const valoresDespesas = categorias.map(cat => data.despesasPorCategoria[cat] || 0);

  const ctx = document.getElementById("graficoCategorias").getContext("2d");

  if (graficoCategorias !== null) {
    graficoCategorias.destroy();
  }

  graficoCategorias = new Chart(ctx, {
    type: "bar",
    data: {
      labels: categorias,
      datasets: [
        {
          label: "Receitas",
          backgroundColor: "limegreen",
          data: valoresReceitas
        },
        {
          label: "Despesas",
          backgroundColor: "crimson",
          data: valoresDespesas
        }
      ]
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Resumo por Categoria"
        }
      }
    }
  });
}


buscarTransacoes();
