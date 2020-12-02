package functions;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class Function {
    private static final String API_KEY = "dcc086604d47489e86bd6d216aa5b09b";

    @Inject
    Vertx vertx;

    private WebClient client;

    @PostConstruct
    void initialize() {
        this.client = WebClient.create(vertx,
                new WebClientOptions()
                        .setDefaultHost("boson.cognitiveservices.azure.com")
                        .setDefaultPort(443)
                        .setSsl(true)
                        .setTrustAll(true));
    }

    @Funq
    public Uni<Output[]> function(Input input, @Context CloudEvent cloudEvent) throws Exception {
        return Uni.createFrom().emitter(emitter -> {
            client.post("/face/v1.0/detect")
                    .addQueryParam("returnFaceId", "false")
                    .addQueryParam("returnFaceLandmarks", "false")
                    .addQueryParam("returnFaceAttributes", "age,emotion")
                    .addQueryParam("detectModel", "detection_01")
                    .addQueryParam("recognitionModel", "recognition_03")
                    .putHeader("Ocp-Apim-Subscription-Key", API_KEY)
                    .sendJson(new Request(input.getUrl()))
                    .subscribe()
                    .with(bufferHttpResponse -> {
                        try {
                            Output[] outs = Arrays.stream(bufferHttpResponse.bodyAsJson(Face[].class))
                                    .map(face -> new Output(input.getChat(), face.getFaceAttributes().getAge(), face.getFaceAttributes().getEmotion()))
                                    .toArray(Output[]::new);
                            emitter.complete(outs);
                        } catch (Throwable t) {
                            emitter.fail(t);
                        }
                    }, t -> emitter.fail(t));

        });
    }

    @RegisterForReflection
    private static class Request {
        public Request(String url) {
            this.url = url;
        }

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
