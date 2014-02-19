package com.lbconsulting.alist_03.database;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lbconsulting.alist_03.database.contentprovider.AListContentProvider;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsTable {

	// MasterListItems data table
	public static final String TABLE_LISTS = "tblLists";
	public static final String COL_LIST_ID = "_id";//0
	public static final String COL_LIST_TITLE = "listTitle";//1
	public static final String COL_STORE_ID = "storeID";//2
	// List Settings
	public static final String COL_SHOW_GROUPS_IN_LISTS_FRAGMENT = "showGroupsInListsFragment";//3
	public static final String COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT = "showGroupsInMasterListFragment";//4
	public static final String COL_SHOW_STORES = "showStores";//5

	public static final String COL_DELETE_NOTE_UPON_DESELECTING_ITEM = "deleteNoteUponDeslectingItem";//6

	public static final String COL_LIST_SORT_ORDER = "listSortOrder";//7
	public static final String COL_MASTER_LIST_SORT_ORDER = "masterListSortOrder";//8

	public static final String COL_TITLE_BACKGROUND_COLOR = "titleBackgroundColor";//9
	public static final String COL_TITLE_TEXT_COLOR = "titleTextColor";//10

	public static final String COL_SEPARATOR_BACKGROUND_COLOR = "separatorBackgroundColor";//11
	public static final String COL_SEPARATOR_TEXT_COLOR = "separatorTextColor";//12

	public static final String COL_LIST_BACKGROUND_COLOR = "listBackgroundColor";//13
	public static final String COL_ITEM_NORMAL_TEXT_COLOR = "itemNormalTextColor";//14
	public static final String COL_ITEM_STRIKEOUT_TEXT_COLOR = "itemStrikeoutTextColor";//15

	public static final String COL_MASTER_LIST_BACKGROUND_COLOR = "masterListBackgroundColor";//16
	public static final String COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR = "masterListItemNormalTextColor";//17
	public static final String COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR = "masterListItemStrikeoutTextColor";//18

	public static final String COL_LISTVIEW_FIRST_VISIBLE_POSITION = "listViewFirstVisiblePosition";//19
	public static final String COL_LISTVIEW_TOP = "listViewTop";//20

	public static final String[] PROJECTION_ALL = { COL_LIST_ID, COL_LIST_TITLE, COL_STORE_ID,
			COL_SHOW_GROUPS_IN_LISTS_FRAGMENT, COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT, COL_SHOW_STORES,
			COL_DELETE_NOTE_UPON_DESELECTING_ITEM,
			COL_LIST_SORT_ORDER, COL_MASTER_LIST_SORT_ORDER,
			COL_TITLE_BACKGROUND_COLOR, COL_TITLE_TEXT_COLOR,
			COL_SEPARATOR_BACKGROUND_COLOR, COL_SEPARATOR_TEXT_COLOR,
			COL_LIST_BACKGROUND_COLOR, COL_ITEM_NORMAL_TEXT_COLOR, COL_ITEM_STRIKEOUT_TEXT_COLOR,
			COL_MASTER_LIST_BACKGROUND_COLOR, COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
			COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
			COL_LISTVIEW_FIRST_VISIBLE_POSITION, COL_LISTVIEW_TOP
	};

	public static final String CONTENT_PATH = TABLE_LISTS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LISTS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_LISTS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_LIST_TITLE = COL_LIST_TITLE + " ASC";

	private final static long DEFAULT_LIST_PREFERENCES = 1;

	// Database creation SQL statements
	private static final String DATATABLE_CREATE = "create table "
			+ TABLE_LISTS
			+ " ("
			+ COL_LIST_ID + " integer primary key autoincrement, "
			+ COL_LIST_TITLE + " text collate nocase, "
			+ COL_STORE_ID + " integer not null references "
			+ StoresTable.TABLE_STORES + " (" + StoresTable.COL_STORE_ID + ") default 1, " // default [No Store]

			// List Settings
			+ COL_SHOW_GROUPS_IN_LISTS_FRAGMENT + " integer  default 0, " // default false=0
			+ COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT + " integer  default 0, " // default false=0
			+ COL_SHOW_STORES + " integer  default 0, " // default false=0

			+ COL_DELETE_NOTE_UPON_DESELECTING_ITEM + " integer default 1, " // default true=1

			+ COL_LIST_SORT_ORDER + " integer default 0, " // default Alphabetically=0
			+ COL_MASTER_LIST_SORT_ORDER + " integer default 0, " // default Alphabetically=0

			// TODO: set default colors
			+ COL_TITLE_BACKGROUND_COLOR + " integer default -1, "
			+ COL_TITLE_TEXT_COLOR + " integer default -1, "

			+ COL_SEPARATOR_BACKGROUND_COLOR + " integer default -1, "
			+ COL_SEPARATOR_TEXT_COLOR + " integer default -1, "

			+ COL_LIST_BACKGROUND_COLOR + " integer default -1, "
			+ COL_ITEM_NORMAL_TEXT_COLOR + " integer default -1, "
			+ COL_ITEM_STRIKEOUT_TEXT_COLOR + " integer default -1, "

			+ COL_MASTER_LIST_BACKGROUND_COLOR + " integer default -1, "
			+ COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR + " integer default -1, "
			+ COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR + " integer default -1, "

			+ COL_LISTVIEW_FIRST_VISIBLE_POSITION + " integer default 0, "
			+ COL_LISTVIEW_TOP + " integer default 0"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("ListsTable", "onCreate: " + TABLE_LISTS + " created.");

		String insertProjection = "insert into "
				+ TABLE_LISTS
				+ " ("
				+ COL_LIST_ID + ", "
				+ COL_LIST_TITLE
				+ ") VALUES ";

		ArrayList<String> sqlStatements = new ArrayList<String>();
		// List 1 used as the default List Preferences
		sqlStatements.add(insertProjection + "(NULL, 'Default List Preferences')");

		//TODO: remove Groceries and ToDO lists
		sqlStatements.add(insertProjection + "(NULL, 'Groceries')");
		sqlStatements.add(insertProjection + "(NULL, 'To Do')");
		sqlStatements.add(insertProjection + "(NULL, 'List 4')");
		sqlStatements.add(insertProjection + "(NULL, 'List 5')");
		sqlStatements.add(insertProjection + "(NULL, 'List 6')");
		sqlStatements.add(insertProjection + "(NULL, 'List 7')");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.w(TABLE_LISTS, "Upgrading database from version " + oldVersion + " to version " + newVersion + ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
		onCreate(database);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static long CreateNewList(Context context, String listTitle) {
		long newListID = -1;
		if (listTitle != null) {
			listTitle = listTitle.trim();
			if (!listTitle.isEmpty()) {

				@SuppressWarnings("resource")
				Cursor cursor = getList(context, listTitle);
				if (cursor != null && cursor.getCount() > 0) {
					// listTitle already exists in the table
					cursor.moveToFirst();
					newListID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_LIST_ID));
					cursor.close();
				} else {
					// listTitle does not exists in the table ... so add it
					ContentResolver cr = context.getContentResolver();
					Uri uri = CONTENT_URI;
					ContentValues values = new ContentValues();
					values.put(COL_LIST_TITLE, listTitle);
					try {
						Uri newListUri = cr.insert(uri, values);
						if (newListUri != null) {
							newListID = Long.parseLong(newListUri.getLastPathSegment());
						}
					} catch (Exception e) {
						MyLog.e("Exception error in CreateNewList. ", e.toString());
					}
				}

			} else {
				MyLog.e("ListTitlesTable", "Error in CreateNewList; groupName is Empty!");
			}

		} else {
			MyLog.e("ListTitlesTable", "Error in CreateNewList; groupName is Null!");
		}
		return newListID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static Cursor getList(Context context, long listID) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error in ListTitlesTable: getList. ", e.toString());
		}
		return cursor;
	}

	public static Cursor getList(Context context, String listTitle) {
		Cursor cursor = null;
		if (listTitle != null) {
			listTitle = listTitle.trim();
			if (!listTitle.isEmpty()) {
				Uri uri = CONTENT_URI;
				String[] projection = PROJECTION_ALL;
				String selection = COL_LIST_TITLE + " = ?";
				String selectionArgs[] = { listTitle };
				String sortOrder = null;
				ContentResolver cr = context.getContentResolver();
				try {
					cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
				} catch (Exception e) {
					MyLog.e("Exception error in ListTitlesTable: getList. ", e.toString());
				}
			} else {
				MyLog.e("ListTitlesTable", "Error in getList; groupName is Empty!");
			}
		} else {
			MyLog.e("ListTitlesTable", "Error in getList; groupName is Null!");
		}
		return cursor;
	}

	public static Cursor getAllLists(Context context) {
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = COL_LIST_ID + "> ?";
		String[] selectionArgs = { String.valueOf(DEFAULT_LIST_PREFERENCES) }; //DEFAULT_LIST_PREFERENCES=1
		String sortOrder = SORT_ORDER_LIST_TITLE;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error in ListTitlesTable: getAllLists. ", e.toString());
		}
		return cursor;
	}

	public static int getNumberOfLists(Context context) {
		int numberOfLists = -1;
		Cursor cursor = getAllLists(context);
		if (cursor != null) {
			cursor.moveToFirst();
			numberOfLists = cursor.getCount();
			cursor.close();
		}
		return numberOfLists;
	}

	public static String getListTitle(Context context, long listID) {
		String listTitle = "";
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			listTitle = cursor.getString(cursor.getColumnIndexOrThrow(COL_LIST_TITLE));
			cursor.close();
		}
		return listTitle;
	}

	public static Cursor getDefaultListPreferencesCursor(Context context) {
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = COL_LIST_ID + "= ?";
		String[] selectionArgs = { String.valueOf(DEFAULT_LIST_PREFERENCES) };
		String sortOrder = SORT_ORDER_LIST_TITLE;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error in ListTitlesTable: getDefaultListPreferencesCursor. ", e.toString());
		}
		return cursor;
	}

	public static long getFirstListID(Context context) {
		long firstListID = -1;
		Cursor allListsCursor = getAllLists(context);
		if (allListsCursor != null) {
			allListsCursor.moveToFirst();
			firstListID = allListsCursor.getLong(allListsCursor.getColumnIndexOrThrow(COL_LIST_ID));
			allListsCursor.close();
		}
		return firstListID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// List Preferences Getters and Setters
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean getGroupsAreShownInListsFragment(Context context, long listID) {
		boolean results = false;
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT));
			results = AListUtilities.intToBoolean(value);
			cursor.close();
		}
		return results;
	}

	/*	public static void setGroupsAreShownInListsFragment(Context context, long listID, boolean value) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

			ContentValues values = new ContentValues();
			values.put(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT, value);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: setGroupsAreShownInListsFragment",
						"The number of ListTitle records updated does not equal 1!");
			}
		}*/

	public static boolean getGroupsAreShownInMasterListFragment(Context context, long listID) {
		boolean results = false;
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT));
			results = AListUtilities.intToBoolean(value);
			cursor.close();
		}
		return results;
	}

	/*	public static void setGroupsAreShownInMasterListFragment(Context context, long listID, boolean value) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

			ContentValues values = new ContentValues();
			values.put(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT, value);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: setGroupsAreShownInMasterListFragment",
						"The number of ListTitle records updated does not equal 1!");
			}
		}*/

	public static boolean getStoresAreShown(Context context, long listID) {
		boolean results = false;
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SHOW_STORES));
			results = AListUtilities.intToBoolean(value);
			cursor.close();
		}
		return results;
	}

	/*	public static void setStoresAreShown(Context context, long listID, boolean value) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

			ContentValues values = new ContentValues();
			values.put(COL_SHOW_STORES, value);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: setStoresAreShown",
						"The number of ListTitle records updated does not equal 1!");
			}
		}*/

	public static boolean getDeleteNoteUponDeslectingItem(Context context, long listID) {
		boolean results = false;
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DELETE_NOTE_UPON_DESELECTING_ITEM));
			results = AListUtilities.intToBoolean(value);
			cursor.close();
		}
		return results;
	}

	/*	public static void setDeleteNoteUponUnStrikingItem(Context context, long listID, boolean value) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

			ContentValues values = new ContentValues();
			values.put(COL_DELETE_NOTE_UPON_DESELECTING_ITEM, value);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: setDeleteNoteUponUnStrikingItem",
						"The number of ListTitle records updated does not equal 1!");
			}
		}*/

	public static int getListPreference(Context context, long listID, String ColumnName) {
		int intResult = -1;
		Cursor cursor = getList(context, listID);
		if (cursor != null) {
			cursor.moveToFirst();
			int position = cursor.getColumnIndexOrThrow(ColumnName);

			switch (position) {
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
				intResult = cursor.getInt(position);
				break;

			default:
				break;
			}
			cursor.close();
		}
		return intResult;
	}

	/*	public static void setListPreference(Context context, long listID, String ColumnName, int value) {
			if (listID > 1) {
				ContentResolver cr = context.getContentResolver();
				Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

				ContentValues values = new ContentValues();
				values.put(ColumnName, value);
				String selection = null;
				String[] selectionArgs = null;
				int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
				if (numberOfUpdatedRecords != 1) {
					MyLog.e("ListTitlesTable: setListPreference", "The number of ListPreference records does not equal 1!");
				}
			}
		}*/

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
		public static void UpdateListTitle(Context context, long listID, String listTitle) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));

			ContentValues values = new ContentValues();
			values.put(COL_LIST_TITLE, listTitle);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: UpdateListTitle", "The number of records does not equal 1!");
			}
		}

		public static void UpdateStoreID(Context context, long listID, long storeID) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));
			ContentValues values = new ContentValues();
			values.put(COL_STORE_ID, storeID);
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(uri, values, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: UpdateStoreID", "The number of records does not equal 1!");
			}
		}*/

	public static void UpdateDefaultListPreferences(Context context, long listID) {
		ContentResolver cr = context.getContentResolver();

		Cursor listCursor = getList(context, listID);
		if (listCursor != null) {
			listCursor.moveToFirst();
			ContentValues newDefaultValues = new ContentValues();

			newDefaultValues.put(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT,
					listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT)));
			newDefaultValues.put(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT,
					listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT)));
			newDefaultValues.put(COL_SHOW_STORES, listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_SHOW_STORES)));

			newDefaultValues.put(COL_DELETE_NOTE_UPON_DESELECTING_ITEM,
					listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_DELETE_NOTE_UPON_DESELECTING_ITEM)));

			newDefaultValues.put(COL_LIST_SORT_ORDER,
					listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_LIST_SORT_ORDER)));
			newDefaultValues.put(COL_MASTER_LIST_SORT_ORDER,
					listCursor.getInt(listCursor.getColumnIndexOrThrow(COL_MASTER_LIST_SORT_ORDER)));

			Uri defaultUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(1));
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(defaultUri, newDefaultValues, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: UpdateDefaultListPreferences", "The number of records does not equal 1!");
			}
			listCursor.close();
		}
	}

	public static int UpdateListsTableFieldValues(Context context, long listID, ContentValues newFieldValues) {
		int numberOfUpdatedRecords = -1;
		if (listID > 1) {
			ContentResolver cr = context.getContentResolver();
			Uri defaultUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));
			String selection = null;
			String[] selectionArgs = null;
			numberOfUpdatedRecords = cr.update(defaultUri, newFieldValues, selection, selectionArgs);
		}
		return numberOfUpdatedRecords;
	}

	public static void setListPreferencesDefaults(Context context, long listID) {
		ContentResolver cr = context.getContentResolver();

		Cursor defaultCursor = getList(context, 1);
		if (defaultCursor != null) {
			defaultCursor.moveToFirst();
			ContentValues newListValues = new ContentValues();

			newListValues.put(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_LISTS_FRAGMENT)));

			newListValues.put(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT)));

			newListValues.put(COL_SHOW_STORES,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_SHOW_STORES)));

			newListValues.put(COL_DELETE_NOTE_UPON_DESELECTING_ITEM,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_DELETE_NOTE_UPON_DESELECTING_ITEM)));

			newListValues.put(COL_LIST_SORT_ORDER,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_LIST_SORT_ORDER)));

			newListValues.put(COL_MASTER_LIST_SORT_ORDER,
					defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(COL_MASTER_LIST_SORT_ORDER)));

			Uri defaultUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(listID));
			String selection = null;
			String[] selectionArgs = null;
			int numberOfUpdatedRecords = cr.update(defaultUri, newListValues, selection, selectionArgs);
			if (numberOfUpdatedRecords != 1) {
				MyLog.e("ListTitlesTable: setListPreferencesDefaults", "The number of records does not equal 1!");
			}
			defaultCursor.close();
		}
	}

	/*public static long FindNextListTitleID(Context context, long listTitleID) {
		Cursor listTitlesCursor = null;
		long nextListTitleID = -1;
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String orderBy = SORT_ORDER_LIST_TITLE;
			listTitlesCursor = cr.query(uri, projection, null, null, orderBy);
		} catch (Exception e) {
			MyLog.e("Exception error in ListsTable: FindNextListID. ", e.toString());
			if (listTitlesCursor != null) {
				listTitlesCursor.close();
			}
			return -1;
		}

		if (listTitlesCursor != null && listTitlesCursor.getCount() > 0) {
			long id = -1;
			boolean foundID = false;
			do {
				id = listTitlesCursor.getLong(listTitlesCursor.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_ID));
				if (id == listTitleID) {
					foundID = true;
					break;
				}
			} while (listTitlesCursor.moveToNext());

			if (foundID) {
				if (listTitlesCursor.moveToNext()) {
					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_ID));

				} else if (listTitlesCursor.moveToPrevious() && listTitlesCursor.moveToPrevious()) {

					nextListTitleID = listTitlesCursor.getLong(listTitlesCursor
							.getColumnIndexOrThrow(ListTitlesTable.COL_LIST_ID));
				}
			}
		}
		if (listTitlesCursor != null) {
			listTitlesCursor.close();
		}
		return nextListTitleID;
	}*/

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int DeleteList(Context context, long listID) {
		int numberOfDeletedRecords = -1;
		if (listID > 1) {
			ItemsTable.DeleteAllItemsInList(context, listID);
			GroupsTable.DeleteAllGroupsInList(context, listID);
			StoresTable.DeleteAllStoresInList(context, listID);

			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + " = ?";
			String[] selectionArgs = { String.valueOf(listID) };
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

}
