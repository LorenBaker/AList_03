package com.lbconsulting.alist_03.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ItemsCursorAdaptor extends CursorAdapter {
	ListSettings mListSettings;

	public ItemsCursorAdaptor(Context context, Cursor c, int flags, ListSettings listSettings) {
		super(context, c, flags);
		this.mListSettings = listSettings;
		MyLog.i("ItemsCursorAdaptor", "ItemsCursorAdaptor constructor.");
	}

	private void ShowSeperator(TextView tv) {
		// TODO logic for showing the separator
		tv.setVisibility(View.GONE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView tvListItemSeparator = (TextView) view.findViewById(R.id.tvListItemSeparator);
		if (tvListItemSeparator != null) {
			if (mListSettings.getShowGroupsInListsFragment()) {
				ShowSeperator(tvListItemSeparator);
			} else {
				tvListItemSeparator.setVisibility(View.GONE);
			}
		}

		int isStruckOut = cursor.getInt(cursor.getColumnIndex(ItemsTable.COL_STRUCK_OUT));
		TextView tvListItemName = (TextView) view.findViewById(R.id.tvListItemName);
		if (tvListItemName != null) {
			tvListItemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NAME)));

			if (isStruckOut > 0) {
				// item has been struck out
				tvListItemName.setTypeface(null, Typeface.ITALIC);
				tvListItemName.setTextColor(this.mListSettings.getItemStrikeoutTextColor());
				tvListItemName.setPaintFlags(tvListItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				// item is NOT struck out
				tvListItemName.setTypeface(null, Typeface.NORMAL);
				tvListItemName.setTextColor(this.mListSettings.getItemNormalTextColor());
				tvListItemName.setPaintFlags(tvListItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			}
		}

		TextView tvListItemNote = (TextView) view.findViewById(R.id.tvListItemNote);
		if (tvListItemNote != null) {
			// if a note exists ... then show it
			String note = cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NOTE));
			if (!note.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				sb.append(cursor.getString(cursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NOTE)));
				sb.append(")");
				tvListItemNote.setText(sb.toString());
				if (isStruckOut > 0) {
					// item has been struck out
					tvListItemNote.setTypeface(null, Typeface.ITALIC);
					tvListItemNote.setTextColor(this.mListSettings.getItemStrikeoutTextColor());
					tvListItemNote.setPaintFlags(tvListItemNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					// item is NOT struck out
					tvListItemNote.setTypeface(null, Typeface.NORMAL);
					tvListItemNote.setTextColor(this.mListSettings.getItemNormalTextColor());
					tvListItemNote.setPaintFlags(tvListItemNote.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
				}
				tvListItemNote.setVisibility(View.VISIBLE);
			} else {
				// no note exists ... 
				tvListItemNote.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.row_list_items, parent, false);
		return view;
	}

}
