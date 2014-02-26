package com.lbconsulting.alist_03.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class CheckItemsCursorAdaptor extends CursorAdapter {
	Context mAdaptorContext;
	ListSettings mListSettings;

	public CheckItemsCursorAdaptor(Context context, Cursor c, int flags, ListSettings listSettings) {
		super(context, c, flags);
		this.mAdaptorContext = context;
		this.mListSettings = listSettings;
		MyLog.i("CheckItemsCursorAdaptor", "CheckItemsCursorAdaptor constructor.");
	}

	private void ShowSeperator(TextView tv) {
		// TODO logic for showing the separator
		//tv.setVisibility(View.GONE);
		tv.setVisibility(View.VISIBLE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (cursor != null) {
			TextView tvListItemSeparator = (TextView) view.findViewById(R.id.tvListItemSeparator);
			if (tvListItemSeparator != null) {
				if (mListSettings.getShowGroupsInMasterListFragment()) {
					ShowSeperator(tvListItemSeparator);
				} else {
					tvListItemSeparator.setVisibility(View.GONE);
				}
			}

			int isCheckedValue = cursor.getInt(cursor.getColumnIndex(ItemsTable.COL_CHECKED));
			boolean isChecked = AListUtilities.intToBoolean(isCheckedValue);
			CheckBox ckBox = (CheckBox) view.findViewById(R.id.ckBox);
			if (ckBox != null) {
				ckBox.setChecked(isChecked);
				ckBox.setTextColor(this.mListSettings.getItemNormalTextColor());
				ckBox.setVisibility(View.VISIBLE);
			}

			TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
			if (tvItemName != null) {
				tvItemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NAME)));
				tvItemName.setTextColor(this.mListSettings.getItemNormalTextColor());
			}

			TextView tvItemNote = (TextView) view.findViewById(R.id.tvItemNote);
			if (tvItemNote != null) {
				// if a note exists ... then show it
				String note = cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NOTE));
				if (note != null && !note.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append("(");
					sb.append(cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NOTE)));
					sb.append(")");
					tvItemNote.setText(sb.toString());
					tvItemNote.setTextColor(this.mListSettings.getItemNormalTextColor());
					tvItemNote.setVisibility(View.VISIBLE);
				} else {
					// no note exists ... 
					tvItemNote.setVisibility(View.GONE);
				}
			}

			TextView tvItemGroup = (TextView) view.findViewById(R.id.tvItemGroup);
			if (tvItemGroup != null) {
				if (mListSettings.getShowGroupsInMasterListFragment()) {
					long groupID = cursor.getLong(cursor.getColumnIndexOrThrow(ItemsTable.COL_GROUP_ID));
					tvItemGroup.setText(GroupsTable.getGroupName(mAdaptorContext, groupID));
					tvItemGroup.setTextColor(this.mListSettings.getItemNormalTextColor());
					tvItemGroup.setVisibility(View.VISIBLE);
				} else {
					// no note exists ... 
					tvItemGroup.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.row_master_list_item, parent, false);
		return view;
	}

}
