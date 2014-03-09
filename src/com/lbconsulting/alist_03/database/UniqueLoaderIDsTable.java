package com.lbconsulting.alist_03.database;

import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lbconsulting.alist_03.database.contentprovider.AListContentProvider;
import com.lbconsulting.alist_03.utilities.MyLog;

public class UniqueLoaderIDsTable {

	// LOCATIONs data table
	public static final String TABLE_UNIQUE_LOADER_IDs = "tblUniqueLoaderIDs";
	public static final String COL_UNIQUE_LOADER_ID = "_id";
	public static final String COL_FRAGMENT_ID = "fragmentID";
	public static final String COL_LOADER_TYPE_ID = "loaderID";
	public static final String COL_DATE_TIME_CREATED = "dateTimeLastCreated";

	public static final String[] PROJECTION_ALL =
	{ COL_UNIQUE_LOADER_ID, COL_FRAGMENT_ID, COL_LOADER_TYPE_ID, COL_DATE_TIME_CREATED };

	public static final String CONTENT_PATH = TABLE_UNIQUE_LOADER_IDs;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_UNIQUE_LOADER_IDs;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_UNIQUE_LOADER_IDs;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_CREATION_DATE = COL_DATE_TIME_CREATED + " ASC";

	// Database creation SQL statements
	private static final String DATATABLE_CREATE = "create table "
			+ TABLE_UNIQUE_LOADER_IDs
			+ " ("
			+ COL_UNIQUE_LOADER_ID + " integer primary key autoincrement, "
			+ COL_FRAGMENT_ID + " integer default 0, "
			+ COL_LOADER_TYPE_ID + " integer default 0, "
			+ COL_DATE_TIME_CREATED + " integer default 0 "
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("LocationsTable", "onCreate: " + TABLE_UNIQUE_LOADER_IDs + " created.");
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIQUE_LOADER_IDs);
		onCreate(database);
		MyLog.w(TABLE_UNIQUE_LOADER_IDs, "Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ". New " + TABLE_UNIQUE_LOADER_IDs + " created.");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static int FetchNextUniqueID(Context context, int fragmentID, int loaderID) {
		int nextUniqueID = -1;

		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(COL_FRAGMENT_ID, fragmentID);
		values.put(COL_LOADER_TYPE_ID, loaderID);
		Calendar now = Calendar.getInstance();
		values.put(COL_DATE_TIME_CREATED, now.getTimeInMillis());

		try {
			Uri nextUniqueIdUri = cr.insert(uri, values);
			if (nextUniqueIdUri != null) {
				nextUniqueID = Integer.parseInt(nextUniqueIdUri.getLastPathSegment());
			}
		} catch (Exception e) {
			MyLog.e("Exception error in UniqueLoaderIDsTable: FetchNextUniqueID. ", e.toString());
		}

		return nextUniqueID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static int getUniqueIdCount(Context context) {
		Cursor cursor = null;
		int count = -1;

		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;
		ContentResolver cr = context.getContentResolver();
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error in UniqueLoaderIDsTable: getUniqueIdCount. ", e.toString());
		}

		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int ReleaseUniqueID(Context context, int uniqueID) {
		int numberOfDeletedRecords = -1;

		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		String where = COL_UNIQUE_LOADER_ID + " = ?";
		String[] selectionArgs = { String.valueOf(uniqueID) };
		numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);

		return numberOfDeletedRecords;
	}

	public static int DeleteAllUniqueIDs(Context context) {
		int numberOfDeletedRecords = -1;
		Uri uri = CONTENT_URI;
		String where = COL_UNIQUE_LOADER_ID + " > 0";
		String selectionArgs[] = null;
		ContentResolver cr = context.getContentResolver();
		numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);

		return numberOfDeletedRecords;
	}

}
