package com.lbconsulting.alist_03.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lbconsulting.alist_03.database.contentprovider.AListContentProvider;
import com.lbconsulting.alist_03.utilities.MyLog;

public class BridgeTable {

	// Items data table
	public static final String TABLE_BRIDGE = "tblBridge";
	public static final String COL_BRIDGE_ID = "_id";
	public static final String COL_LIST_ID = "listID";
	public static final String COL_GROUP_ID = "groupID";
	public static final String COL_STORE_ID = "storeID";
	public static final String COL_LOCATION_ID = "locationID";

	public static final String[] PROJECTION_ALL = { COL_BRIDGE_ID, COL_LIST_ID,
			COL_GROUP_ID, COL_STORE_ID, COL_LOCATION_ID };

	public static final String CONTENT_PATH = TABLE_BRIDGE;

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_BRIDGE;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_BRIDGE;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	// Database creation SQL statements
	private static final String DATATABLE_CREATE =
			"create table " + TABLE_BRIDGE
					+ " ("
					+ COL_BRIDGE_ID + " integer primary key autoincrement, "

					+ COL_LIST_ID + " integer not null references "
					+ ListsTable.TABLE_LISTS + " (" + ListsTable.COL_LIST_ID + ") default 1, "

					+ COL_GROUP_ID + " integer not null references "
					+ GroupsTable.TABLE_GROUPS + " (" + GroupsTable.COL_GROUP_ID + ") default 1, "

					+ COL_STORE_ID + " integer not null references "
					+ StoresTable.TABLE_STORES + " (" + StoresTable.COL_STORE_ID + ") default 1, "

					+ COL_LOCATION_ID + " integer not null references "
					+ LocationsTable.TABLE_LOCATIONS + " (" + LocationsTable.COL_LOCATION_ID + ") default 1 "

					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("ItemsTable", "onCreate: " + TABLE_BRIDGE + " created.");
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_BRIDGE);
		onCreate(database);
		MyLog.w(TABLE_BRIDGE, "Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ". New " + TABLE_BRIDGE + " created.");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method creates a new item in the provided list.
	 * 
	 * @param context
	 * @param listID
	 * @param itemName
	 * @return Returns the new item's ID.
	 */
	public static long CreateNewBridgeRow(Context context, long listID, long storeID, long groupID, long locationID) {
		long newBridgeRowID = -1;
		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(COL_LIST_ID, listID);
		values.put(COL_STORE_ID, storeID);
		values.put(COL_GROUP_ID, groupID);
		values.put(COL_LOCATION_ID, locationID);
		try {
			Uri newBridgeRowUri = cr.insert(uri, values);
			if (newBridgeRowUri != null) {
				newBridgeRowID = Long.parseLong(newBridgeRowUri.getLastPathSegment());
			}
		} catch (Exception e) {
			MyLog.e("Exception error in CreateNewBridgeRow. ", e.toString());
		}
		return newBridgeRowID;
	}

	public static long CreateNewBridgeRow(Context context, long listID, long storeID, long groupID) {
		long newBridgeRowID = -1;
		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(COL_LIST_ID, listID);
		values.put(COL_STORE_ID, storeID);
		values.put(COL_GROUP_ID, groupID);
		try {
			Uri newBridgeRowUri = cr.insert(uri, values);
			if (newBridgeRowUri != null) {
				newBridgeRowID = Long.parseLong(newBridgeRowUri.getLastPathSegment());
			}
		} catch (Exception e) {
			MyLog.e("Exception error in CreateNewBridgeRow. ", e.toString());
		}
		return newBridgeRowID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static long getBridgeTableRowID(Context context, long listID, long storeID, long groupID) {
		long bridgeTableRowID = -1;
		Cursor cursor = null;
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = COL_LIST_ID + " = ? AND " + COL_STORE_ID + " = ? AND " + COL_GROUP_ID + " = ?";
		String selectionArgs[] = new String[]
		{ String.valueOf(listID), String.valueOf(storeID), String.valueOf(groupID) };
		String sortOrder = null;
		ContentResolver cr = context.getContentResolver();
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error in ItemsTable: getItem. ", e.toString());
		}

		if (cursor == null || cursor.getCount() == 0) {
			// bridgeTableRow does not exist... so create one		
			bridgeTableRowID = CreateNewBridgeRow(context, listID, storeID, groupID);
		} else {
			// bridgeTable row exists ... so return its ID
			cursor.moveToFirst();
			bridgeTableRowID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_BRIDGE_ID));
		}

		if (cursor != null) {
			cursor.close();
		}
		return bridgeTableRowID;
	}

	public static Cursor getBridgeTableRow(Context context, long bridgeRowID) {
		Cursor cursor = null;
		if (bridgeRowID > 0) {
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(bridgeRowID));
			String[] projection = PROJECTION_ALL;
			String selection = null;
			String selectionArgs[] = null;
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in ItemsTable: getItem. ", e.toString());
			}
		}
		return cursor;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int UpdateItemFieldValues(Context context, long bridgeRowID, ContentValues newFieldValues) {
		int numberOfUpdatedRecords = -1;
		ContentResolver cr = context.getContentResolver();
		Uri itemUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(bridgeRowID));
		String selection = null;
		String[] selectionArgs = null;
		numberOfUpdatedRecords = cr.update(itemUri, newFieldValues, selection, selectionArgs);
		return numberOfUpdatedRecords;
	}

	public static void SetRow(Context context, long listID, long storeID, long groupID, long locationID) {
		long bridgeTableRowID = getBridgeTableRowID(context, listID, storeID, groupID);
		if (bridgeTableRowID > 0) {
			ContentValues values = new ContentValues();
			values.put(COL_LOCATION_ID, locationID);
			UpdateItemFieldValues(context, bridgeTableRowID, values);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static int DeleteBridgeRow(Context context, long bridgeRowID) {
		int numberOfDeletedRecords = -1;
		if (bridgeRowID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(bridgeRowID));
			String where = null;
			String[] selectionArgs = null;
			cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllBridgeRowsInList(Context context, long listID) {
		int numberOfDeletedRecords = -1;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllBridgeRowsInGroup(Context context, long groupID) {
		int numberOfDeletedRecords = -1;
		if (groupID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_GROUP_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(groupID) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllBridgeRowsWithStore(Context context, long storeID) {
		int numberOfDeletedRecords = -1;
		if (storeID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_STORE_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(storeID) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllBridgeRowsWithLocation(Context context, long locationID) {
		int numberOfDeletedRecords = -1;
		if (locationID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_LOCATION_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(locationID) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

}
