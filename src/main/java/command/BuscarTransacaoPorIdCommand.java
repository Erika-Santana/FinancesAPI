package command;

import java.io.IOException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;

import java.io.PrintWriter;
import java.time.LocalDateTime;

public class BuscarTransacaoPorIdCommand implements Command {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String path = request.getPathInfo();

	    if (path == null || !path.matches("^/\\d+$")) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido na URL.");
	        return;
	    }

	    int id = Integer.parseInt(path.substring(1));

	    TransacoesDAO dao = new TransacoesIMP();
	    Transacoes transacao = dao.buscarTransacaoPorID(id);

	    if (transacao != null) {
	        Gson gson = new GsonBuilder()
	                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>)
	                        (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.toString()))
	                .create();

	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.print(gson.toJson(transacao));
	        out.flush();
	    } else {
	        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
	    }
	}

}
