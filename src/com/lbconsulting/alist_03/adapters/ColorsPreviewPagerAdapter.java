package com.lbconsulting.alist_03.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.fragments.ColorsPreviewFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

//FragmentStatePagerAdapter
//FragmentPagerAdapter
public class ColorsPreviewPagerAdapter extends FragmentStatePagerAdapter {

	private Cursor mAllListsCursor;
	private Context mContext;
	private int mCount;

	public ColorsPreviewPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.mContext = context;
		setAllListsCursor();
	}

	@Override
	public Fragment getItem(int position) {
		long listID = getlistID(position);
		MyLog.d("ColorsPreviewPagerAdapter", "getItem - position=" + position + "; listID=" + listID);
		Fragment newColorsPreviewFragment = ColorsPreviewFragment.newInstance(listID);
		return newColorsPreviewFragment;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	private Cursor setAllListsCursor() {
		mAllListsCursor = ListsTable.getAllLists(mContext);
		mCount = mAllListsCursor.getCount();
		return mAllListsCursor;
	}

	private long getlistID(int position) {
		long listID = -1;
		try {
			mAllListsCursor.moveToPosition(position);
			listID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
		} catch (Exception e) {
			MyLog.d("ColorsPreviewPagerAdapter", "Exception in getlistID: " + e);
		}
		return listID;
	}
}
