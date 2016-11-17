package db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by android on 17.11.16..
 */
@DatabaseTable(tableName = Glumac.TABLE_NAME_ACTORS)
public class Glumac {

    public static final String TABLE_NAME_ACTORS = "actors";

    public static final String FIELD_NAME_NAME   = "name";
    public static final String FIELD_NAME_LASTNAME   = "lastName";
    public static final String FIELD_NAME_BIOGRAPHY   = "biography";
    public static final String FIELD_NAME_OCENA   = "biography";
    public static final String FIELD_NAME_DATUM   = "datum";
    public static final String FIELD_NAME_FILMS= "filmovi";


    public static final String FIELD_NAME_ID     = "id";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_LASTNAME)
    private String mLastName;

    @DatabaseField(columnName = FIELD_NAME_BIOGRAPHY)
    private String mbiography;

    @DatabaseField(columnName = FIELD_NAME_OCENA)
    private int mocena;

    @DatabaseField(columnName = FIELD_NAME_DATUM)
    private Date mDatum;

    // One-to-many
    @ForeignCollectionField(columnName = FIELD_NAME_FILMS, eager = true)
    private ForeignCollection<Film> mFilms;

    public Glumac() {

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getMbiography() {
        return mbiography;
    }

    public void setMbiography(String mbiography) {
        this.mbiography = mbiography;
    }

    public int getMocena() {
        return mocena;
    }

    public void setMocena(int mocena) {
        this.mocena = mocena;
    }

    public Date getmDatum() {
        return mDatum;
    }

    public void setmDatum(Date mDatum) {
        this.mDatum = mDatum;
    }

    public ForeignCollection<Film> getmFilms() {
        return mFilms;
    }

    public void setmFilms(ForeignCollection<Film> mFilms) {
        this.mFilms = mFilms;
    }
}
