package com.celerstudio.wreelysocial.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Query("SELECT * FROM chat")
    LiveData<List<ChatMessage>> getAllChats();

    @Insert
    void insertChat(ChatMessage... chats);

    @Query("SELECT * FROM chat WHERE sender_id LIKE :senderId and receiver_id LIKE :receiverId")
    LiveData<List<ChatMessage>> findChatBySenderIdandReceiverId(String senderId, String receiverId);

    @Query("SELECT * FROM chat WHERE room_id LIKE :roomId")
    LiveData<List<ChatMessage>> findChatByRoomId(String roomId);

    @Query("SELECT * FROM chat WHERE timestamp LIKE :timestamp")
    ChatMessage findChatBySenderIdAndTimestamp(long timestamp);

    @Query("DELETE from chat")
    void deleteAll();

    @Delete
    void deleteChatMessage(ChatMessage chatMessage);

    @Update
    int updateChat(ChatMessage chatMessage);

}
