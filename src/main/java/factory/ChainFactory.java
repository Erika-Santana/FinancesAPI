package factory;

import com.google.gson.*;

import chain.Handler;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ChainFactory {

    public static Handler criarCadeia() throws Exception {
        InputStreamReader reader = new InputStreamReader(
        		ChainFactory.class.getClassLoader().getResourceAsStream("chain-config.json")
        );

        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray array = json.getAsJsonArray("chain");

        List<Handler> handlers = new ArrayList<>();

        for (JsonElement el : array) {
            String className = el.getAsString();
            Class<?> classe = Class.forName(className);
            Constructor<?> construtor = classe.getDeclaredConstructor();
            Handler handler = (Handler) construtor.newInstance();
            handlers.add(handler);
        }

        for (int i = 0; i < handlers.size() - 1; i++) {
            handlers.get(i).setNext(handlers.get(i + 1));
        }

        return handlers.get(0);
    }
}
