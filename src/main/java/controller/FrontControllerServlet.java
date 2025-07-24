package controller;

import java.io.IOException;

import chain.ChainAbstract;
import chain.Handler;
import chain.HandlerFactory;
import command.TransacaoDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import command.Command;


@WebServlet("/transacoes/*")
public class FrontControllerServlet extends HttpServlet {
	
	private Handler chain;

    @Override
    public void init() throws ServletException {
        try {
            this.chain = HandlerFactory.build();
        } catch (Exception e) {
            throw new ServletException("Erro ao inicializar cadeia de handlers", e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {        	 
            if (chain != null) {
                chain.handle(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "API endpoint não configurado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
        }
    }

}
