package com.lbconsulting.alist_03.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.lbconsulting.alist_03.database.contentprovider.AListContentProvider;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class StoresTable {
	// Lists data table
	public static final String TABLE_STORES = "tblStores";
	public static final String COL_STORE_ID = "_id"; //0
	public static final String COL_STORE_NAME = "storeName"; //1
	public static final String COL_LIST_ID = "listTitleID"; //2
	public static final String COL_STREET1 = "street1"; //3
	public static final String COL_STREET2 = "street2"; //4
	public static final String COL_CITY = "city"; //5
	public static final String COL_STATE = "state"; //6
	public static final String COL_ZIP = "zip"; //7
	public static final String COL_GPS_LATITUDE = "gpsLatitude"; //8
	public static final String COL_GPS_LONGITUDE = "gpsLongitude"; //9
	public static final String COL_WEBSITE_URL = "websiteURL"; //10
	public static final String COL_PHONE_NUMBER = "phoneNumber"; //11

	public static final String[] PROJECTION_ALL = { COL_STORE_ID, COL_STORE_NAME, COL_LIST_ID,
			COL_STREET1, COL_STREET2, COL_CITY, COL_STATE, COL_ZIP,
			COL_GPS_LATITUDE, COL_GPS_LONGITUDE, COL_WEBSITE_URL, COL_PHONE_NUMBER
	};

	public static final String CONTENT_PATH = TABLE_STORES;
	public static final String CONTENT_LIST_WITH_GROUP = "listWithGroup";

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_STORES;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_STORES;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);
	public static final Uri LIST_WITH_group_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/"
			+ CONTENT_LIST_WITH_GROUP);

	public static final String SORT_ORDER_STORE_NAME = COL_STORE_NAME + " ASC";
	public static final String SORT_ORDER_CITY = COL_CITY + " ASC";
	public static final String SORT_ORDER_STATE = COL_STATE + " ASC";
	public static final String SORT_ORDER_ZIP = COL_ZIP + " ASC";

	// Database creation SQL statements
	private static final String DATATABLE_CREATE =
			"create table " + TABLE_STORES
					+ " ("
					+ COL_STORE_ID + " integer primary key autoincrement, "
					+ COL_STORE_NAME + " text collate nocase, "
					+ COL_LIST_ID + " integer not null references "
					+ ListsTable.TABLE_LISTS + " (" + ListsTable.COL_LIST_ID + "), "
					+ COL_STREET1 + " text, "
					+ COL_STREET2 + " text, "
					+ COL_CITY + " text collate nocase, "
					+ COL_STATE + " text collate nocase, "
					+ COL_ZIP + " text collate nocase, "
					+ COL_GPS_LATITUDE + " text, "
					+ COL_GPS_LONGITUDE + " text, "
					+ COL_WEBSITE_URL + " text, "
					+ COL_PHONE_NUMBER + " text"
					+ ");";

	private static String defalutStoreValue = "[No Store]";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("StoresTable", "onCreate: " + TABLE_STORES + " created.");

		String insertProjection = "insert into "
				+ TABLE_STORES
				+ " ("
				+ COL_STORE_ID + ", "
				+ COL_LIST_ID + ", "
				+ COL_STORE_NAME + ", "
				+ COL_CITY + ", "
				+ COL_STATE
				+ ") VALUES ";

		ArrayList<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add(insertProjection + "(NULL,1, '" + defalutStoreValue + "', '', '')");

		// Stores for Groceries List (2)
		/*sqlStatements.add(insertProjection + "(NULL,2, 'Safeway-Factoria', 'Bellevue', 'WA')");
		sqlStatements.add(insertProjection + "(NULL,2, 'QFC-Factoria', 'Bellevue', 'WA')");
		sqlStatements.add(insertProjection + "(NULL,2, 'QFC-Issaquah', 'Bellevue', 'WA')");
		sqlStatements.add(insertProjection + "(NULL,2, 'Albertson-Eastgate', 'Bellevue', 'WA')");

		// Stores for ToDo List (3)
		sqlStatements.add(insertProjection + "(NULL,3, 'ToDo Store 1', 'Anywhere 1', 'OR')");
		sqlStatements.add(insertProjection + "(NULL,3, 'ToDo Store 2', 'Anywhere 2', 'WA')");
		sqlStatements.add(insertProjection + "(NULL,3, 'ToDo Store 3', 'Anywhere 3', 'CA')");
		sqlStatements.add(insertProjection + "(NULL,3, 'ToDo Store 4', 'Anywhere 4', 'NY')");
		sqlStatements.add(insertProjection + "(NULL,3, 'ToDo Store 5', 'Anywhere 5', 'UT')");*/

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.w(TABLE_STORES, ": Upgrading database from version " + oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
		onCreate(database);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static long CreateNewStore(Context context, long listID, String storeName) {
		long newStoreID = -1;
		if (listID > 1) {
			// verify that the item does not already exist in the table
			@SuppressWarnings("resource")
			Cursor cursor = getStore(context, listID, storeName);
			if (cursor != null && cursor.getCount() > 0) {
				// the item exists in the table ... so return its id
				cursor.moveToFirst();
				newStoreID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_STORE_ID));
				cursor.close();
			} else {
				// item does not exist in the table ... so add it	
				if (storeName != null) {
					storeName = storeName.trim();
					if (!storeName.isEmpty()) {
						try {
							ContentResolver cr = context.getContentResolver();
							Uri uri = CONTENT_URI;
							ContentValues values = new ContentValues();
							values.put(COL_LIST_ID, listID);
							values.put(COL_STORE_NAME, storeName);
							Uri newListUri = cr.insert(uri, values);
							if (newListUri != null) {
								newStoreID = Long.parseLong(newListUri.getLastPathSegment());
							}
						} catch (Exception e) {
							MyLog.e("Exception error in CreateNewStore. ", e.toString());
						}

					} else {
						MyLog.e("StoresTable", "Error in CreateNewStore; storeName is Empty!");
					}
				} else {
					MyLog.e("StoresTable", "Error in CreateNewStore; storeName is Null!");
				}
			}
		}
		return newStoreID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Cursor getStore(Context context, long storeID) {
		Cursor cursor = null;
		if (storeID > 0) {
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(storeID));
			String[] projection = PROJECTION_ALL;
			String selection = null;
			String selectionArgs[] = null;
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in StoresTable: getStore. ", e.toString());
			}
		}
		return cursor;
	}

	public static Cursor getStore(Context context, long listID, String storeName) {
		Cursor cursor = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_STORE_NAME + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), storeName };
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in StoresTable: getStore. ", e.toString());
			}
		}
		return cursor;
	}

	public static CursorLoader getAllStoresInList(Context context, long listID,
			String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? OR " + COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(1), String.valueOf(listID) };
			/*ContentResolver cr = context.getContentResolver();*/
			try {

				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
				/*cursorLoader = cr.query(uri, projection, selection, selectionArgs, sortOrder);*/
			} catch (Exception e) {
				MyLog.e("Exception error in StoresTable: getAllStoresInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	public static String getStoreDisplayName(Context context, long storeID) {
		String displayName = "";
		Cursor cursor = getStore(context, storeID);
		if (cursor != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(cursor.getString(cursor.getColumnIndexOrThrow(COL_STORE_NAME)));
			sb.append(", ");
			sb.append(cursor.getString(cursor.getColumnIndexOrThrow(COL_CITY)));
			sb.append(", ");
			sb.append(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATE)));
			cursor.close();
		}
		return displayName;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int UpdateAllStoreFields(
			Context context,
			long storeID,
			String storeName,
			String street1,
			String street2,
			String city,
			String state,
			String zip,
			String gpsLatitude,
			String gpsLongitude,
			String websiteURL,
			String phoneNumber
			) {
		int numberOfUpdatedRecords = -1;
		if (storeID > 1) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(storeID));
				String where = null;
				String[] whereArgs = null;

				if (storeName == null) {
					storeName = "";
				}
				if (street1 == null) {
					street1 = "";
				}
				if (street2 == null) {
					street2 = "";
				}
				if (city == null) {
					city = "";
				}
				if (state == null) {
					state = "";
				}
				if (zip == null) {
					zip = "";
				}
				if (gpsLatitude == null) {
					gpsLatitude = "";
				}
				if (gpsLongitude == null) {
					gpsLongitude = "";
				}
				if (websiteURL == null) {
					websiteURL = "";
				}
				if (phoneNumber == null) {
					phoneNumber = "";
				}

				ContentValues values = new ContentValues();
				values.put(COL_STORE_NAME, storeName.trim());
				values.put(COL_STREET1, street1.trim());
				values.put(COL_STREET2, street2.trim());
				values.put(COL_CITY, city.trim());
				values.put(COL_STATE, state.trim());
				values.put(COL_ZIP, zip.trim());
				values.put(COL_GPS_LATITUDE, gpsLatitude.trim());
				values.put(COL_GPS_LONGITUDE, gpsLongitude.trim());
				values.put(COL_WEBSITE_URL, websiteURL.trim());
				values.put(COL_PHONE_NUMBER, phoneNumber.trim());

				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in StoresTable: UpdateAllStoreFields. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static String getStoreField(Context context, long storeID, String ColumnName) {
		String result = "";
		Cursor cursor = getStore(context, storeID);

		if (cursor != null) {
			cursor.moveToFirst();
			int position = cursor.getColumnIndex(ColumnName);
			if (position != -1) {
				switch (position) {
				case 1:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
					result = cursor.getString(position);
					break;

				default:
					break;
				}
			} else {
				// invalid ColumnName
				MyLog.e("StoresTable: getStoreField", "ColumnName " + "\"" + ColumnName + "\"" + " is not valid!");
			}
			cursor.close();
		}
		return result;
	}

	public static int setStoreField(Context context, long storeID, String ColumnName, String storeFieldValue) {
		int numberOfUpdatedRecords = -1;
		if (storeID > 1) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(storeID));
			ContentValues values = new ContentValues();
			values.put(ColumnName, storeFieldValue);
			String selection = null;
			String[] selectionArgs = null;
			try {
				numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
				if (numberOfUpdatedRecords != 1) {
					MyLog.e("StoresTable: setStoreField", "The number of updated Store records does not equal 1!");
				}
			} catch (Exception e) {
				MyLog.e("Exception error in StoresTable: setStoreField. ", e.toString());
			}

		}
		return numberOfUpdatedRecords;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int DeleteStore(Context context, long storeID) {
		int numberOfDeletedRecords = -1;
		if (storeID > 1) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_STORE_ID + " = ?";
			String[] selectionArgs = { String.valueOf(storeID) };
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllStoresInList(Context context, long listID) {
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

}
