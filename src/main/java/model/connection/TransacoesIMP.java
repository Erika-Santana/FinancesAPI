package model.connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;
import model.TransacoesDAO;
import model.entities.Transacoes;

public class TransacoesIMP implements TransacoesDAO{
	
	private static final String ADD_TRANSACAO = "INSERT INTO transacoes (descricao, valor, tipo, categoria) VALUES (?, ?, ?, ?)";
	private static final String DELETAR_TRANSACAO = "DELETE FROM transacoes WHERE ID = ?";
	private static final String ATUALIZAR_TRANSACOES = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, categoria = ? WHERE ID = ?";
	private static final String BUSCAR_TRANSACAO_POR_ID = "SELECT * FROM transacoes WHERE id = ?" ;
	private static final String CALCULA_SALDO = " SELECT SUM(CASE WHEN tipo = 'RECEITAS' THEN valor ELSE 0 END) -  SUM(CASE WHEN tipo = 'DESPESAS' THEN valor ELSE 0 END) AS saldo FROM transacoes";
	
	@Override
	public boolean criarTransacao(Transacoes transacao) {
		
		       try (var connection = DatabaseConnection.getConnection();
		    		   var preparedStatement = connection.prepareStatement(ADD_TRANSACAO);) {

		    	   preparedStatement.setString(1, transacao.getDescricao());
		    	   preparedStatement.setDouble(2, transacao.getValor());
		    	   preparedStatement.setString(3, transacao.getTipoTransacao().toString());
		    	   preparedStatement.setString(4, transacao.getTipoCategoria().toString());
		    	   preparedStatement.executeUpdate();
	            return true;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	      
	
	@Override
	public boolean deletarTransacao(int ID) {
		 
	        try (var connection = DatabaseConnection.getConnection();
		    		   var preparedStatement = connection.prepareStatement(DELETAR_TRANSACAO);) {

	        	preparedStatement.setInt(1, ID);
	            int row = preparedStatement.executeUpdate();
	            return row > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	
	}
	
	@Override
	public boolean atualizarTransacao(Transacoes transacao) {

	    try (var connection = DatabaseConnection.getConnection();
	    		   var preparedStatement = connection.prepareStatement(ATUALIZAR_TRANSACOES);) {

	    	preparedStatement.setString(1, transacao.getDescricao());
	    	preparedStatement.setDouble(2, transacao.getValor());
	    	preparedStatement.setString(3, transacao.getTipoTransacao().toString());
	        preparedStatement.setString(4, transacao.getTipoCategoria().toString());
	        preparedStatement.setInt(5, transacao.getID());

	        int rows = preparedStatement.executeUpdate();
	        return rows > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public List<Transacoes> visualizarTransacoes(TipoTransacao tipo, TipoCategoria categoria, Integer mes, Integer ano, int offset, int limit)
{
	    List<Transacoes> lista = new ArrayList<>();
	    
	    StringBuilder transacoes = new StringBuilder("SELECT * FROM transacoes WHERE 1=1");
	    List<Object> parametros = new ArrayList<>();

	    if (tipo != null) {
	    	transacoes.append(" AND tipo = ?");
	        parametros.add(tipo.toString());
	    }
	    if (categoria != null) {
	    	transacoes.append(" AND categoria = ?");
	        parametros.add(categoria.toString());
	    }
	    if (mes != null) {
	        transacoes.append(" AND MONTH(date_creation) = ?");
	        parametros.add(mes);
	    }

	    if (ano != null) {
	        transacoes.append(" AND YEAR(date_creation) = ?");
	        parametros.add(ano);
	    }

	    transacoes.append(" ORDER BY date_creation DESC LIMIT ? OFFSET ?");
	    parametros.add(limit);
	    parametros.add(offset);

	    try (var connection = DatabaseConnection.getConnection();
	         var preparedStatement = connection.prepareStatement(transacoes.toString())) {

	        for (int i = 0; i < parametros.size(); i++) {
	            preparedStatement.setObject(i + 1, parametros.get(i));
	        }

	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Transacoes t = new Transacoes();
	            t.setID(rs.getInt("ID"));
	            t.setDescricao(rs.getString("descricao"));
	            t.setValor(rs.getDouble("valor"));
	            t.setTipoTransacao(TipoTransacao.valueOf(rs.getString("tipo")));
	            t.setTipoCategoria(TipoCategoria.valueOf(rs.getString("categoria")));
	            t.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
	            lista.add(t);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lista;
	}


	@Override
	public Transacoes buscarTransacaoPorID(int ID){
		try (var connection = DatabaseConnection.getConnection();
	    		   var preparedStatement = connection.prepareStatement(BUSCAR_TRANSACAO_POR_ID);) {

			preparedStatement.setInt(1, ID);
			ResultSet rs = preparedStatement.executeQuery();

		        if (rs.next()) {
		            Transacoes transacao = new Transacoes();
		            transacao.setID(rs.getInt("id"));
		            transacao.setDescricao(rs.getString("descricao"));
		            transacao.setValor(rs.getDouble("valor"));
		            transacao.setTipoTransacao(TipoTransacao.valueOf(rs.getString("tipo")));
		            transacao.setTipoCategoria(TipoCategoria.valueOf(rs.getString("categoria")));
		            transacao.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
		            rs.close();
		            return transacao;
		        }
		        rs.close();
		       
		    }catch (SQLException e) {
	            e.printStackTrace();
	        }
		return null;
	}


	public Map<String, Double> totalPorCategoria(String tipo) {
	    Map<String, Double> resultado = new HashMap();

	    String sql = "SELECT categoria, SUM(valor) as total FROM transacoes WHERE tipo = ? GROUP BY categoria";

	    try (var conn = DatabaseConnection.getConnection();
	         var stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, tipo);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            resultado.put(rs.getString("categoria"), rs.getDouble("total"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return resultado;
	}

	public double calcularSaldo() {

	    try (var conn = DatabaseConnection.getConnection();
	         var stmt = conn.prepareStatement(CALCULA_SALDO);
	         var rs = stmt.executeQuery()) {

	        if (rs.next()) return rs.getDouble("saldo");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}



}
