package factory;

import enumTipos.TipoBanco;
import model.connection.TransacoesDAO;
import model.connection.TransacoesIMP;

public class TransicoesDatabaseFactory {

	private TipoBanco tipo;
	
	public TransicoesDatabaseFactory() {
		if(tipo == null) {
			tipo = TipoBanco.MYSQL;
			
		}
	}
	
	public TransicoesDatabaseFactory(TipoBanco type) {
		tipo = type;
	}
	
	public static TransacoesDAO factory(TipoBanco type) {
		switch (type) {
		case MYSQL: {
			return new TransacoesIMP();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
}
