package functions;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class FaceAttributes {
    private float age;
    private Emotion emotion;

    public FaceAttributes() {}

    public float getAge() {
        return this.age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public Emotion getEmotion() {
        return this.emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}