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
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ManageLocationsCursorAdaptor extends CursorAdapter {

	public ManageLocationsCursorAdaptor(Context context, Cursor c, int flags) {
		super(context, c, flags);
		MyLog.i("ManageLocationsCursorAdaptor", "ManageLocationsCursorAdaptor constructor.");
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (cursor != null) {

			int isCheckedValue = cursor.getInt(cursor.getColumnIndexOrThrow(GroupsTable.COL_CHECKED));
			boolean isChecked = AListUtilities.intToBoolean(isCheckedValue);
			CheckBox ckBox = (CheckBox) view.findViewById(R.id.ckBox);
			if (ckBox != null) {
				ckBox.setChecked(isChecked);
			}

			TextView tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
			if (tvGroupName != null) {
				tvGroupName.setText(cursor.getString(cursor.getColumnIndexOrThrow(GroupsTable.COL_GROUP_NAME)));
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.row_manage_locations, parent, false);
		return view;
	}

}
