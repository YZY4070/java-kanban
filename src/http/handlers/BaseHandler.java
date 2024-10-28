package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import http.handlers.specific.utils.DurationTypeGsonAdapter;
import http.handlers.specific.utils.LocalDateTimeTypeGsonAdapter;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHandler {
  protected final TaskManager taskManager;
  public Gson gson = Managers.getGson();

  public BaseHandler(InMemoryTaskManager taskManager) {
    this.taskManager = taskManager;
  }

  protected void sendText(HttpExchange httpExchange, String text, int statusCode) throws IOException {
    byte[] resp = text.getBytes(StandardCharsets.UTF_8);
    httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
    httpExchange.sendResponseHeaders(statusCode, resp.length);
    try (OutputStream os = httpExchange.getResponseBody()) {
      os.write(resp);
    }
    httpExchange.close();
  }

  protected void sendNotFound(HttpExchange httpExchange) throws IOException {
    String text = "Not Found";
    sendText(httpExchange, text, 404);
  }

  protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
    String text = "Not Acceptable";
    sendText(httpExchange, text, 406);
  }

  protected String getRequestBodyAsString(HttpExchange httpExchange) throws IOException {
    return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
  }

}
