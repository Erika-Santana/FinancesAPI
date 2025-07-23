package model;

import java.time.YearMonth;

import java.util.List;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;
import model.entities.Transacoes;

public interface TransacoesDAO {
	
	public boolean criarTransacao(Transacoes transacao);
	public boolean deletarTransacao(int ID);
	public boolean atualizarTransacao(Transacoes transacao);
	public List<Transacoes> visualizarTransacoes(TipoTransacao tipo, TipoCategoria categoria, Integer mes, Integer ano, int offset, int limit);
	Transacoes buscarTransacaoPorID(int ID);
}
