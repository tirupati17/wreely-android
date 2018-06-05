package com.celerstudio.wreelysocial.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.celerstudio.wreelysocial.persistence.ChatMessage;

import java.util.Map;

public class Chat {
    private int id;

    private String userId;

    private String userName;

    private boolean incoming;

    private String message;

    private String senderId;

    private String receiverId;

    private long timestamp;

    private String roomId;

    public Chat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public static ChatMessage cloneToChatMessage(Chat chat) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setIncoming(chat.isIncoming());
        chatMessage.setMessage(chat.getMessage());
        chatMessage.setSenderId(chat.getSenderId());
        chatMessage.setReceiverId(chat.getReceiverId());
        chatMessage.setTimestamp(chat.getTimestamp());
        chatMessage.setUserName(chat.getUserName());
        chatMessage.setRoomId(chat.getRoomId());
        return chatMessage;
    }

    public static Chat map(String firebaseId, Map singleUser) {
        Chat chat = new Chat();
        chat.setUserName((String) singleUser.get("userName"));
        chat.setTimestamp((Long) singleUser.get("timestamp"));
        chat.setMessage((String) singleUser.get("message"));
        chat.setSenderId((String) singleUser.get("senderId"));
        chat.setReceiverId((String) singleUser.get("receiverId"));
        chat.setRoomId((String) singleUser.get("roomId"));
        if (chat.getSenderId().equalsIgnoreCase(firebaseId))
            chat.setIncoming(false);
        else
            chat.setIncoming(true);
        return chat;
    }

}
