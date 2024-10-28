package http.handlers.specific;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHandler;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHttpHandler extends BaseHandler implements HttpHandler {

    public HistoryHttpHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle (HttpExchange exchange) throws  IOException{
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")){
                String response = gson.toJson(taskManager.getHistory());
                sendText(exchange, response, 200);
            }else sendText(exchange, "Только GET метод работает", 400);
        }catch (Exception e){
            e.printStackTrace();
            sendText(exchange, "server error", 500);
        }
    }
}
