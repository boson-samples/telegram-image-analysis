package functions.face.rest.model;

public class FaceAttributes {
    private double age;
    private Emotion emotion;

    public FaceAttributes() {}

    public double getAge() {
        return this.age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public Emotion getEmotion() {
        return this.emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}