package functions;

public class Output {
    private String chat;
    private String age;
    private String emotion;

    public Output() {}

    public Output(String chat, String age, String emotion) {
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

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmotion() {
        return this.emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}
