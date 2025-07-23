package command;

import java.io.IOException;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;

public class ListarTransacoesCommand implements Command{

	    @Override
	    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        int page = 1;
	        int limit = 10;

	        String pageParam = request.getParameter("page");
	        String limitParam = request.getParameter("limit");

	        if (pageParam != null) page = Integer.parseInt(pageParam);
	        if (limitParam != null) limit = Integer.parseInt(limitParam);

	        int offset = (page - 1) * limit;

	        TransacoesDAO dao = new TransacoesIMP();

	        TipoTransacao tipo = null;
	        String tipoParam = request.getParameter("tipo");

	        if (tipoParam != null && !tipoParam.isEmpty()) {
	            try {
	                tipo = TipoTransacao.valueOf(tipoParam);
	            } catch (IllegalArgumentException e) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo de transação inválido.");
	                return;
	            }
	        } else {
	            tipo = null;
	        }

	       
	        TipoCategoria categoria = null;
	        String categoriaParam = request.getParameter("categoria");

	        if (categoriaParam != null && !categoriaParam.isEmpty()) {
	            try {
	                categoria = TipoCategoria.valueOf(categoriaParam);
	            } catch (IllegalArgumentException e) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Categoria inválida.");
	                return;
	            }
	        } else {
	            categoria = null; 
	        }

	        
	        String mesParam = request.getParameter("mes");
	        String anoParam = request.getParameter("ano");

	        Integer mes = (mesParam != null && !mesParam.isEmpty()) ? Integer.parseInt(mesParam) : null;
	        Integer ano = (anoParam != null && !anoParam.isEmpty()) ? Integer.parseInt(anoParam) : null;

	        List<Transacoes> transacoes = dao.visualizarTransacoes(tipo, categoria, mes, ano, offset, limit);


	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();

	        Gson gson = new GsonBuilder()
	            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
	                @Override
	                public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
	                    return new JsonPrimitive(date.toString());
	                }

	
	            })
	            .create();

	        out.print(gson.toJson(transacoes));
	        out.flush();
	    }
	}
	

