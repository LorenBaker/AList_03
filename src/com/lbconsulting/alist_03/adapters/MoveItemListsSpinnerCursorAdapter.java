package com.lbconsulting.alist_03.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class MoveItemListsSpinnerCursorAdapter extends CursorAdapter {
	//ListSettings mListSettings;

	public MoveItemListsSpinnerCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		//this.mListSettings = listSettings;
		MyLog.i("MoveItemListsSpinnerCursorAdapter", "MoveItemListsSpinnerCursorAdapter constructor.");
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		switch (view.getId()) {
		case R.id.rowLinearLayout:
			TextView rowTextView = (TextView) view.findViewById(R.id.rowTextView);
			if (rowTextView != null) {
				rowTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE)));
				rowTextView.setTextColor(context.getResources().getColor(android.R.color.black));
			}
			break;
		case R.id.rowLinearLayoutDropdown:
			TextView rowTextViewDropdown = (TextView) view.findViewById(R.id.rowTextViewDropdown);
			if (rowTextViewDropdown != null) {
				rowTextViewDropdown.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE)));
				rowTextViewDropdown.setTextColor(context.getResources().getColor(android.R.color.black));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.row_spinner_dropdown, parent, false);
		bindView(v, context, cursor);
		return v;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.row_spinner, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
