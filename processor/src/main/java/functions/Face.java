package functions;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Face {
    private FaceRectangle faceRectangle;
    private FaceAttributes faceAttributes;

    public Face() {}

    public FaceRectangle getFaceRectangle() {
        return this.faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public FaceAttributes getFaceAttributes() {
        return this.faceAttributes;
    }

    public void setFaceAttributes(FaceAttributes faceAttributes) {
        this.faceAttributes = faceAttributes;
    }
}
