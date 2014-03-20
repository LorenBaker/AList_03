package com.lbconsulting.alist_03;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ListsPagerAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.ListsDialogFragment;
import com.lbconsulting.alist_03.fragments.ListPreferencesFragment;
import com.lbconsulting.alist_03.fragments.MasterListFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsActivity extends FragmentActivity {

	private ListsPagerAdapter mListsPagerAdapter;
	private ViewPager mPager;

	private MasterListFragment mMasterListFragment;
	private Boolean mTwoFragmentLayout = false;

	private long NO_ACTIVE_LIST_ID = 0;
	private long mActiveListID = NO_ACTIVE_LIST_ID;
	private long mActiveItemID;
	private int mActiveListPosition = 0;
	private ListSettings mListSettings;

	private Cursor mAllListsCursor;
	private BroadcastReceiver mListTableChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lists_pager);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);

		mListTableChanged = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.hasExtra("editedListTitle")) {
					// the list title has changed ...
				}

				if (intent.hasExtra("newListID")) {
					// a new list has been created ...
					mActiveListID = intent.getLongExtra("newListID", 0);
				}

				// restart activity to ensure that all lists are shown in
				// alphabetical order
				ReStartListsActivity();
			}
		};
		// Register to receive messages.
		String key = String.valueOf(mActiveListID) + ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mListTableChanged, new IntentFilter(key));

		if (mActiveListID < 2) {
			return;
		}

		View frag_masterList_placeholder = this.findViewById(R.id.frag_masterList_placeholder);
		mTwoFragmentLayout = frag_masterList_placeholder != null
				&& frag_masterList_placeholder.getVisibility() == View.VISIBLE;

		MyLog.i("Lists_ACTIVITY", "onCreate - ViewPager");

		mAllListsCursor = ListsTable.getAllLists(this);
		mListSettings = new ListSettings(this, mActiveListID);

		mListsPagerAdapter = new ListsPagerAdapter(getSupportFragmentManager(), this);
		mPager = (ViewPager) findViewById(R.id.listsPager);
		mPager.setAdapter(mListsPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				// A list page has been selected
				SetActiveListID(position);
				SetActiveListBroadcastReceivers();
				MyLog.d("Lists_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = " + mActiveListID);

				if (mTwoFragmentLayout) {
					LoadMasterListFragment();
				}
			}
		});

		if (mTwoFragmentLayout) {
			LoadMasterListFragment();
		}
	}

	private void SetActiveListID(int position) {
		if (mAllListsCursor != null) {
			try {
				mAllListsCursor.moveToPosition(position);
				mActiveListID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
				mListSettings = new ListSettings(this, mActiveListID);
				mActiveListPosition = position;
			} catch (Exception e) {
				MyLog.d("Lists_ACTIVITY", "Exception in getlistID: " + e);
			}
		}
	}

	private void SetActiveListBroadcastReceivers() {
		// Unregister old receiver
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mListTableChanged);

		// Register new receiver
		String key = String.valueOf(mActiveListID) + ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mListTableChanged, new IntentFilter(key));
	}

	private void ReStartListsActivity() {
		mAllListsCursor = ListsTable.getAllLists(this);
		mActiveListPosition = AListUtilities.getListsCursorPositon(mAllListsCursor, mActiveListID);
		Intent intent = new Intent(this, ListsActivity.class);
		// prohibit the back button from displaying previous version of this ListPreferencesActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void ReStartListsActivity(int position) {
		mAllListsCursor = ListsTable.getAllLists(this);
		if (mAllListsCursor.getCount() >= position + 1) {
			mActiveListPosition = position;
		} else {
			if (mAllListsCursor.getCount() > 0) {
				mActiveListPosition = 0;
			} else {
				// there are no lists in the ListsTable!
				mActiveListPosition = -1;
				mActiveListID = -1;
			}

		}
		if (mActiveListPosition > -1) {
			mActiveListID = AListUtilities.getIdByPosition(mAllListsCursor, mActiveListPosition);
		}
		Intent intent = new Intent(this, ListsActivity.class);
		// prohibit the back button from displaying previous version of this ListPreferencesActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void StartMasterListActivity() {
		Intent masterListActivityIntent = new Intent(this, MasterListActivity.class);
		// masterListActivityIntent.putExtra("ActiveListID", mActiveListID);
		startActivity(masterListActivityIntent);
	}

	private void StartManageLocationsActivity() {
		Intent intent = new Intent(this, ManageLocationsActivity.class);
		intent.putExtra("listTitle", mListSettings.getListTitle());
		intent.putExtra("titleBackgroundColor", mListSettings.getTitleBackgroundColor());
		intent.putExtra("titleTextColor", mListSettings.getTitleTextColor());
		startActivity(intent);
	}

	private void StartListPreferencesActivity() {
		Intent intent = new Intent(this, ListPreferencesActivity.class);
		startActivity(intent);
	}

	private void StartAboutActivity() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	private void LoadMasterListFragment() {
		mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager().findFragmentByTag("MasterListFragment");
		if (mMasterListFragment == null) {
			// create MasterListFragment
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = " + mActiveListID);
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. MasterListFragment ADD. ListID = " + mActiveListID);
		} else {
			// MasterListFragment exists ... so replace it
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = " + mActiveListID);
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. MasterListFragment REPLACE. ListID = " + mActiveListID);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		MyLog.i("Lists_ACTIVITY", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		MyLog.i("Lists_ACTIVITY", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		MyLog.i("Lists_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("Lists_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("Lists_ACTIVITY", "onResume");
		if (mActiveListID < 2) {
			CreatNewList();
		} else {
			mPager.setCurrentItem(mActiveListPosition);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Lists_ACTIVITY", "onPause");

		// save activity state
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putLong("ActiveItemID", mActiveItemID);
		applicationStates.putInt("ActiveListPosition", mActiveListPosition);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("Lists_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Lists_ACTIVITY", "onDestroy");
		if (mAllListsCursor != null) {
			mAllListsCursor.close();
		}
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mListTableChanged);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("Lists_ACTIVITY", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.lists_1activity, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_removeStruckOffItems:
			ItemsTable.UnStrikeAndDeselectAllStruckOutItems(this, mActiveListID,
					mListSettings.getDeleteNoteUponDeselectingItem());
			// SendRestartItemsLoaderBroadCast();
			return true;

		case R.id.action_addItem:
			StartMasterListActivity();
			return true;

		case R.id.action_newList:
			CreatNewList();
			return true;

		case R.id.action_clearList:
			ItemsTable
					.DeselectAllItemsInList(this, mActiveListID, mListSettings.getDeleteNoteUponDeselectingItem());
			return true;

		case R.id.action_emailList:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_editListTitle:
			EditListTitle();
			return true;

		case R.id.action_deleteList:
			DeleteList();
			return true;

		case R.id.action_manageLocations:
			StartManageLocationsActivity();
			return true;

		case R.id.action_Preferences:
			StartListPreferencesActivity();
			return true;

		case R.id.action_about:
			StartAboutActivity();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	private void DeleteList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// set title
		builder.setTitle("Delete List");

		String msg = "Permanently delete " + "\"" + mListSettings.getListTitle() + "\" ?";
		// set dialog message
		builder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						ListsTable.DeleteList(ListsActivity.this, mActiveListID);
						ReStartListsActivity(mActiveListPosition);
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

	private void CreatNewList() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_lists_table_update");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		ListsDialogFragment editListTitleDialog = ListsDialogFragment
				.newInstance(mActiveListID, ListsDialogFragment.NEW_LIST);
		editListTitleDialog.show(fm, "dialog_lists_table_update");
	}

	private void EditListTitle() {

		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_lists_table_update");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		ListsDialogFragment editListTitleDialog = ListsDialogFragment
				.newInstance(mActiveListID, ListsDialogFragment.EDIT_LIST_TITLE);
		editListTitleDialog.show(fm, "dialog_lists_table_update");
	}

}