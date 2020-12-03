package functions.face.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum  FaceAttributeType {
    AGE("age"),
    GENDER("gender"),
    HEAD_POSE("headPose"),
    SMILE("smile"),
    FACIAL_HAIR("facialHair"),
    GLASSES("glasses"),
    EMOTION("emotion"),
    HAIR("hair"),
    MAKEUP("makeup"),
    OCCLUSION("occlusion"),
    ACCESSORIES("accessories"),
    BLUR("blur"),
    EXPOSURE("exposure"),
    NOISE("noise");

    private String value;

    private FaceAttributeType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FaceAttributeType fromString(String value) {
        FaceAttributeType[] items = values();
        FaceAttributeType[] var2 = items;
        int var3 = items.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            FaceAttributeType item = var2[var4];
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }

        return null;
    }

    @JsonValue
    public String toString() {
        return this.value;
    }
}
