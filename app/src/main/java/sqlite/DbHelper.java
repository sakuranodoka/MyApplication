package sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "SHOP_INFO";
	private static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "InvoiceInfo";
	public static final String COL_ID = "info_id";
	public static final String COL_INVOICE = "info_invoice";
	public static final String COL_LATITUDE = "info_latitude";
	public static final String COL_LONGITUDE = "info_longitude";
	public static final String COL_TIME = "info_time";
	public static final String COL_EMPLOYEE = "info_employee";

	public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME +" ("
				  + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + COL_INVOICE + " TEXT, "
				  + COL_LATITUDE + " TEXT, "
				  + COL_LONGITUDE + " TEXT, "
				  + COL_TIME + " TEXT, "
				  + COL_EMPLOYEE + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void clearRange(SQLiteDatabase db, @NonNull String userName) {
		if(!userName.isEmpty()) {
			db.execSQL("DELETE FROM " + TABLE_NAME +" WHERE " + COL_EMPLOYEE + "='" + userName + "';");
		}
	}
}
