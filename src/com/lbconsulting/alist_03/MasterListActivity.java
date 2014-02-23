package com.lbconsulting.alist_03;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lbconsulting.alist_03.database.ListsTable;
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
	private int mActiveListPosition = 0;

	private Cursor mAllListsCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyLog.i("MasterList_ACTIVITY", "onCreate");
		setContentView(R.layout.activity_master_list);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", 0);

		mAllListsCursor = ListsTable.getAllLists(this);

		// create the master list fragment
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
		Toast.makeText(this, " Master List: Edit Item is under construction.", Toast.LENGTH_SHORT).show();
		mActiveListID = itemID;

	}
}