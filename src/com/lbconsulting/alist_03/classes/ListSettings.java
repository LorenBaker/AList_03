package com.lbconsulting.alist_03.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.AListUtilities;

public class ListSettings {

	private Context context;
	private long listID;
	private Cursor listCursor;

	public ListSettings(Context context, long listID) {
		this.context = context;
		this.listID = listID;
		this.RefreshListSettings();
		if (this.getTitleBackgroundColor() == -1) {
			SetDefaultColors();
			ListsTable.setListPreferencesDefaults(context, listID);
			this.RefreshListSettings();
		}
	}

	public void RefreshListSettings() {
		this.listCursor = ListsTable.getList(context, listID);
		if (listCursor != null) {
			this.listCursor.moveToFirst();
		}
	}

	private void SetDefaultColors() {
		//Resources res = context.getResources();
		if (listID > 0) {
			// select the new list title's colors
			ContentValues newDefaultValues = new ContentValues();
			int selector = (int) listID % 6;
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
						AListUtilities.GetColorInt("#EFECCA"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#002F2F"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#A7A37E"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#E6E2AF"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);

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
						AListUtilities.GetColorInt("#ADD5F7"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#16193B"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#35478C"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#7FB2F0"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);
				break;

			case 2:
				// This Green
				newDefaultValues.put(ListsTable.COL_TITLE_BACKGROUND_COLOR, AListUtilities.GetColorInt("#00261C"));
				newDefaultValues.put(ListsTable.COL_TITLE_TEXT_COLOR, AListUtilities.GetColorInt("#96ED89"));

				newDefaultValues.put(ListsTable.COL_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#168039"));
				newDefaultValues.put(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR, AListUtilities.GetColorInt("#96ED89"));
				newDefaultValues.put(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR, AListUtilities.GetColorInt("#044D29"));

				newDefaultValues
						.put(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR, AListUtilities.GetColorInt("#168039"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR,
						AListUtilities.GetColorInt("#96ED89"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#00261C"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#044D29"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#45BF55"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);
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
						AListUtilities.GetColorInt("#2F343B"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#E3CDA4"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#C77966"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#2F343B"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);

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
						AListUtilities.GetColorInt("#CC5400"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#B31E00"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#B33600"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#FFC887"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);
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
						AListUtilities.GetColorInt("#F1F2D8"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#425955"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#BFBD9F"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#778C7A"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);

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
						AListUtilities.GetColorInt("#EFECCA"));
				newDefaultValues.put(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR,
						AListUtilities.GetColorInt("#002F2F"));

				newDefaultValues.put(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR, AListUtilities.GetColorInt("#A7A37E"));
				newDefaultValues.put(ListsTable.COL_SEPARATOR_TEXT_COLOR, AListUtilities.GetColorInt("#E6E2AF"));

				ListsTable.UpdateListsTableFieldValues(context, listID, newDefaultValues);
				break;
			}
		}
	}

	public long getListID() {
		return listID;
	}

	public String getListTitle() {
		return listCursor.getString(listCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE));
	}

	public long getStoreID() {
		return listCursor.getLong(listCursor.getColumnIndexOrThrow(ListsTable.COL_STORE_ID));
	}

	public boolean getShowGroupsInListsFragment() {
		int value = listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_SHOW_GROUPS_IN_LISTS_FRAGMENT));
		return AListUtilities.intToBoolean(value);
	}

	public boolean getShowGroupsInMasterListFragment() {
		int value = listCursor.getInt(listCursor
				.getColumnIndexOrThrow(ListsTable.COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT));
		return AListUtilities.intToBoolean(value);
	}

	public boolean getShowStores() {
		int value = listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_SHOW_STORES));
		return AListUtilities.intToBoolean(value);
	}

	public boolean getDeleteNoteUponDeselectingItem() {
		int value = listCursor.getInt(listCursor
				.getColumnIndexOrThrow(ListsTable.COL_DELETE_NOTE_UPON_DESELECTING_ITEM));
		return AListUtilities.intToBoolean(value);
	}

	public int getListSortOrder() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_SORT_ORDER));
	}

	public int getMasterListSortOrder() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_SORT_ORDER));
	}

	public int getTitleBackgroundColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_TITLE_BACKGROUND_COLOR));
	}

	public int getTitleTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_TITLE_TEXT_COLOR));
	}

	public int getSeparatorBackgroundColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_SEPARATOR_BACKGROUND_COLOR));
	}

	public int getSeparatorTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_SEPARATOR_TEXT_COLOR));
	}

	public int getListBackgroundColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_BACKGROUND_COLOR));
	}

	public int getItemNormalTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_ITEM_NORMAL_TEXT_COLOR));
	}

	public int getItemStrikeoutTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_ITEM_STRIKEOUT_TEXT_COLOR));
	}

	public int getMasterListBackgroundColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_BACKGROUND_COLOR));
	}

	public int getMasterListItemNormalTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_ITEM_NORMAL_TEXT_COLOR));
	}

	public int getMasterListItemSelectedTextColor() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_MASTER_LIST_ITEM_SELECTED_TEXT_COLOR));
	}

	public int getListViewFirstVisiblePosition() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_LISTVIEW_FIRST_VISIBLE_POSITION));
	}

	public int getListViewTop() {
		return listCursor.getInt(listCursor.getColumnIndexOrThrow(ListsTable.COL_LISTVIEW_TOP));
	}

	public void updateListsTableFieldValues(ContentValues newFieldValues) {
		ListsTable.UpdateListsTableFieldValues(context, listID, newFieldValues);
		this.RefreshListSettings();
	}

	protected void finalize() {
		if (this.listCursor != null) {
			this.listCursor.close();
		}
	}
}
