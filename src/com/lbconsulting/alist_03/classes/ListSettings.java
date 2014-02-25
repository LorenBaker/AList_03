package com.lbconsulting.alist_03.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.AListUtilities;

public class ListSettings {

	private Context context;
	private long mListID;
	private Cursor mListCursor;

	public ListSettings(Context context, long listID) {
		this.context = context;
		this.mListID = listID;
		this.RefreshListSettings();
		if (this.getTitleBackgroundColor() == -1) {
			SetDefaultColors();
			ListsTable.setListPreferencesDefaults(context, listID);
			this.RefreshListSettings();
		}
	}

	public void RefreshListSettings() {
		this.mListCursor = ListsTable.getList(context, mListID);
		if (mListCursor != null) {
			this.mListCursor.moveToFirst();
		}
	}

	private void SetDefaultColors() {
		//Resources res = context.getResources();
		if (mListID > 0) {
			// select the new list title's colors
			ContentValues newDefaultValues = new ContentValues();
			int selector = (int) mListID % 6;
			switch (selector) {
			case 0:
				// Sandy Stone
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#002F2F"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#EFECCA"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#046380"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#EFECCA"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#A7A37E"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#046380"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#002F2F"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#EFECCA"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#A7A37E"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#E6E2AF"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);

				break;

			case 1:
				// Blue Sky
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#16193B"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#ADD5F7"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#4E7AC7"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#ADD5F7"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#7FB2F0"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#4E7AC7"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#16193B"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#ADD5F7"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#35478C"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#7FB2F0"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);
				break;

			case 2:
				// This Green
				// Master Selected == Item Normal
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#00261C"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#96ED89"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#168039"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#96ED89"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#044D29"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#168039"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#00261C"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#96ED89"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#044D29"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#45BF55"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);
				break;

			case 3:
				// Vintage Ralph Lauren
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#E3CDA4"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#2F343B"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#7E827A"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#2F343B"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#703030"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#7E827A"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#E3CDA4"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#2F343B"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#C77966"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#2F343B"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);

				break;

			case 4:
				// Five Oranges
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#B31E00"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#FFC887"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#FFC887"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#CC5400"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#FF8A00"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#FFC887"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#B31E00"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#CC5400"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#B33600"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#FFC887"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);
				break;

			case 5:
				// Winter Road
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#425955"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#F1F2D8"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#778C7A"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#F1F2D8"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#BFBD9F"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#778C7A"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#425955"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#F1F2D8"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#BFBD9F"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#778C7A"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);

				break;

			default:
				// Sandy Stone
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#002F2F"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#EFECCA"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#046380"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#EFECCA"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#A7A37E"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#046380"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#002F2F"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#EFECCA"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#A7A37E"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#E6E2AF"));

				ListsTable.UpdateListsTableFieldValues(context, mListID, newDefaultValues);
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

	public long getStoreID() {
		return mListCursor.getLong(mListCursor.getColumnIndexOrThrow(ListsTable.COL_STORE_ID));
	}

	public boolean getShowGroupsInListsFragment() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_SHOW_GROUPS_IN_LISTS_FRAGMENT));
		return AListUtilities.intToBoolean(value);
	}

	public boolean getShowGroupsInMasterListFragment() {
		int value = mListCursor.getInt(mListCursor
				.getColumnIndexOrThrow(ListsTable.COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT));
		return AListUtilities.intToBoolean(value);
	}

	public boolean getShowStores() {
		int value = mListCursor.getInt(mListCursor.getColumnIndexOrThrow(ListsTable.COL_SHOW_STORES));
		return AListUtilities.intToBoolean(value);
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
		ListsTable.UpdateListsTableFieldValues(context, mListID, newFieldValues);
		this.RefreshListSettings();
	}

	protected void finalize() {
		if (this.mListCursor != null) {
			this.mListCursor.close();
		}
	}
}
