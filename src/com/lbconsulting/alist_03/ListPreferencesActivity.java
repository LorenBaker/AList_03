package com.lbconsulting.alist_03;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lbconsulting.alist_03.adapters.ListPreferencesPagerAdaptor;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListPreferencesActivity extends FragmentActivity {

	private long mActiveListID = -1;
	private boolean mTwoFragmentLayout;
	private ListPreferencesPagerAdaptor mListPreferencesPagerAdapter;
	private ViewPager mPager;
	private Cursor mAllListsCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("ListPreferences_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_preferences_pager);

		View frag_colors_placeholder = this.findViewById(R.id.frag_colors_placeholder);
		mTwoFragmentLayout = frag_colors_placeholder != null
				&& frag_colors_placeholder.getVisibility() == View.VISIBLE;

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);

		mAllListsCursor = ListsTable.getAllLists(this);

		mListPreferencesPagerAdapter = new ListPreferencesPagerAdaptor(getSupportFragmentManager(), this);

		mPager = (ViewPager) findViewById(R.id.listPreferencesPager);
		mPager.setAdapter(mListPreferencesPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				SetActiveListID(position);
				MyLog.d("ListPreferences_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = "
						+ mActiveListID);

				if (mTwoFragmentLayout) {
					LoadColorsFragment();
				}
			}
		});

		if (mTwoFragmentLayout) {
			LoadColorsFragment();
		}

		if (mTwoFragmentLayout) {

		} else {

		}
	}

	private void LoadColorsFragment() {
		// TODO code LoadColorsFragment

	}

	protected void SetActiveListID(int position) {
		if (mAllListsCursor != null) {
			long listID = -1;
			try {
				mAllListsCursor.moveToPosition(position);
				listID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
			} catch (Exception e) {
				MyLog.d("ListPreferences_ACTIVITY", "Exception in getlistID: " + e);
			}
			mActiveListID = listID;
		}
	}

	@Override
	protected void onStart() {
		MyLog.i("ListPreferences_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("ListPreferences_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("ListPreferences_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("ListPreferences_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("ListPreferences_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("ListPreferences_ACTIVITY", "onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("ListPreferences_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

}