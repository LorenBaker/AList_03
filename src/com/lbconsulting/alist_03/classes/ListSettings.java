package com.lbconsulting.alist_03.classes;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.AListUtilities;

public class ListSettings {

	private Context mContext;
	private long mListID;
	private Cursor mListCursor;

	public ListSettings(Context context, long listID) {
		this.mContext = context;
		this.mListID = listID;
		this.RefreshListSettings();
		if (this.getTitleBackgroundColor() == -1) {
			SetDefaultColors();
			ListsTable.setListPreferencesDefaults(mContext, listID);
			this.RefreshListSettings();
		}
	}

	public void RefreshListSettings() {
		this.mListCursor = ListsTable.getList(mContext, mListID);
		if (mListCursor != null) {
			this.mListCursor.moveToFirst();
		}
	}

	private void SetDefaultColors() {
		Resources res = mContext.getResources();
		if (mListID > 0) {
			// select the new list title's colors
			ContentValues newDefaultValues = new ContentValues();
			int selector = (int) mListID % 6;
			switch (selector) {
			case 0:
				// Sandy Stone
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset0_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset0_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset0_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset0_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset0_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset0_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset0_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);

				break;

			case 1:
				// Blue Sky
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset1_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset1_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset1_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset1_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset1_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset1_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset1_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset1_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset1_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset1_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);
				break;

			case 2:
				// This Green
				// Master Selected == Item Normal
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset2_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset2_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset2_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset2_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset2_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset2_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset2_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset2_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset2_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset2_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);
				break;

			case 3:
				// Vintage Ralph Lauren
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset3_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset3_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset3_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset3_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset3_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset3_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset3_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset3_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset3_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset3_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);

				break;

			case 4:
				// Five Oranges
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset4_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset4_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset4_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset4_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset4_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset4_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset4_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset4_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset4_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset4_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);
				break;

			case 5:
				// Winter Road
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset5_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset5_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset5_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset5_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset5_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset5_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset5_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset5_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset5_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset5_separator_text));

				ListsTable.UpdateListsTableFieldValues(mContext, mListID, newDefaultValues);

				break;

			default:
				// Sandy Stone
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_title_background));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, res.getColor(R.color.preset0_title_text));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_list_background));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset0_list_normal_text));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR,
						res.getColor(R.color.preset0_list_strikeout_text));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, res.getColor(R.color.preset0_list_background));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						res.getColor(R.color.preset0_list_strikeout_text));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						res.getColor(R.color.preset0_list_normal_text));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR,
						res.getColor(R.color.preset0_separator_background));
				newDefaultValues
						.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, res.getColor(R.color.preset0_separator_text));

				break;
			}
		}
	}

	public long getListID() {
		return mListID;
	}

	public String getListTitle() {
		return mListCursor.getString(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE));
	}

	public long getActiveStoreID() {
		return mListCursor.getLong(mListCursor.getColumnIndexOrThrow(ListsTable.COL_ACTIVE_STORE_ID));
	}

	public boolean isManualSort() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_SORT_ORDER));
		return value == AListUtilities.LIST_SORT_MANUAL;
	}

	public boolean getShowGroupsInListsFragment() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_SORT_ORDER));
		return value == AListUtilities.LIST_SORT_BY_GROUP;
	}

	public boolean getShowGroupsInMasterListFragment() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_SORT_ORDER));
		return value == AListUtilities.MASTER_LIST_SORT_BY_GROUP;
	}

	public boolean getShowStores() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_SORT_ORDER));
		return value == AListUtilities.LIST_SORT_BY_STORE_LOCATION;
	}

	public boolean getDeleteNoteUponDeselectingItem() {
		int value = mListCursor.getInt(mListCursor
				.getColumnIndexOrThrow(ListsTable.COL_DELETE_NOTE_UPON_DESELECTING_ITEM));
		return AListUtilities.intToBoolean(value);
	}

	public int getListSortOrder() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_SORT_ORDER));
	}

	public int getMasterListSortOrder() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_SORT_ORDER));
	}

	public int getTitleBackgroundColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_TITLE_BACKGROUND_COLOR));
	}

	public int getTitleTextColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_TITLE_TEXT_COLOR));
	}

	public int getSeparatorBackgroundColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR));
	}

	public int getSeparatorTextColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_SEPARATOR_TEXT_COLOR));
	}

	public int getListBackgroundColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_BACKGROUND_COLOR));
	}

	public int getItemNormalTextColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR));
	}

	public int getItemStrikeoutTextColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR));
	}

	public int getMasterListBackgroundColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR));
	}

	public int getMasterListItemNormalTextColor() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR));
	}

	public int getMasterListItemSelectedTextColor() {
		return mListCursor.getInt(mListCursor
				.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR));
	}

	public int getListViewFirstVisiblePosition() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LISTVIEW_FIRST_VISIBLE_POSITION));
	}

	public int getListViewTop() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_LISTVIEW_TOP));
	}

	public int getMasterListViewFirstVisiblePosition() {
		return mListCursor.getInt(mListCursor
				.getColumnIndexOrThrow(ListsTable.COL_MASTER_LISTVIEW_FIRST_VISIBLE_POSITION));
	}

	public int getMasterListViewTop() {
		return mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LISTVIEW_TOP));
	}

	public void updateListsTableFieldValues(ContentValues newFieldValues) {
		ListsTable.UpdateListsTableFieldValues(mContext, mListID, newFieldValues);
		this.RefreshListSettings();
	}

	@Override
	protected void finalize() {
		if (this.mListCursor != null) {
			this.mListCursor.close();
		}
	}
}
