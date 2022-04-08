package hk.tldrserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class TldrServer {
    private final static Logger LOGGER = Logger.getLogger(TldrServer.class.getName());

    public static void main(String[] args) {
        int port = 8080;
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
            server.createContext("/", new RequestHandler());
            server.setExecutor(null);
            server.start();
            LOGGER.log(Level.INFO, "Server started.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Http server failed", e);
        }
    }
}
