package command;

import command.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class TransacaoDispatcher {

    private final Map<String, Command> rotas = new HashMap<>();

    public TransacaoDispatcher() {
        rotas.put("GET:/", new ListarTransacoesCommand());
        rotas.put("POST:/", new AddTransacoesCommand());
        rotas.put("GET:/id", new BuscarTransacaoPorIdCommand());
        rotas.put("PUT:/id", new AtualizarTransacoesCommand());
        rotas.put("DELETE:/id", new ExcluirTransacoesCommand());
    }

    public Command resolver(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getPathInfo(); 

        if (path == null || path.equals("/")) {
            return rotas.get(method + ":/");
        } else if (path.matches("^/\\d+$")) {
            return rotas.get(method + ":/id");
        }

        return null;
    }
}

