package com.github.theword.event;

import com.google.gson.annotations.SerializedName;

import static com.github.theword.ConfigReader.configMap;

public class FabricEvent {
    @SerializedName("server_name")
    private final String serverName = configMap.get("server_name").toString();

    @SerializedName("event_name")
    private String eventName;

    @SerializedName("post_type")
    private String postType;

    @SerializedName("sub_type")
    private String subType;

    private final int timestamp = (int) (System.currentTimeMillis() / 1000);

    public FabricEvent(String eventName, String postType, String subType) {
        this.eventName = eventName;
        this.postType = postType;
        this.subType = subType;
    }
}