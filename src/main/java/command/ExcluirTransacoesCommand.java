package command;

import java.io.IOException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TransacoesDAO;
import model.connection.TransacoesIMP;

public class ExcluirTransacoesCommand implements Command{

	    @Override
	    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        String path = request.getPathInfo();

	        if (path == null || !path.matches("^/\\d+$")) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da transação inválido.");
	            return;
	        }

	        int id = Integer.parseInt(path.substring(1));
	        TransacoesDAO dao = new TransacoesIMP();
	        boolean removido = dao.deletarTransacao(id);

	        if (removido) {
	            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	        } else {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
	        }
	    }
	}


	

