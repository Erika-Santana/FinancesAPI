package command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;

public class ResumoFinanceiroCommand implements Command {


		@Override
		public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
			 TransacoesIMP dao = new TransacoesIMP();

		        Map<String, Double> receitas = dao.totalPorCategoria("RECEITAS");
		        Map<String, Double> despesas = dao.totalPorCategoria("DESPESAS");
		        double saldo = dao.calcularSaldo();

		        System.out.print("Entrou no meu DAO do Resumo");
		        JsonObject json = new JsonObject();
		        json.add("receitasPorCategoria", new Gson().toJsonTree(receitas));
		        json.add("despesasPorCategoria", new Gson().toJsonTree(despesas));
		        json.addProperty("saldoTotal", saldo);

		        response.setContentType("application/json");
		        PrintWriter out = response.getWriter();
		        out.print(json.toString());
		        out.flush();
		}
	
}


