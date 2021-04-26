package functions;

import com.microsoft.azure.cognitiveservices.vision.faceapi.models.Emotion;

import java.util.Arrays;

public class Output {
    private Face[] faces;
    private String chat;

    public Output() {}

    public Output(String chat, Face[] faces) {
        this.chat = chat;
        this.faces = faces;
    }

    public Face[] getFaces() {
        return faces;
    }

    public void setFaces(Face[] faces) {
        this.faces = faces;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "Output{" +
                "faces=" + Arrays.toString(faces) +
                ", chat='" + chat + '\'' +
                '}';
    }

    public static class Face {
        private Double age;
        private Emotion emotion;

        public Face() {
        }

        public Face(Double age, Emotion emotion) {
            this.age = age;
            this.emotion = emotion;
        }

        public Double getAge() {
            return age;
        }

        public void setAge(Double age) {
            this.age = age;
        }

        public Emotion getEmotion() {
            return emotion;
        }

        public void setEmotion(Emotion emotion) {
            this.emotion = emotion;
        }

        @Override
        public String toString() {
            return "Face{" +
                    "age=" + age +
                    ", emotion=" + emotion +
                    '}';
        }
    }
}
