package com.lbconsulting.alist_03.database.contentprovider;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.lbconsulting.alist_03.database.AListDatabaseHelper;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class AListContentProvider extends ContentProvider {

	// AList database
	private AListDatabaseHelper database = null;

	// UriMatcher switch constants
	private static final int ITEMS_MULTI_ROWS = 10;
	private static final int ITEMS_SINGLE_ROW = 11;

	private static final int LIST_MULTI_ROWS = 20;
	private static final int LIST_SINGLE_ROW = 21;

	private static final int GROUPS_MULTI_ROWS = 30;
	private static final int GROUPS_SINGLE_ROW = 31;

	private static final int STORES_MULTI_ROWS = 40;
	private static final int STORES_SINGLE_ROW = 41;

	public static final String AUTHORITY = "com.lbconsulting.alist_03.contentprovider";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, ItemsTable.CONTENT_PATH, ITEMS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ItemsTable.CONTENT_PATH + "/#", ITEMS_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH, LIST_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, ListsTable.CONTENT_PATH + "/#", LIST_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, GroupsTable.CONTENT_PATH, GROUPS_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, GroupsTable.CONTENT_PATH + "/#", GROUPS_SINGLE_ROW);

		sURIMatcher.addURI(AUTHORITY, StoresTable.CONTENT_PATH, STORES_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, StoresTable.CONTENT_PATH + "/#", STORES_SINGLE_ROW);

	}

	@Override
	public boolean onCreate() {
		MyLog.i("AListContentProvider", "onCreate");
		// Construct the underlying database
		// Defer opening the database until you need to perform
		// a query or other transaction.
		database = new AListDatabaseHelper(getContext());
		return true;
	}

	/*A content provider is created when its hosting process is created, 
	 * and remains around for as long as the process does, so there is 
	 * no need to close the database -- it will get closed as part of the 
	 * kernel cleaning up the process's resources when the process is killed.
	 */

	@SuppressWarnings("resource")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String rowID = null;
		int deleteCount = 0;

		// Open a WritableDatabase database to support the delete transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			// To return the number of deleted items you must specify a where clause.
			// To delete all rows and return a value pass in "1".
			if (selection == null) {
				selection = "1";
			}

			// Perform the deletion
			deleteCount = db.delete(ItemsTable.TABLE_ITEMS, selection, selectionArgs);
			break;

		case ITEMS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ItemsTable.COL_ITEM_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(ItemsTable.TABLE_ITEMS, selection, selectionArgs);
			break;

		case LIST_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
			break;

		case LIST_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListsTable.COL_LIST_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
			break;

		case GROUPS_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(GroupsTable.TABLE_GROUPS, selection, selectionArgs);
			break;

		case GROUPS_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = GroupsTable.COL_GROUP_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(GroupsTable.TABLE_GROUPS, selection, selectionArgs);
			break;

		case STORES_MULTI_ROWS:
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(StoresTable.TABLE_STORES, selection, selectionArgs);
			break;

		case STORES_SINGLE_ROW:
			rowID = uri.getLastPathSegment();
			selection = StoresTable.COL_STORE_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(StoresTable.TABLE_STORES, selection, selectionArgs);
			break;

		/*		case LISTS_MULTI_ROWS:
					if (selection == null) {
						selection = "1";
					}
					// Perform the deletion
					deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
					break;

				case LISTS_SINGLE_ROW:
					// Limit deletion to a single row
					rowID = uri.getLastPathSegment();
					selection = ListsTable.COL_ID + "=" + rowID
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
					// Perform the deletion
					deleteCount = db.delete(ListsTable.TABLE_LISTS, selection, selectionArgs);
					break;*/

		default:
			throw new IllegalArgumentException("Method delete: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			return ItemsTable.CONTENT_TYPE;
		case ITEMS_SINGLE_ROW:
			return ItemsTable.CONTENT_ITEM_TYPE;

		case LIST_MULTI_ROWS:
			return ListsTable.CONTENT_TYPE;
		case LIST_SINGLE_ROW:
			return ListsTable.CONTENT_ITEM_TYPE;

		case GROUPS_MULTI_ROWS:
			return GroupsTable.CONTENT_TYPE;
		case GROUPS_SINGLE_ROW:
			return GroupsTable.CONTENT_ITEM_TYPE;

		case STORES_MULTI_ROWS:
			return StoresTable.CONTENT_TYPE;
		case STORES_SINGLE_ROW:
			return StoresTable.CONTENT_ITEM_TYPE;

			/*		case LISTS_MULTI_ROWS:
						return ListsTable.CONTENT_TYPE;
					case LISTS_SINGLE_ROW:
						return ListsTable.CONTENT_ITEM_TYPE;*/

		default:
			throw new IllegalArgumentException("Method getType. Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = null;
		long newRowId = 0;
		String nullColumnHack = null;
		//String[] sKeys = null;

		// Open a WritableDatabase database to support the insert transaction
		db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			values.put(ItemsTable.COL_DATE_TIME_LAST_USED, Calendar.getInstance().getTimeInMillis());
			newRowId = db.insertOrThrow(ItemsTable.TABLE_ITEMS, nullColumnHack, values);
			if (newRowId > 0) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(ItemsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(ItemsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case ITEMS_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannon insert a new row with a single row URI. Illegal URI: " + uri);

		case LIST_MULTI_ROWS:
			newRowId = db.insertOrThrow(ListsTable.TABLE_LISTS, nullColumnHack, values);
			if (newRowId > 0) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(ListsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(ListsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case LIST_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case GROUPS_MULTI_ROWS:
			newRowId = db.insertOrThrow(GroupsTable.TABLE_GROUPS, nullColumnHack, values);
			if (newRowId > 0) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(GroupsTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(GroupsTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case GROUPS_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

		case STORES_MULTI_ROWS:
			newRowId = db.insertOrThrow(StoresTable.TABLE_STORES, nullColumnHack, values);
			if (newRowId > 0) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(StoresTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(StoresTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case STORES_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);

			/*		case LISTS_MULTI_ROWS:
						newRowId = db.insertOrThrow(ListsTable.TABLE_LISTS, nullColumnHack, values);
						if (newRowId > -1) {
							// Construct and return the URI of the newly inserted row.
							Uri newRowUri = ContentUris.withAppendedId(ListsTable.CONTENT_URI, newRowId);
							// Notify and observers of the change in the database.
							getContext().getContentResolver().notifyChange(ListsTable.CONTENT_URI, null);
							return newRowUri;
						} else {
							return null;
						}

					case LISTS_SINGLE_ROW:
						throw new IllegalArgumentException(
								"Method insert: Cannot insert a new row with a single row URI. Illegal URI: " + uri);*/

		default:
			throw new IllegalArgumentException("Method insert: Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			queryBuilder.setTables(ItemsTable.TABLE_ITEMS);
			checkItemsColumnNames(projection);
			break;

		case ITEMS_SINGLE_ROW:
			queryBuilder.setTables(ItemsTable.TABLE_ITEMS);
			checkItemsColumnNames(projection);
			queryBuilder.appendWhere(ItemsTable.COL_ITEM_ID + "=" + uri.getLastPathSegment());
			break;

		case LIST_MULTI_ROWS:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListsColumnNames(projection);
			break;

		case LIST_SINGLE_ROW:
			queryBuilder.setTables(ListsTable.TABLE_LISTS);
			checkListsColumnNames(projection);
			queryBuilder.appendWhere(ListsTable.COL_LIST_ID + "=" + uri.getLastPathSegment());
			break;

		case GROUPS_MULTI_ROWS:
			queryBuilder.setTables(GroupsTable.TABLE_GROUPS);
			checkGroupsColumnNames(projection);
			break;

		case GROUPS_SINGLE_ROW:
			queryBuilder.setTables(GroupsTable.TABLE_GROUPS);
			checkGroupsColumnNames(projection);
			queryBuilder.appendWhere(GroupsTable.COL_GROUP_ID + "=" + uri.getLastPathSegment());
			break;

		case STORES_MULTI_ROWS:
			queryBuilder.setTables(StoresTable.TABLE_STORES);
			checkStoresColumnNames(projection);
			break;

		case STORES_SINGLE_ROW:
			queryBuilder.setTables(StoresTable.TABLE_STORES);
			checkStoresColumnNames(projection);
			queryBuilder.appendWhere(StoresTable.COL_STORE_ID + "=" + uri.getLastPathSegment());
			break;

		default:
			throw new IllegalArgumentException("Method query. Unknown URI: " + uri);
		}

		// Execute the query on the database
		SQLiteDatabase db = null;
		try {
			db = database.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = database.getReadableDatabase();
		}

		if (null != db) {
			String groupBy = null;
			String having = null;
			Cursor cursor = null;
			try {
				cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in AListContentProvider:query. ", e.toString());
			}

			if (null != cursor) {
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
			}
			return cursor;
		} else {
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String rowID = null;
		int updateCount = 0;

		// Open a WritableDatabase database to support the update transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS_MULTI_ROWS:
			values.put(ItemsTable.COL_DATE_TIME_LAST_USED, Calendar.getInstance().getTimeInMillis());
			updateCount = db.update(ItemsTable.TABLE_ITEMS, values, selection, selectionArgs);
			break;

		case ITEMS_SINGLE_ROW:
			// Limit update to a single row
			rowID = uri.getLastPathSegment();
			selection = ItemsTable.COL_ITEM_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the update
			values.put(ItemsTable.COL_DATE_TIME_LAST_USED, Calendar.getInstance().getTimeInMillis());
			updateCount = db.update(ItemsTable.TABLE_ITEMS, values, selection, selectionArgs);
			break;

		case LIST_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
			break;

		case LIST_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = ListsTable.COL_LIST_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the update
			updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
			break;

		case GROUPS_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(GroupsTable.TABLE_GROUPS, values, selection, selectionArgs);
			break;

		case GROUPS_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = GroupsTable.COL_GROUP_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the update
			updateCount = db.update(GroupsTable.TABLE_GROUPS, values, selection, selectionArgs);
			break;

		case STORES_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(StoresTable.TABLE_STORES, values, selection, selectionArgs);
			break;

		case STORES_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = StoresTable.COL_STORE_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the update
			updateCount = db.update(StoresTable.TABLE_STORES, values, selection, selectionArgs);
			break;

		/*		case LISTS_MULTI_ROWS:
					// Perform the update
					updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
					break;

				case LISTS_SINGLE_ROW:
					// Limit deletion to a single row
					rowID = uri.getLastPathSegment();
					selection = ListsTable.COL_ID + "=" + rowID
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
					// Perform the update
					updateCount = db.update(ListsTable.TABLE_LISTS, values, selection, selectionArgs);
					break;*/

		default:
			throw new IllegalArgumentException("Method update: Unknown URI: " + uri);
		}

		// Notify any observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

	/* From: http://stackoverflow.com/questions/14090695/how-to-use-join-query-in-cursorloader-when-its-constructor-does-not-support-it
	 
	The Uri does not point to any table. It points to whatever you feel like pointing it to.

	Let's pretend that your two tables are Customer and Order. One customer may have many orders. 
	You want to execute a query to get all outstanding orders... but you want to join in some customer-related columns 
	that you will need, such as the customer's name.

	Let's further pretend that you already have 
	content://your.authority.goes.here/customer and content://your.authority.goes.here/order 
	defined to purely query those tables.

	You have two choices:

	(1)	Add the join of the customer's display name on your /order Uri. 
		Having another available column probably will not break any existing consumers 
		of the provider (though testing is always a good idea). This is what ContactsContract does -- 
		it joins in some base columns, like the contact's name, on pretty much all queries of all tables.
		
	(2)	Create content://your.authority.goes.here/orderWithCust 
		that does the same basic query as /order does, but contains your join. 
		In this case, you could have insert(), update(), and delete() throw some sort of RuntimeException, 
		to remind you that you should not be modifying data using /orderWithCust as a Uri.

	In the end, designing a ContentProvider Uri system is similar to designing a REST Web service's URL system. 
	In both cases, the join has to be done on the provider/server side, and so you may need to break 
	the one-table-to-one-URL baseline to offer up some useful joins.*/

	private void checkItemsColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(ItemsTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkItemsColumnNames: Unknown MasterListItemsTable column name!");
			}
		}
	}

	private void checkListsColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(ListsTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkListsColumnNames: Unknown ListTitlesTable column name!");
			}
		}
	}

	private void checkGroupsColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(GroupsTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkListsColumnNames: Unknown ListTitlesTable column name!");
			}
		}
	}

	private void checkStoresColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(StoresTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkListsColumnNames: Unknown ListTitlesTable column name!");
			}
		}
	}

	/**
	 * A test package can call this to get a handle to the database underlying
	 * AListContentProvider, so it can insert test data into the database. The
	 * test case class is responsible for instantiating the provider in a test
	 * context; {@link android.test.ProviderTestCase2} does this during the call
	 * to setUp()
	 * 
	 * @return a handle to the database helper object for the provider's data.
	 */
	public AListDatabaseHelper getOpenHelperForTest() {
		return database;
	}
}
