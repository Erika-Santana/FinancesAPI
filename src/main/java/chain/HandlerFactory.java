package chain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HandlerFactory {

    public static Handler build() {
        try {
            InputStream stream = HandlerFactory.class.getClassLoader().getResourceAsStream("chain-config.json");
            if (stream == null) {
                throw new RuntimeException("chain-config.json not found in classpath");
            }

            InputStreamReader reader = new InputStreamReader(stream);

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> json = gson.fromJson(reader, type);

            List<String> handlerClasses = json.get("chain");

            List<Handler> handlers = new ArrayList<>();
            for (String className : handlerClasses) {
                Class<?> clazz = Class.forName(className);
                Handler handler = (Handler) clazz.getDeclaredConstructor().newInstance();
                handlers.add(handler);
            }

            for (int i = 0; i < handlers.size() - 1; i++) {
                handlers.get(i).setNext(handlers.get(i + 1));
            }

            return handlers.isEmpty() ? null : handlers.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao construir cadeia de Chains", e);
        }
    }
}
