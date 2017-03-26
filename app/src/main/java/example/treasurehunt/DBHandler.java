package example.treasurehunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL on 6/14/2016.
 */
public class DBHandler extends SQLiteOpenHelper
{
    Context context;
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "treasurehunt.db";
    static final String COLOMN_NAME = "name";
    static final String COLOMN_EMAIL = "email";
    static final String TABLE_NAME = "users";


    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +TABLE_NAME+ "(" + COLOMN_EMAIL + " TEXT NOT NULL," +COLOMN_NAME +
                " TEXT NOT NULL "+" );";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }
    public long add(String email, String name ){
        ContentValues newValues = new ContentValues();
        newValues.put(COLOMN_EMAIL, email);
        newValues.put(COLOMN_NAME, name);
        SQLiteDatabase db = getWritableDatabase();
        long test = db.insert(TABLE_NAME, null, newValues);
        return  test;

    }
}
