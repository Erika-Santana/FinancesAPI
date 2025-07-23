package chain;

import command.ResumoFinanceiroCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResumoTransacoesChain extends ChainAbstract {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		
		  String path = request.getPathInfo();
			    return "GET".equals(request.getMethod()) && "/resumo".equals(path);
	}

    
    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new ResumoFinanceiroCommand().execute(request, response);
    }

}
