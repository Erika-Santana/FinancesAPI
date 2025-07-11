package model.connection;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;
import model.entities.Transacoes;

public class TransacoesIMP implements TransacoesDAO{
	
	private static final String ADD_TRANSACAO = "INSERT INTO transacoes (descricao, valor, tipo, categoria, date_creation) VALUES (?, ?, ?, ?, ?)";
	private static final String DELETAR_TRANSACAO = "DELETE FROM transacoes WHERE ID = ?";
	private static final String CONSULTAR_TRANSACOES = "SELECT * FROM transacoes  WHERE tipo = ? AND categoria = ? AND MONTH(date_creation) = ? AND YEAR(date_creation) = ? "
			+ "ORDER BY date_creation DESC LIMIT ? OFFSET ? ";
	private static final String ATUALIZAR_TRANSACOES = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, categoria = ?, date_creation = ? WHERE ID = ?";
			
			
	
	@Override
	public boolean criarTransacao(Transacoes transacao) {
		
		       try (var connection = DatabaseConnection.getConnection();
		    		   var preparedStatement = connection.prepareStatement(ADD_TRANSACAO);) {

		    	   preparedStatement.setString(1, transacao.getDescricao());
		    	   preparedStatement.setDouble(2, transacao.getValor());
		    	   preparedStatement.setString(3, transacao.getTipoTransacao().toString());
		    	   preparedStatement.setString(4, transacao.getTipoCategoria().toString());
		    	   preparedStatement.setTimestamp(5, Timestamp.valueOf(transacao.getDateCreation()));
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
	    String sql = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, categoria = ?, date_creation = ? WHERE ID = ?";

	    try (var connection = DatabaseConnection.getConnection();
	    		   var preparedStatement = connection.prepareStatement(ATUALIZAR_TRANSACOES);) {

	    	preparedStatement.setString(1, transacao.getDescricao());
	    	preparedStatement.setDouble(2, transacao.getValor());
	    	preparedStatement.setString(3, transacao.getTipoTransacao().toString());
	        preparedStatement.setString(4, transacao.getTipoCategoria().toString());
	        preparedStatement.setTimestamp(5, Timestamp.valueOf(transacao.getDateCreation()));
	        preparedStatement.setInt(6, transacao.getID());

	        int rows = preparedStatement.executeUpdate();
	        return rows > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public List<Transacoes> visualizarTransacoes(TipoTransacao tipo, TipoCategoria categoria, YearMonth periodo, int offset, int limit) {
        List<Transacoes> lista = new ArrayList();

        try (var connection = DatabaseConnection.getConnection();
	    		   var preparedStatement = connection.prepareStatement(CONSULTAR_TRANSACOES);) {

        	preparedStatement.setString(1, tipo.toString());
        	preparedStatement.setString(2, categoria.toString());
        	preparedStatement.setInt(3, periodo.getMonthValue());
        	preparedStatement.setInt(4, periodo.getYear());
        	preparedStatement.setInt(5, limit);
        	preparedStatement.setInt(6, offset);

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
	public Transacoes buscarTransacaoPorID(int ID) {
		// TODO Auto-generated method stub
		return null;
	}


}
