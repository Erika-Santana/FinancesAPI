package chain;

import command.AtualizarTransacoesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateTransacoesChain extends ChainAbstract {

    	@Override
    	protected boolean canHandle(HttpServletRequest request) {
    		 String path = request.getPathInfo(); 
    		    return "PUT".equals(request.getMethod()) &&
    		           path != null &&
    		           path.matches("^/\\d+$");
    	}

    
    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new AtualizarTransacoesCommand().execute(request, response);
    }

}
