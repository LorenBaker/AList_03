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

public class GroupsTable {

	// Groups data table
	public static final String TABLE_GROUPS = "tblGroups";
	public static final String COL_GROUP_ID = "_id";
	public static final String COL_GROUP_NAME = "groupName";
	public static final String COL_LIST_ID = "listID";
	public static final String COL_CHECKED = "groupChecked";

	public static final String[] PROJECTION_ALL = { COL_GROUP_ID, COL_GROUP_NAME, COL_LIST_ID, COL_CHECKED };

	public static final String CONTENT_PATH = TABLE_GROUPS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_GROUPS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_GROUPS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_GROUP = COL_GROUP_NAME + " ASC";

	// Database creation SQL statements
	private static final String DATATABLE_CREATE = "create table "
			+ TABLE_GROUPS
			+ " ("
			+ COL_GROUP_ID + " integer primary key autoincrement, "
			+ COL_GROUP_NAME + " text collate nocase, "
			+ COL_LIST_ID + " integer not null references "
			+ ListsTable.TABLE_LISTS + " (" + ListsTable.COL_LIST_ID + ") default 1, "
			+ COL_CHECKED + " integer default 0 "
			+ ");";

	private static String defalutGroupValue = "[No Group]";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("GroupsTable", "onCreate: " + TABLE_GROUPS + " created.");
		ArrayList<String> sqlStatements = new ArrayList<String>();
		String insertProjection = "insert into "
				+ TABLE_GROUPS
				+ " ("
				+ COL_GROUP_ID + ", "
				+ COL_GROUP_NAME + ", "
				+ COL_LIST_ID + ") VALUES ";

		// Default Group
		sqlStatements.add(insertProjection + "(NULL, '" + defalutGroupValue + "', 1)");

		// Groups for Groceries List (2)
		/*sqlStatements.add(insertProjection + "(NULL, 'Aisle 1', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Aisle 2', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Aisle 3', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Aisle 4', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Aisle 5', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Produce', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Dairy', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Meats', 2)");
		sqlStatements.add(insertProjection + "(NULL, 'Bakery', 2)");

		// Groups for ToDo List (3)
		sqlStatements.add(insertProjection + "(NULL, 'Group 4', 3)");
		sqlStatements.add(insertProjection + "(NULL, 'Group 3', 3)");
		sqlStatements.add(insertProjection + "(NULL, 'Group 2', 3)");
		sqlStatements.add(insertProjection + "(NULL, 'Group 1', 3)");*/

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.w(TABLE_GROUPS, "Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ". Adding groupChecked column.");

		// ALTER TABLE tblGroups ADD COLUMN groupChecked integer default 0
		database.execSQL("ALTER TABLE " + TABLE_GROUPS + " ADD COLUMN " + COL_CHECKED + " integer default 0");

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static long CreateNewGroup(Context context, long listID, String groupName) {
		long newGroupID = -1;
		if (listID > 1) {
			// verify that the item does not already exist in the table
			@SuppressWarnings("resource")
			Cursor cursor = getGroup(context, listID, groupName);
			if (cursor != null && cursor.getCount() > 0) {
				// the item exists in the table ... so return its id
				cursor.moveToFirst();
				newGroupID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_GROUP_ID));
				cursor.close();
			} else {
				// group does not exist in the table ... so add it	
				if (groupName != null) {
					groupName = groupName.trim();
					if (!groupName.isEmpty()) {
						try {
							ContentResolver cr = context.getContentResolver();
							Uri uri = CONTENT_URI;
							ContentValues values = new ContentValues();
							values.put(COL_LIST_ID, listID);
							values.put(COL_GROUP_NAME, groupName);
							Uri newListUri = cr.insert(uri, values);
							if (newListUri != null) {
								newGroupID = Long.parseLong(newListUri.getLastPathSegment());
							}
						} catch (Exception e) {
							MyLog.e("Exception error in CreateNewGroup. ", e.toString());
						}
					} else {
						MyLog.e("GroupsTable", "Error in CreateNewGroup; groupName is Empty!");
					}
				} else {
					MyLog.e("GroupsTable", "Error in CreateNewGroup; groupName is Null!");
				}
			}
		}
		return newGroupID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Cursor getGroup(Context context, long groupID) {
		Cursor cursor = null;
		if (groupID > 0) {
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(groupID));
			String[] projection = PROJECTION_ALL;
			String selection = null;
			String selectionArgs[] = null;
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: getGroup. ", e.toString());
			}
		}
		return cursor;
	}

	public static Cursor getGroup(Context context, long listID, String groupName) {
		Cursor cursor = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_GROUP_NAME + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), groupName };
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: getGroup. ", e.toString());
			}
		}
		return cursor;
	}

	public static CursorLoader getAllGroupsInListIncludeDefault(Context context, long listID, String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? OR " + COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(1), String.valueOf(listID) };
			try {
				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: getAllGroupsInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	public static CursorLoader getAllGroupsInList(Context context, long listID, String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID) };
			try {
				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: getAllGroupsInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	public static Cursor getAllCheckedGroups(Context context, long listID) {
		Cursor cursor = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_CHECKED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(1) };
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: getAllCheckedGroups. ", e.toString());
			}
		}
		return cursor;
	}

	public static String getGroupName(Context context, long groupID) {
		String groupName = "";
		Cursor cursor = getGroup(context, groupID);
		if (cursor != null) {
			cursor.moveToFirst();
			groupName = cursor.getString(cursor.getColumnIndexOrThrow(COL_GROUP_NAME));
			cursor.close();
		}
		return groupName;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static int UpdateGroupName(Context context, long groupID, String groupName) {
		int numberOfUpdatedRecords = -1;
		// cannot update the default group with ID=1
		if (groupID > 1) {
			Uri uri = CONTENT_URI;
			String selection = COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(groupID) };
			ContentResolver cr = context.getContentResolver();

			ContentValues values = new ContentValues();
			values.put(COL_GROUP_NAME, groupName.trim());
			numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("GroupsTable: UpdateGroupName", "The number of records updated does not equal 1!");
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int UnCheckAllCheckedGroups(Context context, long listID) {
		int numberOfUpdatedRecords = -1;
		// cannot update the default group with ID=1
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String selection = COL_LIST_ID + " = ? AND " + COL_CHECKED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(1) };
			ContentResolver cr = context.getContentResolver();

			ContentValues values = new ContentValues();
			values.put(COL_CHECKED, 0);
			numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
		}
		return numberOfUpdatedRecords;
	}

	public static void ToggleCheckBox(Context context, long groupID) {
		Cursor cursor = getGroup(context, groupID);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndexOrThrow(COL_CHECKED);
			int checkIntValue = cursor.getInt(columnIndex);
			boolean checkValue = AListUtilities.intToBoolean(checkIntValue);
			cursor.close();
			CheckItem(context, groupID, !checkValue);
		}
	}

	public static int CheckItem(Context context, long groupID, boolean checked) {
		int numberOfUpdatedRecords = -1;
		if (groupID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_GROUP_ID + " = ?";
				String[] whereArgs = { String.valueOf(groupID) };

				ContentValues values = new ContentValues();
				int checkedValue = AListUtilities.boolToInt(checked);
				values.put(COL_CHECKED, checkedValue);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in GroupsTable: CheckItem. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int DeleteGroup(Context context, long groupID) {
		int numberOfDeletedRecords = -1;
		if (groupID > 1) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_GROUP_ID + " = ?";
			String[] selectionArgs = { String.valueOf(groupID) };
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllGroupsInList(Context context, long listID) {
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

	public static int ApplyLocationToCheckedGroups(Context context,
			long listID, long storeID, long locationID) {
		int numberOfCheckedGroups = -1;

		// get all of the checked groups
		Cursor allCheckedGroupsCursor = getAllCheckedGroups(context, listID);
		if (allCheckedGroupsCursor != null) {
			if (allCheckedGroupsCursor.getCount() > 0) {
				allCheckedGroupsCursor.moveToPosition(-1);
				long groupID = -1;
				while (allCheckedGroupsCursor.moveToNext()) {
					groupID = allCheckedGroupsCursor
							.getLong(allCheckedGroupsCursor.getColumnIndexOrThrow(COL_GROUP_ID));
					BridgeTable.SetRow(context, listID, storeID, groupID, locationID);
				}
			}
			allCheckedGroupsCursor.close();
			// un-check all checked groups
			numberOfCheckedGroups = UnCheckAllCheckedGroups(context, listID);
		}
		return numberOfCheckedGroups;
	}

}
