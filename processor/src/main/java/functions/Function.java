package functions;


import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

import java.util.*;

import com.microsoft.azure.cognitiveservices.vision.faceapi.FaceAPI;
import com.microsoft.azure.cognitiveservices.vision.faceapi.FaceAPIManager;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.AzureRegions;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FaceAttributeType;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Vertx;

import javax.inject.Inject;

public class Function {

    private static final String API_KEY = "dcc086604d47489e86bd6d216aa5b09b";

    @Inject
    Vertx vertx;

    private static final List<FaceAttributeType> attributes;
    static {
        List<FaceAttributeType> attrs = new ArrayList<>(2);
        attrs.add(FaceAttributeType.AGE);
        attrs.add(FaceAttributeType.EMOTION);
        attributes = Collections.unmodifiableList(attrs);
    }



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
        FaceAPI faceAPI = FaceAPIManager.authenticate("https://boson.cognitiveservices.azure.com/face/v1.0", API_KEY);
        faceAPI
                .withAzureRegion(AzureRegions.EASTUS) // not really used but has to be set anyway :(
                .faces()
                .detectWithUrl()
                .withUrl(input.getUrl())
                .withReturnFaceAttributes(attributes)
                .withReturnFaceLandmarks(false)
                .withReturnFaceId(false)
                .executeAsync()
                .subscribe(detectedFaces -> {
                    try {
                        Output[] out = detectedFaces
                                .stream()
                                .map(face -> new Output(input.getChat(), face.faceAttributes().age(), face.faceAttributes().emotion()))
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
