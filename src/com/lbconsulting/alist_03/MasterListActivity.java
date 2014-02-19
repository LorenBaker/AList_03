package com.lbconsulting.alist_03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lbconsulting.alist_03.fragments.MasterListFragment;
import com.lbconsulting.alist_03.fragments.MasterListFragment.OnMasterListItemLongClickListener;
import com.lbconsulting.alist_03.utilities.MyLog;

public class MasterListActivity extends FragmentActivity implements OnMasterListItemLongClickListener {

	/*private MasterListPagerAdapter mListsPagerAdapter;
	private ViewPager mPager;*/

	private MasterListFragment mMasterListFragment;
	private long NO_ACTIVE_LIST_ID = 0;
	private long mActiveListID = NO_ACTIVE_LIST_ID;
	private long mActiveItemID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// This activity only shown in portrait orientation
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		MyLog.i("MasterList_ACTIVITY", "onCreate");

		// MTM You really shouldn't do this... it is better to support it with portrait in landscape
		// - then do nothing at all.  It really breaks convention.
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}
		setContentView(R.layout.activity_master_list);
		// get the selected task id
		Intent intent = getIntent();
		mActiveListID = intent.getLongExtra("ActiveListID", 0);

		// create the task details fragment
		mMasterListFragment = MasterListFragment.newInstance(mActiveListID);
		// add the fragment
		this.getSupportFragmentManager().beginTransaction()
				.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
				.commit();
		MyLog.i("MasterList_ACTIVITY", "onCreate. MasterListFragment add.");

	}

	@Override
	protected void onStart() {
		MyLog.i("MasterList_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("MasterList_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("MasterList_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("MasterList_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putLong("ActiveItemID", mActiveItemID);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("MasterList_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("MasterList_ACTIVITY", "onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("MasterList_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onMasterListItemLongClick(int position, long itemID) {
		// TODO Edit Item ... Edit Item Dialog
		mActiveListID = itemID;

	}
}