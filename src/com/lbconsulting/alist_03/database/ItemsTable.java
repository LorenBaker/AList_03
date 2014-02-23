package com.lbconsulting.alist_03.database;

import java.util.ArrayList;
import java.util.Calendar;

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

public class ItemsTable {

	// Items data table
	public static final String TABLE_ITEMS = "tblItems";
	public static final String COL_ITEM_ID = "_id";
	public static final String COL_ITEM_NAME = "itemName";
	public static final String COL_ITEM_NOTE = "itemNote";
	public static final String COL_LIST_ID = "listID";
	public static final String COL_GROUP_ID = "groupID";
	public static final String COL_SELECTED = "itemSelected";
	public static final String COL_STRUCK_OUT = "itemStruckOut";
	public static final String COL_CHECKED = "itemChecked";
	public static final String COL_MANUAL_SORT_ORDER = "manualSortOrder";
	public static final String COL_DATE_TIME_LAST_USED = "dateTimeLastUsed";

	public static final String[] PROJECTION_ALL = { COL_ITEM_ID, COL_ITEM_NAME, COL_ITEM_NOTE, COL_LIST_ID,
			COL_GROUP_ID, COL_SELECTED, COL_STRUCK_OUT, COL_CHECKED, COL_MANUAL_SORT_ORDER, COL_DATE_TIME_LAST_USED };

	public static final String CONTENT_PATH = TABLE_ITEMS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_ITEMS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_ITEMS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AListContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_ITEM_NAME = COL_ITEM_NAME + " ASC";
	public static final String SORT_ORDER_SELECTED_AT_TOP = COL_SELECTED + " ASC, " + SORT_ORDER_ITEM_NAME;
	public static final String SORT_ORDER_SELECTED_AT_BOTTOM = COL_SELECTED + " DESC, " + SORT_ORDER_ITEM_NAME;
	public static final String SORT_ORDER_LAST_USED = COL_DATE_TIME_LAST_USED + " DESC, " + SORT_ORDER_ITEM_NAME;

	//TODO: SORT by group name not id!
	public static final String SORT_ORDER_BY_GROUP = COL_GROUP_ID + " ASC, " + SORT_ORDER_ITEM_NAME;

	public static final int SELECTED_TRUE = 1;
	public static final int SELECTED_FALSE = 0;

	public static final int STRUCKOUT_TRUE = 1;
	public static final int STRUCKOUT_FALSE = 0;

	public static final int CHECKED_TRUE = 1;
	public static final int CHECKED_FALSE = 0;

	// Database creation SQL statements
	private static final String DATATABLE_CREATE =
			"create table " + TABLE_ITEMS
					+ " ("
					+ COL_ITEM_ID + " integer primary key autoincrement, "
					+ COL_ITEM_NAME + " text collate nocase, "
					+ COL_ITEM_NOTE + " text collate nocase, "
					+ COL_LIST_ID + " integer not null references "
					+ ListsTable.TABLE_LISTS + " (" + ListsTable.COL_LIST_ID + ") default 1, "
					+ COL_GROUP_ID + " integer not null references "
					+ GroupsTable.TABLE_GROUPS + " (" + GroupsTable.COL_GROUP_ID + ") default 1, "
					+ COL_SELECTED + " integer default 0, "
					+ COL_STRUCK_OUT + " integer default 0, "
					+ COL_CHECKED + " integer default 0, "
					+ COL_MANUAL_SORT_ORDER + " integer default -1, "
					+ COL_DATE_TIME_LAST_USED + " integer"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATATABLE_CREATE);
		MyLog.i("ItemsTable", "onCreate: " + TABLE_ITEMS + " created.");

		String insertProjection = "insert into "
				+ TABLE_ITEMS
				+ " ("
				+ COL_ITEM_ID + ", "
				+ COL_ITEM_NAME + ", "
				+ COL_ITEM_NOTE + ", "
				+ COL_LIST_ID + ", "
				+ COL_SELECTED + ", "
				+ COL_DATE_TIME_LAST_USED
				+ ") VALUES ";

		ArrayList<String> sqlStatements = new ArrayList<String>();

		Calendar rightNow = Calendar.getInstance();
		long currentDateTimeInMillis = rightNow.getTimeInMillis();
		// round current millisecond time to the nearest second 
		currentDateTimeInMillis = ((currentDateTimeInMillis + 500) / 1000) * 1000;

		// Groceries List (2)
		sqlStatements.add(insertProjection + "(NULL, 'Apples', 'Note A', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Bacon', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Baked Beans', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Balsamic Vinegar', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Bananas', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Beer', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Black Olives', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Blue Cheese', '8oz', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Blueberries', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Bread', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Broccoli', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Buns', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Buttermilk', '1 quart', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Carrots', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Cereal', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Cheese', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Chicken', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Chicken Broth', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Corn', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Cottage Cheese', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Cream Cheese', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Distilled Water', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Eggs', '2 dozen', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Garlic', '3 heads', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Ground Beef', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Hummus', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Kleenex', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Lemons', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Limes', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Manicotti Noodles', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Mustard', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Olive Oil', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Parmesan Cheese', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Peanut Butter', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Pickles', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Pineapple', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Potatoes', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Relish', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Sour Cream', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'String Cheese', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Toilet Paper', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Tomatoes', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Toothpaste', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Tuna', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Vanilla', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Vinegar', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Wine', 'Cabernet', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Yogurt', '', 2, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'Avocado', '', 2, 0, " + currentDateTimeInMillis + ")");

		// TO Do List (3)
		sqlStatements.add(insertProjection + "(NULL, 'To Do 07', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 08', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 09', 'Note 09', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 10', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 11', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 12', '', 3, 0, " + currentDateTimeInMillis + ")");

		sqlStatements.add(insertProjection + "(NULL, 'To Do 01', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 02', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 03', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 04', 'Note 04', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 05', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 06', '', 3, 0, " + currentDateTimeInMillis + ")");

		sqlStatements.add(insertProjection + "(NULL, 'To Do 13', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 14', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 15', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 16', '', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 17', 'Note 17', 3, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'To Do 18', '', 3, 0, " + currentDateTimeInMillis + ")");

		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=4 - Item 07', '', 4, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=4 - Item 08', '', 4, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'List Id=4 - Item 09', 'Note 09', 4, 1, "
				+ currentDateTimeInMillis
				+ ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=4 - Item 10', '', 4, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=4 - Item 11', '', 4, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=4 - Item 12', '', 4, 0, " + currentDateTimeInMillis + ")");

		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=5 - Item 13', '', 5, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=5 - Item 14', '', 5, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=5 - Item 15', '', 5, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=5 - Item 16', '', 5, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'List Id=5 - Item 17', 'Note 17', 5, 1, "
				+ currentDateTimeInMillis
				+ ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=5 - Item 18', '', 5, 0, " + currentDateTimeInMillis + ")");

		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=6 - Item 01', '', 6, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=6 - Item 02', '', 6, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=6 - Item 03', '', 6, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'List Id=6 - Item 04', 'Note 04', 6, 0, "
				+ currentDateTimeInMillis
				+ ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=6 - Item 05', '', 6, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=6 - Item 06', '', 6, 0, " + currentDateTimeInMillis + ")");

		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=7 - Item 01', '', 7, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=7 - Item 02', '', 7, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=7 - Item 03', '', 7, 0, " + currentDateTimeInMillis + ")");
		sqlStatements.add(insertProjection + "(NULL, 'List Id=7 - Item 04', 'Note 04', 7, 0, "
				+ currentDateTimeInMillis
				+ ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=7 - Item 05', '', 7, 0, " + currentDateTimeInMillis + ")");
		sqlStatements
				.add(insertProjection + "(NULL, 'List Id=7 - Item 06', '', 7, 0, " + currentDateTimeInMillis + ")");

		AListUtilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.w(TABLE_ITEMS, "Upgrading database from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(database);
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
	public static long CreateNewItem(Context context, long listID, String itemName) {
		long newItemID = -1;
		if (listID > 1) {
			// verify that the item does not already exist in the table
			@SuppressWarnings("resource")
			Cursor cursor = getItem(context, listID, itemName);
			if (cursor != null && cursor.getCount() > 0) {
				// the item exists in the table ... so return its id
				cursor.moveToFirst();
				newItemID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ITEM_ID));
				cursor.close();
			} else {
				// item does not exist in the table ... so add it	
				if (itemName != null) {
					itemName = itemName.trim();
					if (!itemName.isEmpty()) {
						try {
							ContentResolver cr = context.getContentResolver();
							Uri uri = CONTENT_URI;
							ContentValues values = new ContentValues();
							values.put(COL_LIST_ID, listID);
							values.put(COL_ITEM_NAME, itemName);
							Uri newListUri = cr.insert(uri, values);
							if (newListUri != null) {
								newItemID = Long.parseLong(newListUri.getLastPathSegment());
							}
						} catch (Exception e) {
							MyLog.e("Exception error in CreateNewList. ", e.toString());

						}

					} else {
						MyLog.e("ItemsTable", "Error in CreateNewItem; itemName is Empty!");
					}
				} else {
					MyLog.e("ItemsTable", "Error in CreateNewItem; itemName is Null!");
				}
			}
		}
		return newItemID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Cursor getItem(Context context, long itemID) {
		Cursor cursor = null;
		if (itemID > 0) {
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(itemID));
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

	public static Cursor getItem(Context context, long listID, String itemName) {
		Cursor cursor = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_ITEM_NAME + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), itemName };
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

	public static long getListID(Context context, long itemID) {
		long listID = -1;
		Cursor cursor = getItem(context, itemID);
		if (cursor != null) {
			cursor.moveToFirst();
			listID = cursor.getLong(cursor.getColumnIndexOrThrow(COL_LIST_ID));
			cursor.close();
		}

		return listID;
	}

	/**
	 * This method gets all items in the provided list
	 * 
	 * @param context
	 * @param listID
	 * @param itemName
	 * @return Returns all items associated with the provided list ID
	 */
	public static CursorLoader getAllItemsInList(Context context, long listID, String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID) };
			try {
				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error  in ItemsTable: getAllItemsInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	public static CursorLoader getAllItemsInList(Context context, long listID, String selection, String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			if (selection != null) {
				selection = selection + " AND " + COL_LIST_ID + " = ?";
			} else {
				selection = COL_LIST_ID + " = ?";
			}
			String selectionArgs[] = new String[] { String.valueOf(listID) };
			try {
				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error  in ItemsTable: getAllItemsInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	/**
	 * This method gets all items in the provided list that are selected (True)
	 * or not selected (False)
	 * 
	 * @param context
	 * @param listID
	 * @param selected
	 * @return Returns all selected (or not selected) items in the list.
	 */
	public static CursorLoader getAllSelectedItemsInList(Context context, long listID, boolean selected,
			String sortOrder) {
		CursorLoader cursorLoader = null;
		if (listID > 1) {
			int selectedValue = AListUtilities.boolToInt(selected);
			if (sortOrder == null) {
				sortOrder = SORT_ORDER_ITEM_NAME;
			}
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_SELECTED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(selectedValue) };
			/*ContentResolver cr = context.getContentResolver();*/
			try {

				cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
				/*				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);*/
			} catch (Exception e) {
				MyLog.e("Exception error  in ItemsTable: getAllSelectedItemsInList. ", e.toString());
			}
		}
		return cursorLoader;
	}

	/**
	 * This method gets all items in the provided list that are struck out
	 * (True) or not struck out (False)
	 * 
	 * @param context
	 * @param listID
	 * @param struckOut
	 * @param sortOrder
	 * @return
	 */
	public static Cursor getAllStruckOutItemsInList(Context context, long listID, boolean struckOut, String sortOrder) {
		Cursor cursor = null;
		if (listID > 1) {
			int struckOutValue = AListUtilities.boolToInt(struckOut);
			if (sortOrder == null) {
				sortOrder = SORT_ORDER_ITEM_NAME;
			}
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_STRUCK_OUT + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(struckOutValue) };
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error  in ItemsTable: getAllStruckOutItemsInList. ", e.toString());
			}
		}
		return cursor;
	}

	public static Cursor getAllCheckedItemsInList(Context context, long listID, boolean checked) {
		Cursor cursor = null;
		if (listID > 1) {

			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_LIST_ID + " = ? AND " + COL_CHECKED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(checked) };
			String sortOrder = null;
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error  in ItemsTable: getAllCheckedItemsInList. ", e.toString());
			}
		}
		return cursor;
	}

	/**
	 * This method gets all items in the provided group.
	 * 
	 * @param context
	 * @param groupID
	 * @return
	 */
	public static Cursor getAllItemsInGroup(Context context, long groupID, String sortOrder) {
		Cursor cursor = null;
		if (groupID > 0) {
			if (sortOrder == null) {
				sortOrder = SORT_ORDER_ITEM_NAME;
			}
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_GROUP_ID + " = ?";
			String[] selectionArgs = { String.valueOf(groupID) };
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in ItemsTable: getAllItemsInGroup. ", e.toString());
			}
		}
		return cursor;
	}

	/**
	 * This method gets all items in the provided group that are selected (True)
	 * or not selected (False).
	 * 
	 * @param context
	 * @param groupID
	 * @param selected
	 * @return
	 */
	public static Cursor getAllSelectedItemsInGroup(Context context, long groupID, boolean selected, String sortOrder) {
		Cursor cursor = null;
		if (groupID > 0) {
			int selectedValue = AListUtilities.boolToInt(selected);
			if (sortOrder == null) {
				sortOrder = SORT_ORDER_ITEM_NAME;
			}
			Uri uri = CONTENT_URI;
			String[] projection = PROJECTION_ALL;
			String selection = COL_GROUP_ID + " = ? AND " + COL_SELECTED + " = ?";
			String[] selectionArgs = { String.valueOf(groupID), String.valueOf(selectedValue) };
			ContentResolver cr = context.getContentResolver();
			try {
				cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in ItemsTable: getAllSelectedItemsInGroup. ", e.toString());
			}
		}
		return cursor;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int UpdateItemFieldValues(Context context, long itemID, ContentValues newFieldValues) {
		int numberOfUpdatedRecords = -1;
		ContentResolver cr = context.getContentResolver();
		Uri itemUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(itemID));
		String selection = null;
		String[] selectionArgs = null;
		numberOfUpdatedRecords = cr.update(itemUri, newFieldValues, selection, selectionArgs);
		return numberOfUpdatedRecords;
	}

	/*	public static int UpdateItemName(Context context, long itemID, String newItemName) {
			int numberOfUpdatedRecords = -1;
			if (itemID > 0) {
				newItemName = newItemName.trim();
				try {
					ContentResolver cr = context.getContentResolver();
					Uri uri = CONTENT_URI;
					String where = COL_ITEM_ID + " = ?";
					String[] whereArgs = { String.valueOf(itemID) };
					ContentValues values = new ContentValues();
					values.put(COL_ITEM_NAME, newItemName);
					numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
				} catch (Exception e) {
					MyLog.e("Exception error in UpdateItemName. ", e.toString());
				}
			}
			return numberOfUpdatedRecords;
		}

		public static int UpdateItemNote(Context context, long itemID, String newItemNote) {
			int numberOfUpdatedRecords = -1;
			if (itemID > 0) {
				newItemNote = newItemNote.trim();
				try {
					ContentResolver cr = context.getContentResolver();
					Uri uri = CONTENT_URI;
					String where = COL_ITEM_ID + " = ?";
					String[] whereArgs = { String.valueOf(itemID) };
					ContentValues values = new ContentValues();
					values.put(COL_ITEM_NOTE, newItemNote);
					numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
				} catch (Exception e) {
					MyLog.e("Exception error in UpdateItemNote. ", e.toString());
				}
			}
			return numberOfUpdatedRecords;
		}*/

	/*	public static int ChangeGroupID(Context context, long itemID, long newGroupID) {
			int numberOfUpdatedRecords = -1;
			if (itemID > 0 && newGroupID > 0) {
				try {
					ContentResolver cr = context.getContentResolver();
					Uri uri = CONTENT_URI;
					String where = COL_ITEM_ID + " = ?";
					String[] whereArgs = { String.valueOf(itemID) };
					ContentValues values = new ContentValues();
					values.put(COL_GROUP_ID, newGroupID);
					numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
				} catch (Exception e) {
					MyLog.e("Exception error in ChangeGroupID. ", e.toString());
				}
			}
			return numberOfUpdatedRecords;
		}*/

	public static int UpdateItem(Context context, long itemID, String itemName, String itemNote, long itemGroupID) {
		int numberOfUpdatedRecords = -1;
		if (itemID > 0) {
			itemName = itemName.trim();
			itemNote = itemNote.trim();
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ITEM_ID + " = ?";
				String[] whereArgs = { String.valueOf(itemID) };
				ContentValues values = new ContentValues();
				values.put(COL_ITEM_NAME, itemName);
				values.put(COL_ITEM_NOTE, itemNote);
				values.put(COL_GROUP_ID, itemGroupID);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in UpdateItem. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;

	}

	public static int SelectItem(Context context, long itemID, boolean selected) {
		int numberOfUpdatedRecords = -1;
		if (itemID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ITEM_ID + " = ?";
				String[] whereArgs = { String.valueOf(itemID) };

				ContentValues values = new ContentValues();
				int selectedValue = AListUtilities.boolToInt(selected);
				values.put(COL_SELECTED, selectedValue);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in SelectItem. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int DeselectAllItemsInList(Context context, long listID, boolean deleteNoteUponDeslectingItem) {
		int numberOfUpdatedRecords = -1;
		if (listID > 1) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_LIST_ID + " = ? AND " + COL_SELECTED + " = ?";
				String[] whereArgs = { String.valueOf(listID), String.valueOf(SELECTED_TRUE) };

				ContentValues values = new ContentValues();
				values.put(COL_STRUCK_OUT, STRUCKOUT_FALSE);
				values.put(COL_SELECTED, SELECTED_FALSE);
				if (deleteNoteUponDeslectingItem) {
					values.put(COL_ITEM_NOTE, "");
				}
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in DeselectAllItemsInList. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int DeselectAllItemsInGroup(Context context, long groupID) {
		int numberOfUpdatedRecords = -1;
		if (groupID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_GROUP_ID + " = ? AND " + COL_SELECTED + " = ?";
				String[] whereArgs = { String.valueOf(groupID), String.valueOf(SELECTED_TRUE) };

				ContentValues values = new ContentValues();
				values.put(COL_SELECTED, SELECTED_FALSE);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in DeselectAllItemsInGroup. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static void ToggleStrikeOut(Context context, long itemID) {
		Cursor cursor = getItem(context, itemID);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndexOrThrow(COL_STRUCK_OUT);
			int strikeOutIntValue = cursor.getInt(columnIndex);
			boolean strikeOutValue = AListUtilities.intToBoolean(strikeOutIntValue);
			cursor.close();
			StrikeItem(context, itemID, !strikeOutValue);
		}
	}

	public static void ToggleSelection(Context context, long itemID) {
		Cursor cursor = getItem(context, itemID);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndexOrThrow(COL_SELECTED);
			int selectedIntValue = cursor.getInt(columnIndex);
			boolean selectedValue = AListUtilities.intToBoolean(selectedIntValue);
			cursor.close();
			SelectItem(context, itemID, !selectedValue);
		}

	}

	public static int StrikeItem(Context context, long itemID, boolean struckOut) {
		int numberOfUpdatedRecords = -1;
		if (itemID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ITEM_ID + " = ?";
				String[] whereArgs = { String.valueOf(itemID) };

				ContentValues values = new ContentValues();
				int struckOutValue = AListUtilities.boolToInt(struckOut);
				values.put(COL_STRUCK_OUT, struckOutValue);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in StrikeItem. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int UnStrikeAndDeselectAllStruckOutItems(Context context, long listID,
			boolean deleteNoteUponDeslectingItem) {
		int numberOfUpdatedRecords = -1;
		if (listID > 1) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_LIST_ID + " = ? AND " + COL_STRUCK_OUT + " = ?";
				String[] whereArgs = { String.valueOf(listID), String.valueOf(SELECTED_TRUE) };

				ContentValues values = new ContentValues();
				values.put(COL_STRUCK_OUT, STRUCKOUT_FALSE);
				values.put(COL_SELECTED, SELECTED_FALSE);
				if (deleteNoteUponDeslectingItem) {
					values.put(COL_ITEM_NOTE, "");
				}
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in UnStrikeAllItemsInList. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int UnStrikeAllItemsInGroup(Context context, long groupID) {
		int numberOfUpdatedRecords = -1;
		if (groupID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_GROUP_ID + " = ? AND " + COL_STRUCK_OUT + " = ?";
				String[] whereArgs = { String.valueOf(groupID), String.valueOf(SELECTED_TRUE) };

				ContentValues values = new ContentValues();
				values.put(COL_STRUCK_OUT, SELECTED_FALSE);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in UnStrikeAllItemsInGroup. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int CheckItem(Context context, long itemID, boolean checked) {
		int numberOfUpdatedRecords = -1;
		if (itemID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ITEM_ID + " = ?";
				String[] whereArgs = { String.valueOf(itemID) };

				ContentValues values = new ContentValues();
				int checkedValue = AListUtilities.boolToInt(checked);
				values.put(COL_CHECKED, checkedValue);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in CheckItem. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int UnCheckAllItemsInList(Context context, long listID) {
		int numberOfUpdatedRecords = -1;
		if (listID > 1) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_LIST_ID + " = ? AND " + COL_CHECKED + " = ?";
				String[] whereArgs = { String.valueOf(listID), String.valueOf(CHECKED_TRUE) };

				ContentValues values = new ContentValues();
				values.put(COL_CHECKED, CHECKED_FALSE);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in UnCheckAllItemsInList. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int MoveItem(Context context, long itemID, long newListID) {
		int numberOfUpdatedRecords = 0;
		String existingItemName;
		Cursor existingItemCursor = null;
		Cursor newListCursor = null;

		if (itemID > 0 && newListID > 1) {
			existingItemCursor = getItem(context, itemID);
			if (existingItemCursor != null) {
				if (existingItemCursor.getCount() > 0) {

					existingItemCursor.moveToFirst();
					existingItemName = existingItemCursor
							.getString(existingItemCursor.getColumnIndexOrThrow(COL_ITEM_NAME));

					// verify that the item does not already exist in the new list
					newListCursor = getItem(context, newListID, existingItemName);
					if (newListCursor != null) {
						if (newListCursor.getCount() == 0) {
							// the item does note exists in the table ... so move it
							// by changing the listID
							numberOfUpdatedRecords = ChangeListID(context, itemID, newListID);

						} else {
							// the item exists in the new list ... so move it 
							// by deleting it from the new list and changing the existing item's listID
							long newListItemID = newListCursor
									.getLong(newListCursor.getColumnIndexOrThrow(COL_ITEM_ID));
							DeleteItem(context, newListItemID);
							numberOfUpdatedRecords = ChangeListID(context, itemID, newListID);
						}
					}
				}
			}
		}

		if (existingItemCursor != null) {
			existingItemCursor.close();
		}
		if (newListCursor != null) {
			newListCursor.close();
		}

		return numberOfUpdatedRecords;
	}

	private static int ChangeListID(Context context, long itemID, long newListID) {
		int numberOfUpdatedRecords = -1;
		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		String where = COL_ITEM_ID + " = ?";
		String[] whereArgs = { String.valueOf(itemID) };

		ContentValues values = new ContentValues();
		values.put(COL_LIST_ID, newListID);
		values.put(COL_GROUP_ID, 1);
		numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);

		return numberOfUpdatedRecords;
	}

	public static int MoveAllCheckedItemsInList(Context context, long listID, long newListID) {
		int numberOfUpdatedRecords = -1;
		Cursor checkedItemsCursor = null;
		if (listID > 1 && newListID > 1) {

			checkedItemsCursor = getAllCheckedItemsInList(context, listID, true);
			if (checkedItemsCursor != null && checkedItemsCursor.getCount() > 0) {
				numberOfUpdatedRecords = 0;
				int numberOfItemsMoved = 0;
				long itemID;
				while (checkedItemsCursor.moveToNext()) {
					itemID = checkedItemsCursor.getLong(checkedItemsCursor.getColumnIndexOrThrow(COL_ITEM_ID));
					numberOfItemsMoved = MoveItem(context, itemID, newListID);
					numberOfUpdatedRecords += numberOfItemsMoved;
				}
				if (numberOfUpdatedRecords != checkedItemsCursor.getCount()) {
					StringBuilder sb = new StringBuilder();
					sb.append("Error in MoveAllCheckedItemsInList: ");
					sb.append(System.getProperty("line.separator"));
					sb.append("Number of items moved does not match the number of checked items in the list!");
					sb.append(System.getProperty("line.separator"));
					sb.append("Number of items moved = " + numberOfItemsMoved);
					sb.append(System.getProperty("line.separator"));
					sb.append("Number of checked items in the list = " + checkedItemsCursor.getCount());
					MyLog.e("ItemsTable", sb.toString());
				}
			}
		}
		if (checkedItemsCursor != null) {
			checkedItemsCursor.close();
		}
		return numberOfUpdatedRecords;
	}

	public static int setManualSortOrder(Context context, long itemID, int manualSortOrder) {
		int numberOfUpdatedRecords = -1;
		if (itemID > 0) {
			try {
				ContentResolver cr = context.getContentResolver();
				Uri uri = CONTENT_URI;
				String where = COL_ITEM_ID + " = ?";
				String[] whereArgs = { String.valueOf(itemID) };

				ContentValues values = new ContentValues();
				values.put(COL_MANUAL_SORT_ORDER, manualSortOrder);
				numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
			} catch (Exception e) {
				MyLog.e("Exception error in setManualSortOrder. ", e.toString());
			}
		}
		return numberOfUpdatedRecords;
	}

	public static int getManualSortOrder(Context context, long itemID) {
		int manualSortOrder = -1;
		if (itemID > 0) {
			Cursor cursor = getItem(context, itemID);
			if (cursor != null) {
				cursor.moveToFirst();
				manualSortOrder = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MANUAL_SORT_ORDER));
				cursor.close();
			}
		}
		return manualSortOrder;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static int DeleteItem(Context context, long itemID) {
		int numberOfDeletedRecords = -1;
		if (itemID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(itemID));
			String where = null;
			String[] selectionArgs = null;
			cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllItemsInList(Context context, long listID) {
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

	public static int DeleteAllSelectedItemsInList(Context context, long listID) {
		int numberOfDeletedRecords = -1;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + " = ? AND " + COL_SELECTED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(SELECTED_TRUE) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllStruckOutItemsInList(Context context, long listID) {
		int numberOfDeletedRecords = -1;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + " = ? AND " + COL_STRUCK_OUT + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(STRUCKOUT_TRUE) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

	public static int DeleteAllCheckedItemsInList(Context context, long listID) {
		int numberOfDeletedRecords = -1;
		if (listID > 1) {
			Uri uri = CONTENT_URI;
			String where = COL_LIST_ID + " = ? AND " + COL_CHECKED + " = ?";
			String selectionArgs[] = new String[] { String.valueOf(listID), String.valueOf(CHECKED_TRUE) };
			ContentResolver cr = context.getContentResolver();
			numberOfDeletedRecords = cr.delete(uri, where, selectionArgs);
		}
		return numberOfDeletedRecords;
	}

}
