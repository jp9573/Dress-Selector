package in.co.jaypatel.dressselector.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import in.co.jaypatel.dressselector.Dress;

/**
 * Created by jay on 14/04/18.
 */

@Dao
public interface DressItemDao {

    @Query("SELECT * FROM DressTable")
    List<Dress> getDressItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(Dress listItem);

    @Delete
    int deleteItem(Dress listItem);
}
