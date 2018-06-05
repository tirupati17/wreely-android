package com.celerstudio.wreelysocial.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class WreelyDataViewModel extends AndroidViewModel {

    private LiveData<List<ChatMessage>> chatMessages;

    private WreelyDatabase jubiAIChatBotDatabase;

    public WreelyDataViewModel(@NonNull Application application) {
        super(application);
        jubiAIChatBotDatabase = DatabaseUtils.getDatabase(this.getApplication());
    }

    public LiveData<List<ChatMessage>> findChatByRoomId(String roomId) {
        chatMessages = jubiAIChatBotDatabase.chatMessageDao().findChatByRoomId(roomId);
        return chatMessages;
    }

}
