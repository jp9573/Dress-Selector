package in.co.jaypatel.dressselector.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import in.co.jaypatel.dressselector.Dress;

/**
 * Created by jay on 14/04/18.
 */

@Database(entities = {Dress.class}, version = 1, exportSchema = false)
public abstract class DressItemDatabase extends RoomDatabase {

    private static DressItemDatabase INSTANCE;

    public abstract DressItemDao dressItemDao();

    public static DressItemDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                            DressItemDatabase.class,
                                            "users-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
