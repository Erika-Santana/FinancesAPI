package controller;

import java.io.IOException;


import command.AddTransacoesCommand;
import command.AtualizarTransacoesCommand;
import command.ExcluirTransacoesCommand;
import command.ListarTransacoesCommand;
import command.TransacaoDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import command.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/transacoes/*")
public class FrontControllerServlet extends HttpServlet {

    private TransacaoDispatcher dispatcher;

    @Override
    public void init() {
        dispatcher = new TransacaoDispatcher();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Command comando = dispatcher.resolver(request);
            if (comando != null) {
                comando.execute(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota não encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
