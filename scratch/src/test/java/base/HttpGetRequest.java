package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpGetRequest {
    private static final String ERROR_MSG = "Usage: java HttpGetRequest http://localhost:8443/foo/bar?param=1";

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println(ERROR_MSG);
            return;
        }

        try {
            System.out.println("Sending request to: " + args[0]);
            // Parse the URL from the first argument
            final URL url = new URI(args[0]).toURL();

            // Open a connection to the URL
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            final int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response from the server
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            final StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response
            System.out.println("Response: " + response);
        } catch (final URISyntaxException e) {
            System.err.println(ERROR_MSG);
            e.printStackTrace();
        } catch (final IOException e) {
            System.err.println("Error sending request.");
            e.printStackTrace();
        }
    }
}
