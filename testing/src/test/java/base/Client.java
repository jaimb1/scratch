package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(final String[] args) {
        if (args.length < 2) {
            return;
        }

        final String hostname = args[0];
        final int port = Integer.parseInt(args[1]);
        System.err.print("using port " + port + " to connect...");
        try (final Socket socket = new Socket(hostname, port)) {
            System.err.println("...but using port " + socket.getLocalPort() + " to get data from server");
            final InputStream input = socket.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            final String time = reader.readLine();

            System.out.println(time);


        } catch (final UnknownHostException ex) {

            System.out.println("base.Server not found: " + ex.getMessage());

        } catch (final IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

