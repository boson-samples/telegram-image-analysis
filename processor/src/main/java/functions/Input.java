package functions;

public class Input {
    private String url;
    private String chat;

    public Input() {}

    public Input(String url, String chat) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChat() {
        return this.chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
