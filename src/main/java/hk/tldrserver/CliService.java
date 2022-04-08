package hk.tldrserver;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CliService {
    private static final int TIMEOUT = 5000;
    private final static Logger LOGGER = Logger.getLogger(CliService.class.getName());
    /*
    Method tries to execute 'tldr' command line util and return its response.
    In case when 'tldr' don't find information about the command in local index
    it started to update the index from internet.
    Such behavior can lead to slowness of our API, that's why I added timeout.
    */
    public String callCliTldr(String commandName) {
        try {
            Process process = Runtime
                    .getRuntime()
                    .exec("tldr " + commandName);
            if (!process.waitFor(TIMEOUT, MILLISECONDS)) {
                process.destroy();
                return null;
            }
            Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : null;
        } catch(IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Command line tool invocation failed.", e);
        }
        return null;
    }
}
