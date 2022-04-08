package hk.tldrserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements HttpHandler {
    private CliService cliService = new CliService();

    @Override
    public void handle(HttpExchange httpThread) throws IOException {
        String commandName = httpThread.getRequestURI().getPath().substring(1);
        if (commandName.isEmpty()) {
            sendResponse(httpThread, "Please, specify command name.", 400);
        }

        String result = cliService.invokeTldr(commandName);
        if (result != null) {
            sendResponse(httpThread, result, 200);
        } else {
            sendResponse(httpThread,
                    "tldr has no information about '" + commandName + "'.",
                    404);
        }
    }

    private void sendResponse(HttpExchange httpThread, String responseBody, int code)
            throws IOException {
        httpThread.sendResponseHeaders(code, responseBody.length());
        OutputStream response = httpThread.getResponseBody();
        response.write(responseBody.getBytes(StandardCharsets.UTF_8));
        response.close();
    }
}