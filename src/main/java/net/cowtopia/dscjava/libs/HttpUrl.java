package net.cowtopia.dscjava.libs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrl
{
    // https://api.thecatapi.com/api/images/get?format=src&type=png
    public static String whereUrlRedirects(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false); // Disable automatic redirection

            int statusCode = connection.getResponseCode();

            if (statusCode >= 300 && statusCode < 400) {
                String newUrl = connection.getHeaderField("Location"); // Get the redirected URL
                connection.disconnect();
                return newUrl;
            } else {
                connection.disconnect();
                return "No response";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "No response";
        }
    }

    // maybe useful? (stolen from chatgpt)
    public static void showWhereUrlRedirects(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false); // Disable automatic redirection

            int statusCode = connection.getResponseCode();

            if (statusCode >= 300 && statusCode < 400) {
                String newUrl = connection.getHeaderField("Location"); // Get the redirected URL
                System.out.println("Redirected to: " + newUrl);
            } else {
                System.out.println("No redirection detected. Status code: " + statusCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
