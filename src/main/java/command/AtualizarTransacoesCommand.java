package command;


import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.connection.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;


public class AtualizarTransacoesCommand implements Command{

	    @Override
	    public void execute(HttpServletRequest request, HttpServletResponse response){
	        String path = request.getPathInfo();

	        if (path == null || !path.matches("^/\\d+$")) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da transação inválido.");
	            return;
	        }

	        int id = Integer.parseInt(path.substring(1));

	        BufferedReader reader = request.getReader();

	        Gson gson = new GsonBuilder()
	                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
	                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	                            throws JsonParseException {
	                        return LocalDateTime.parse(json.getAsString());
	                    }
	                })
	                .create();

	        Transacoes transacaoAtualizada = gson.fromJson(reader, Transacoes.class);

	        TransacoesDAO dao = new TransacoesIMP();
	        boolean atualizado = dao.atualizarTransacao(transacaoAtualizada);

	        if (atualizado) {
	            response.setStatus(HttpServletResponse.SC_OK);
	        } else {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transação não encontrada.");
	        }
	    }
	}



}
