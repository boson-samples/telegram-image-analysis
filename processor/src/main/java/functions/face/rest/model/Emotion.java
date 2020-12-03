package functions.face.rest.model;

public class Emotion {
    private double anger;
    private double contempt;
    private double disgust;
    private double fear;
    private double happiness;
    private double neutral;
    private double sadness;
    private double surprise;

    public Emotion() {}

    public double getAnger() {
        return this.anger;
    }

    public void setAnger(double value) {
        this.anger = value;
    }

    public double getContempt() {
        return this.contempt;
    }

    public void setContempt(double value) {
        this.contempt = value;
    }

    public double getDisgust() {
       return this.disgust;
    }

    public void setDisgust(double value) {
        this.disgust = value;
    }

    public double getFear() {
       return this.fear;
    }

    public void setFear(double value) {
       this.fear = value;
    }

    public double getHappiness() {
        return this.happiness;
    }

    public void setHappiness(double value) {
        this.happiness = value;
    }

    public double getNeutral() {
        return this.neutral;
    }

    public void setNeutral(double value) {
        this.neutral = value;
    }

    public double getSadness() {
        return this.sadness;
    }

    public void setSadness(double value) {
        this.sadness = value;
    }

    public double getSurprise() {
        return this.surprise;
    }

    public void setSurprise(double value) {
        this.surprise = value;
    }
}
