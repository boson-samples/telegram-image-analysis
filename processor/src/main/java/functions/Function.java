package functions;


import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

import java.util.*;

import functions.face.FaceClient;
import functions.face.rest.Client;
import functions.face.rest.model.FaceAttributeType;
import functions.face.rest.model.URLRequest;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

public class Function {

    @Inject
    Vertx vertx;

    @Inject
    FaceClient faceClient;

    @Funq
    public Uni<Output[]> function(Input input, @Context CloudEvent cloudEvent) {
        return Uni.createFrom().emitter(emitter -> {
            getData(input, cloudEvent, emitter, 100);
        });

    }

    private void getData(Input input, CloudEvent cloudEvent, UniEmitter<? super Output[]> emitter, int retries) {
        if (retries <= 0) {
            emitter.fail(new RuntimeException("Too many fails to call face api."));
        }

        faceClient.detect(input.getUrl())
                .subscribe()
                .with(detectedFaces -> {
                    try {
                        Output[] out = detectedFaces
                                .stream()
                                .map(face -> new Output(input.getChat(), face.getFaceAttributes().getAge(), face.getFaceAttributes().getEmotion()))
                                .toArray(Output[]::new);
                        emitter.complete(out);
                    } catch (Exception e) {
                        emitter.fail(e);
                    }
                }, throwable -> {
                    if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
                        vertx.setTimer(500, dummy -> getData(input, cloudEvent, emitter, retries-1));
                    } else {
                        emitter.fail(throwable);
                    }
                });
    }
}
