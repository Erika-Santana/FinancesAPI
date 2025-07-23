package chain;

import command.AddTransacoesCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerFactory{

	    public static Handler createChain() {
	      
	        Handler listar = new ListarTransacoesChain();
	        Handler buscar = new BuscarTransacoesPorIDChain();
	        Handler criar = new AddTransacoesChain();
	        Handler atualizar = new UpdateTransacoesChain();
	        Handler excluir = new ExcluirTransacoesChain();
	        Handler resumo = new ListarTransacoesChain();

	        
	        listar.setNext(buscar);
	        buscar.setNext(criar);
	        criar.setNext(atualizar);
	        atualizar.setNext(excluir);
	        excluir.setNext(resumo);

	        
	        return listar;
	    
	    }

}
