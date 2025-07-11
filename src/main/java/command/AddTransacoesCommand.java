package command;

import java.io.IOException;

import com.google.gson.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.connection.TransacoesDAO;
import model.connection.TransacoesIMP;
import model.entities.Transacoes;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class AddTransacoesCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString());
                }
            })
            .create();

        Transacoes transacao = gson.fromJson(reader, Transacoes.class);
        TransacoesDAO dao = new TransacoesIMP();
        dao.criarTransacao(transacao);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}