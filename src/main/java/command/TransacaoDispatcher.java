package command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import chain.AddTransacoesChain;
import chain.BuscarTransacoesPorIDChain;
import chain.ChainAbstract;
import chain.ExcluirTransacoesChain;
import chain.ListarTransacoesChain;
import chain.ResumoTransacoesChain;
import chain.UpdateTransacoesChain;

public class TransacaoDispatcher {

    private ChainAbstract chain;

    public TransacaoDispatcher() {
    	ChainAbstract resumo = new ResumoTransacoesChain();
    	ChainAbstract buscar = new BuscarTransacoesPorIDChain();
    	ChainAbstract listar = new ListarTransacoesChain();
    	ChainAbstract criar = new AddTransacoesChain();
    	ChainAbstract atualizar = new UpdateTransacoesChain();
    	ChainAbstract excluir = new ExcluirTransacoesChain();

    	resumo.setNext(buscar);
    	buscar.setNext(listar);
    	listar.setNext(criar);
    	criar.setNext(atualizar);
    	atualizar.setNext(excluir);

      
        this.chain = listar;
    }

    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        chain.handle(request, response);
    }
}
