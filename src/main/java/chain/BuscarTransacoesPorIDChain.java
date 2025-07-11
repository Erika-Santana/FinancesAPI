package chain;

import command.AddTransacoesCommand;
import command.BuscarTransacaoPorIdCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BuscarTransacoesPorIDChain extends ChainAbstract {

    @Override
    protected boolean canHandle(HttpServletRequest request) {
   	 return request.getMethod().equals("GET") && request.getPathInfo().matches("^/\\d+$");
    }
    
    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new BuscarTransacaoPorIdCommand().execute(request, response);
    }

}