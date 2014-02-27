package com.lbconsulting.alist_03;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.CheckItemsPagerAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.MoveCheckedItemsDialogFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class CheckItemsActivity extends FragmentActivity {
	private CheckItemsPagerAdapter mCheckItemsPagerAdapter;
	private ViewPager mPager;

	private long mActiveListID = -1;
	private long mActiveItemID = -1;
	private int mActiveListPosition = -1;
	private ListSettings mListSettings;
	private Cursor mAllListsCursor;
	private BroadcastReceiver mItemsMovedReceiver;
	private long mSelectedListID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("CheckItems_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_check_items_pager);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);

		mItemsMovedReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.hasExtra("selectedListID")) {
					// the new list ID has been selected ...
					mSelectedListID = intent.getLongExtra("selectedListID", -1);
					int numberOfItemsMoved = ItemsTable.MoveAllCheckedItemsInList(CheckItemsActivity.this,
							mActiveListID, mSelectedListID);

				}
			}
		};
		// Register to receive messages.
		String key = String.valueOf(mActiveListID) + ItemsTable.ITEM_MOVE_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mItemsMovedReceiver, new IntentFilter(key));

		getActionBar().setTitle("Cull or Move Items");

		mAllListsCursor = ListsTable.getAllLists(this);
		mListSettings = new ListSettings(this, mActiveListID);

		mCheckItemsPagerAdapter = new CheckItemsPagerAdapter(getSupportFragmentManager(), this);
		mPager = (ViewPager) findViewById(R.id.checkItemsPager);
		mPager.setAdapter(mCheckItemsPagerAdapter);
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
				MyLog.d("CheckItems_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = "
						+ mActiveListID);
			}
		});
	}

	protected void SetActiveListBroadcastReceivers() {
		// Unregister old receiver
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mItemsMovedReceiver);
		// Register new receiver
		String key = String.valueOf(mActiveListID) + ItemsTable.ITEM_MOVE_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mItemsMovedReceiver, new IntentFilter(key));
	}

	@Override
	protected void onStart() {
		MyLog.i("CheckItems_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("CheckItems_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("CheckItems_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);

		if (mActiveListPosition > -1) {
			mPager.setCurrentItem(mActiveListPosition);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("CheckItems_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putInt("ActiveListPosition", mActiveListPosition);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("CheckItems_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.check_items_1activity, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_deleteCheckedItems:
			DeleteCheckedItems();
			return true;

		case R.id.action_clearAllCheckedItems:
			ClearAllCheckedItems();
			return true;

		case R.id.action_moveCheckedItmes:
			MoveCheckedItems();
			//Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_checkUnused90:
			CheckUnused(90);
			return true;

		case R.id.action_checkUnused180:
			CheckUnused(180);
			return true;

		case R.id.action_checkUnused365:
			CheckUnused(365);
			return true;

		case R.id.action_sortOrder:
			ChangeSortOrder();
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
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
				MyLog.d("CheckItems_ACTIVITY", "Exception in getlistID: " + e);
			}
		}
	}

	private void DeleteCheckedItems() {
		int numberOfCheckedItems = ItemsTable.getNumberOfCheckedItmes(this, mActiveListID);

		if (numberOfCheckedItems > -1) {
			Resources res = getResources();
			String numberOfCheckedItemsFound = res.getQuantityString(R.plurals.numberOfCheckedItems,
					numberOfCheckedItems, numberOfCheckedItems);
			String msg = "";

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// set title
			builder.setTitle(R.string.dialog_title_delete_all_checked_items);

			if (numberOfCheckedItems > 0) {
				msg = "Permanently delete " + numberOfCheckedItemsFound + "?";
				builder
						.setMessage(msg)
						.setCancelable(false)
						.setPositiveButton(R.string.btn_yes_text, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ItemsTable.DeleteAllCheckedItemsInList(CheckItemsActivity.this, mActiveListID);
							}
						})
						.setNegativeButton(R.string.btn_no_text, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// close the dialog box and do nothing
								dialog.cancel();
							}
						});
			} else {
				//numberOfCheckedItems == 0
				msg = numberOfCheckedItemsFound + "!";
				builder
						.setMessage(msg)
						.setCancelable(false)
						.setPositiveButton(R.string.btn_ok_text, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// close the dialog box and do nothing
								dialog.cancel();
							}
						});
			}

			// create alert dialog
			AlertDialog alertDialog = builder.create();
			// show it
			alertDialog.show();
		}
	}

	private void ClearAllCheckedItems() {
		ItemsTable.UnCheckAllItemsInList(CheckItemsActivity.this, mActiveListID);
	}

	private void MoveCheckedItems() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment prev = fm.findFragmentByTag("dialog_move_checked_items");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}
		int numberOfCheckedItems = ItemsTable.getNumberOfCheckedItmes(this, mActiveListID);
		MoveCheckedItemsDialogFragment moveCheckedItemsDialog = MoveCheckedItemsDialogFragment.newInstance(
				mActiveListID, numberOfCheckedItems);
		moveCheckedItemsDialog.show(fm, "dialog_move_checked_items");
	}

	private void CheckUnused(long numberOfDays) {
		ItemsTable.CheckItemsUnused(this, mActiveListID, numberOfDays);
	}

	private void ChangeSortOrder() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		MyLog.i("CheckItems_ACTIVITY", "onDestroy");
		if (mAllListsCursor != null) {
			mAllListsCursor.close();
		}
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mItemsMovedReceiver);
		super.onDestroy();
	}
}