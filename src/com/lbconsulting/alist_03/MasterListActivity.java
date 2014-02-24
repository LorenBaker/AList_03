package com.lbconsulting.alist_03;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ListsSpinnerCursorAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.fragments.MasterListFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class MasterListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private MasterListFragment mMasterListFragment;

	private long mActiveListID = 0;
	private int mActiveListPosition = 0;
	private ListSettings mListSettings;

	private ActionBar mActionBar;
	private ListsSpinnerCursorAdapter mListsSpinnerCursorAdapter;
	private LoaderManager mLoaderManager = null;
	private LoaderManager.LoaderCallbacks<Cursor> mListsFragmentCallbacks;
	private static final int LISTS_LOADER_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_list);
		View view = findViewById(R.id.frag_masterList_placeholder);
		if (view == null) {
			finish();
			return;
		}

		MyLog.i("MasterList_ACTIVITY", "onCreate");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", 0);
		mListSettings = new ListSettings(this, mActiveListID);

		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		mListsSpinnerCursorAdapter = new ListsSpinnerCursorAdapter(this, null, 0);

		mActionBar.setListNavigationCallbacks(mListsSpinnerCursorAdapter, new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int listPosition, long listID) {
				mActiveListID = listID;
				mActiveListPosition = listPosition;
				mListSettings = new ListSettings(MasterListActivity.this, mActiveListID);
				LoadMasterListFragment();
				return false;
			}
		});
		mListsFragmentCallbacks = this;
		mLoaderManager = getSupportLoaderManager();
		mLoaderManager.initLoader(LISTS_LOADER_ID, null, mListsFragmentCallbacks);
	}

	private void LoadMasterListFragment() {
		mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager()
				.findFragmentByTag("MasterListFragment");
		if (mMasterListFragment == null) {
			// MasterListFragment does not exists ... so add it
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);
			this.getSupportFragmentManager().beginTransaction()
					.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("MasterList_ACTIVITY", "LoadMasterListFragment. MasterListFragment ADD. ListID = " + mActiveListID);

		} else {

			// MasterListFragment exists ... so replace it
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("MasterList_ACTIVITY", "LoadMasterListFragment. MasterListFragment REPLACE. ListID = "
					+ mActiveListID);
		}
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
		mActiveListPosition = storedStates.getInt("ActiveListPosition", 0);
		;
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("MasterList_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putInt("ActiveListPosition", mActiveListPosition);
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
		getMenuInflater().inflate(R.menu.master_list_1activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_clearAllSelectedItems:
			ItemsTable.DeselectAllItemsInList(this, mActiveListID, mListSettings.getDeleteNoteUponDeselectingItem());
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		case R.id.action_deleteAllItems:
			ItemsTable.DeleteAllItemsInList(this, mActiveListID);
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		case R.id.action_cullItems:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_about:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	@Override
	protected void onDestroy() {
		MyLog.i("MasterList_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("MasterList_ACTIVITY", "onCreateLoader; id = " + id);
		CursorLoader cursorLoader = null;
		try {
			cursorLoader = ListsTable.loadAllLists(this);

		} catch (SQLiteException e) {
			MyLog.e("MasterList_ACTIVITY: onCreateLoader SQLiteException: ", e.toString());

		} catch (IllegalArgumentException e) {
			MyLog.e("MasterList_ACTIVITY: onCreateLoader IllegalArgumentException: ", e.toString());
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		int id = loader.getId();
		MyLog.i("MasterList_ACTIVITY", "onLoadFinished; id = " + id);
		mListsSpinnerCursorAdapter.swapCursor(newCursor);
		getActionBar().setSelectedNavigationItem(mActiveListPosition);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
		MyLog.i("MasterList_ACTIVITY", "onLoaderReset; id = " + id);
		mListsSpinnerCursorAdapter.swapCursor(null);
	}
}