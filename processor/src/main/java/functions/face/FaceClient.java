package functions.face;

import functions.face.rest.Client;
import functions.face.rest.model.Face;
import functions.face.rest.model.FaceAttributeType;
import functions.face.rest.model.URLRequest;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class FaceClient {

    private static final String API_KEY = "dcc086604d47489e86bd6d216aa5b09b";

    @Inject
    @RestClient
    Client client;

    public Uni<List<Face>> detect(String url) {
        return client.detect(new URLRequest(url),
                false,
                false,
                "age,emotion",
                "recognition_03",
                false,
                "detection_01",
                API_KEY);
    }
}
