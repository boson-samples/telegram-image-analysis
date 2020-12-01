package functions;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;

public class Function {
    private static final String API_KEY = "dcc086604d47489e86bd6d216aa5b09b";
    private static String endpoint = "https://boson.cognitiveservices.azure.com/face/v1.0/detect?returnFaceId=false&returnFaceLandmarks=false&returnFaceAttributes=age,emotion&recognitionModel=recognition_03&returnRecognitionModel=false&detectModel=detection_01";   

    @Funq
    public Output function(Input input, @Context CloudEvent cloudEvent) {
        try {
            // The image URL we will analyze - to be sent as the HTTP request body
            String requestBody = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{
                    put("url", input.getUrl());
                }});

            System.out.println("Evaluating " + input.getUrl());

            // Build an HTTP request to the Faces API service
            HttpRequest request = HttpRequest.newBuilder()
                .header("Ocp-Apim-Subscription-Key", Function.API_KEY)
                .uri(new URI(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            System.out.println(request);

            // Create HTTPClient to handle our request
            HttpClient client = HttpClient.newHttpClient();

            // Send request to Faces API
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

            // Read the output as JSON
            ObjectMapper mapper = new ObjectMapper();
            List<HashMap> faces = mapper.readValue(response.body(), List.class);

            // We get a list of faces back - use the first
            System.out.println(faces);
            HashMap face = faces.get(0);
            System.out.println(face.get("faceAttributes"));

            // We get the age back as a float, let's just convert to a string for now
            // String age = Float.toString(face.get("faceAttributes").get("age"));
            String age = "10";
            
            // TODO: Extract the emotion from our results
            return new Output(input.getChat(), age, "Happy");

            // For all of these exceptions, it's unclear how to best
            // return from this function to indicate an error code
        } catch(JsonProcessingException e) {
            System.err.println("Error processing input\n" + e);
        } catch(URISyntaxException e) {
            System.err.println("Error processing endpoint\n" + e);
        } catch(IOException e) {
            System.err.println("Error communicating with Faces API\n" + e);
        } catch(Exception e) {
            System.err.println("Unknown error\n" + e);
        }
        return null;
    }

}
