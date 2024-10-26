import com.sun.net.httpserver.HttpServer;
import http.Server;
import task.*;
import manager.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {

        TaskManager manager = Managers.getDefault();
        Server server = new Server(manager);


    }
}

