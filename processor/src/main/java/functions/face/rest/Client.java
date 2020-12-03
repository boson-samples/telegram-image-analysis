package functions.face.rest;



import functions.face.rest.model.Face;
import functions.face.rest.model.FaceAttributeType;
import functions.face.rest.model.URLRequest;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/v1.0")
@RegisterRestClient(baseUri = "https://boson.cognitiveservices.azure.com/face/")
public interface Client {

    @POST
    @Path("/detect")
    Uni<List<Face>> detect(URLRequest imageURLRequest,
                           @QueryParam("returnFaceId") boolean returnFaceId,
                           @QueryParam("returnFaceLandmarks") boolean returnFaceLandmarks,
                           @QueryParam("returnFaceAttributes") String returnFaceAttributes,
                           @QueryParam("recognitionModel") String recognitionModel,
                           @QueryParam("returnRecognitionModel") boolean returnRecognitionModel,
                           @QueryParam("detectModel") String detectModel,
                           @HeaderParam("Ocp-Apim-Subscription-Key") String apiKey);
}
