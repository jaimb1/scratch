package base;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(final String[] args) {
        if (args.length < 1) {
            return;
        }

        final int port = Integer.parseInt(args[0]);

        try (final ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("listening on port " + port + "...");

            while (true) {
                final Socket socket = serverSocket.accept();


                System.out.println("...using port " + socket.getPort() + " to send data to new client");

                final OutputStream output = socket.getOutputStream();
                final PrintWriter writer = new PrintWriter(output, true);

                writer.println(new Date().toString());
            }

        } catch (final IOException ex) {
            System.out.println("base.Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

