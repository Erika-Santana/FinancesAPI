package command;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;


public class AtualizarTransacoesCommand implements Command{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String path = request.getPathInfo();
	    if (path == null || !path.matches("^/\\d+$")) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
	        return;
	    }

	    int id = Integer.parseInt(path.substring(1));

	    try {
	        JsonObject json = JsonParser.parseReader(new InputStreamReader(request.getInputStream())).getAsJsonObject();

	        String descricao = json.get("descricao").getAsString();
	        double valor = json.get("valor").getAsDouble();
	        String tipoTransacao = json.get("tipoTransacao").getAsString();
	        String tipoCategoria = json.get("tipoCategoria").getAsString();

	        Transacoes transacao = new Transacoes();
	        transacao.setID(id);
	        transacao.setDescricao(descricao);
	        transacao.setValor(valor);
	        transacao.setTipoTransacao(TipoTransacao.valueOf(tipoTransacao));
	        transacao.setTipoCategoria(TipoCategoria.valueOf(tipoCategoria));

	        boolean atualizou = new TransacoesIMP().atualizarTransacao(transacao);

	        if (atualizou) {
	            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	        } else {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Falha ao atualizar.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro ao processar requisição.");
	    }
	}
}



