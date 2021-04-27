package functions;


import com.microsoft.azure.cognitiveservices.vision.faceapi.FaceAPI;
import com.microsoft.azure.cognitiveservices.vision.faceapi.FaceAPIManager;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.AzureRegions;
import com.microsoft.azure.cognitiveservices.vision.faceapi.models.FaceAttributeType;
import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Function {

    @Inject
    Vertx vertx;

    private static final List<FaceAttributeType> attributes;
    static {
        List<FaceAttributeType> attrs = new ArrayList<>(2);
        attrs.add(FaceAttributeType.AGE);
        attrs.add(FaceAttributeType.EMOTION);
        attributes = Collections.unmodifiableList(attrs);
    }

    @ConfigProperty(name = "OCP_APIM_SUBSCRIPTION_KEY")
    String apiKey;

    @Funq
    @CloudEventMapping(responseType = "telegram.image.processed")
    public Uni<Output> function(Input input, @Context CloudEvent cloudEvent) {
        return Uni.createFrom().emitter(emitter -> {
            getData(input, cloudEvent, emitter, 100);
        });

    }

    private void getData(Input input, CloudEvent cloudEvent, UniEmitter<? super Output> emitter, int retries) {
        if (retries <= 0) {
            emitter.fail(new RuntimeException("Too many fails to call face api."));
        }
        FaceAPI faceAPI = FaceAPIManager.authenticate(AzureRegions.EASTUS, apiKey);
        faceAPI
                .faces()
                .detectWithUrl()
                .withUrl(input.getUrl())
                .withReturnFaceAttributes(attributes)
                .withReturnFaceLandmarks(false)
                .withReturnFaceId(false)
                .executeAsync()
                .subscribe(detectedFaces -> {
                    try {
                        Output.Face[] faces = detectedFaces
                                .stream()
                                .map(face -> new Output.Face(face.faceAttributes().age(), face.faceAttributes().emotion()))
                                .toArray(Output.Face[]::new);
                        System.out.println("Number of faces detected in the image: " + faces.length);
                        emitter.complete(new Output(input.getChat(), faces));

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
