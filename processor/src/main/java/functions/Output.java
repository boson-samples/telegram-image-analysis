package functions;

public class Output {
    private String chat;
    private float age;
    private Emotion emotion;

    public Output() {}

    public Output(String chat, float age, Emotion emotion) {
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
