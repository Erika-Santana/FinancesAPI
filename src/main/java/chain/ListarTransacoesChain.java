package chain;

import command.ListarTransacoesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListarTransacoesChain extends ChainAbstract {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		 return request.getMethod().equals("GET") && 
				 (request.getPathInfo() == null || request.getPathInfo().equals("/"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ListarTransacoesCommand().execute(request, response);
		
	}

}