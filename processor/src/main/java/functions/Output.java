package functions;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Emotion;

public class Output {
    private String chat;
    private Double age;
    private Emotion emotion;

    public Output() {}


    public Output(String chat, Double age, Emotion emotion) {
        this.chat = chat;
        this.age = age;
        this.emotion = emotion;
    }

    public String getChat() {
        return this.chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }


    public Double getAge() {
        return this.age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Emotion getEmotion() {
        return this.emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}
