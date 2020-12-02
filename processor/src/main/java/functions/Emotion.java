package functions;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Emotion {
    private float anger;
    private float contempt;
    private float disgust;
    private float fear;
    private float happiness;
    private float neutral;
    private float sadness;
    private float surprise;

    public Emotion() {}

    public float getAnger() {
        return this.anger;
    }

    public void setAnger(float value) {
        this.anger = value;
    }

    public float getContempt() {
        return this.contempt;
    }

    public void setContempt(float value) {
        this.contempt = value;
    }

    public float getDisgust() {
       return this.disgust;
    }

    public void setDisgust(float value) {
        this.disgust = value;
    }

    public float getFear() {
       return this.fear;
    }

    public void setFear(float value) {
       this.fear = value;
    }

    public float getHappiness() {
        return this.happiness;
    }

    public void setHappiness(float value) {
        this.happiness = value;
    }

    public float getNeutral() {
        return this.neutral;
    }

    public void setNeutral(float value) {
        this.neutral = value;
    }

    public float getSadness() {
        return this.sadness;
    }

    public void setSadness(float value) {
        this.sadness = value;
    }

    public float getSurprise() {
        return this.surprise;
    }

    public void setSurprise(float value) {
        this.surprise = value;
    }
}
