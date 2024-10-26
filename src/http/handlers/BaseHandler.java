package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class BaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange){
        if (Pattern.matches(httpExchange.getRequestURI().getPath(), "task");
    }
}
