package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.handlers.specific.utils.DurationTypeGsonAdapter;
import http.handlers.specific.utils.LocalDateTimeTypeGsonAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

  public static TaskManager getDefault() {

    return new InMemoryTaskManager(new InMemoryHistoryManager());
  }

  public static HistoryManager getDefaultHistoryManager() {
    return new InMemoryHistoryManager();
  }

  public static Gson getGson(){
    return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeGsonAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeGsonAdapter())
            .serializeNulls()
            .create();
  }
}
