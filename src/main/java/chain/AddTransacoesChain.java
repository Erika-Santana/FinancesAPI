package chain;

import command.AddTransacoesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddTransacoesChain extends ChainAbstract {

    @Override
    protected boolean canHandle(HttpServletRequest request) {
   	 return request.getMethod().equals("POST") && 
			 (request.getPathInfo() == null || request.getPathInfo().equals("/"));
    }
    
    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new AddTransacoesCommand().execute(request, response);
    }

}
