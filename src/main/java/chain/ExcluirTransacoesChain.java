package chain;
import command.ExcluirTransacoesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcluirTransacoesChain extends ChainAbstract {

	 @Override
	    protected boolean canHandle(HttpServletRequest request) {
	        return request.getMethod().equals("DELETE") && 
	        		request.getPathInfo().matches("^/\\d+$");
	    }
	    
	    @Override
	    protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        new ExcluirTransacoesCommand().execute(request, response);
	    }

}