package db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by android on 17.11.16..
 */
@DatabaseTable(tableName = Film.TABLE_NAME_FILMS)
public class Film {

    public static final String TABLE_NAME_FILMS = "films";

    public static final String FIELD_NAME_ACTOR = "glumac";

    public static final String FIELD_NAME_FILMNAME = "nazivFilma";

    public static final String FIELD_NAME_ID     = "id";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_ACTOR, foreign = true, foreignAutoRefresh = true)
    private Glumac mGlumac;

    @DatabaseField(columnName = FIELD_NAME_FILMNAME)
    private String movieDescription;

    public Film() {

    }

    public Film(int id, String name) {
        this.id = id;
        this.movieDescription = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Glumac getmGlumac() {
        return mGlumac;
    }

    public void setmGlumac(Glumac mGlumac) {
        this.mGlumac = mGlumac;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }
}
