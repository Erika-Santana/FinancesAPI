package command;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.connection.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;

import java.io.PrintWriter;

public class BuscarTransacaoPorIdCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        String path = request.getPathInfo();

        if (path == null || !path.matches("^/\\d+$")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido na URL.");
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        TransacoesDAO dao = new TransacoesIMP();
        Transacoes transacao = dao.buscarTransacaoPorID(id);

        if (transacao != null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(transacao));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
        }
    }
}
