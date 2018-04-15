package in.co.jaypatel.dressselector;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;

import java.io.Serializable;

@Entity(tableName = "DressTable")
public class Dress implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "topCloth")
    private String topCloth;

    @ColumnInfo(name = "bottomCloth")
    private String bottomCloth;

    @ColumnInfo(name = "isFavourite")
    private boolean isFavourite;

    public Dress() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopCloth() {
        return topCloth;
    }

    public void setTopCloth(String topCloth) {
        this.topCloth = topCloth;
    }

    public String getBottomCloth() {
        return bottomCloth;
    }

    public void setBottomCloth(String bottomCloth) {
        this.bottomCloth = bottomCloth;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
