package com.celerstudio.wreelysocial.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class WreelyDatabase extends RoomDatabase {

    private static WreelyDatabase INSTANCE;

    public static WreelyDatabase getInstance(Context context, String databaseName) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, WreelyDatabase.class, databaseName)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public abstract ChatMessageDao chatMessageDao();
}
