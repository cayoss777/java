package API;
/*
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONObject;
*/

public class ApiPeruService {

    private static String API_TOKEN = "TU_TOKEN";
/*
    public static JSONObject consultarDNI(String dni) {

        try {

            if (dni == null || !dni.matches("\\d{8}")) {
                System.err.println("DNI inválido");
                return null;
            }

            String url = "https://peruapi.com/api/dni/" 
                    + dni 
                    + "?api_token=" 
                    + API_TOKEN;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                return new JSONObject(response.body());

            } else {

                System.err.println("Error API: " + response.statusCode());
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
*/
}