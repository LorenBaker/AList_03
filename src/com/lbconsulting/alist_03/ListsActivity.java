package com.lbconsulting.alist_03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ListsPagerAdapter;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.EditItemDialogFragment;
import com.lbconsulting.alist_03.dialogs.EditItemDialogFragment.EditItemDialogListener;
import com.lbconsulting.alist_03.dialogs.EditListTitleDialogFragment;
import com.lbconsulting.alist_03.dialogs.EditListTitleDialogFragment.EditListTitleDialogListener;
import com.lbconsulting.alist_03.fragments.ListsFragment;
import com.lbconsulting.alist_03.fragments.ListsFragment.OnListItemLongClickListener;
import com.lbconsulting.alist_03.fragments.MasterListFragment;
import com.lbconsulting.alist_03.fragments.MasterListFragment.OnMasterListItemLongClickListener;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsActivity extends FragmentActivity
		implements OnListItemLongClickListener, EditItemDialogListener, EditListTitleDialogListener,
		OnMasterListItemLongClickListener {

	//private PagerAdapter mFragmentPager;
	private ListsPagerAdapter mListsPagerAdapter;
	private ViewPager mPager;

	private ListsFragment mListsFragment;
	private MasterListFragment mMasterListFragment;
	private Boolean mTwoFragmentLayout = false;
	//private Bundle mActiveListID_Bundle;

	private long NO_ACTIVE_LIST_ID = 0;
	//private long mActiveListID = NO_ACTIVE_LIST_ID;
	private long mActiveListID = 2;
	private long mActiveItemID;
	private int mActiveListPosition = 0;
	//private int mLastViewedActivity = -1;
	private Cursor mAllListsCursor;

	//private ListsTableObserver mListsTableObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lists_pager);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", 0);

		if (mActiveListID < 2) {
			SetToFirstList();
		}

		View frag_masterList_placeholder = this.findViewById(R.id.frag_masterList_placeholder);
		mTwoFragmentLayout = frag_masterList_placeholder != null
				&& frag_masterList_placeholder.getVisibility() == View.VISIBLE;

		MyLog.i("Lists_ACTIVITY", "onCreate - ViewPager");

		mAllListsCursor = ListsTable.getAllLists(this);

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
				SetActiveListID(position);
				MyLog.d("Lists_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = " + mActiveListID);

				if (mTwoFragmentLayout) {
					LoadMasterListFragment();
				}
			}
		});

		if (mTwoFragmentLayout) {
			LoadMasterListFragment();
		}

		/*		MyLog.i("Lists_ACTIVITY", "onCreate");
				setContentView(R.layout.activity_lists);

				// Add ListsFragment
				mListsFragment = (ListsFragment) this.getSupportFragmentManager().findFragmentByTag("ListsFragment");
				if (mListsFragment == null) {
					// create the fragment
					mListsFragment = ListsFragment.newInstance(mActiveListID);

					MyLog.i("Lists_ACTIVITY", "onCreate. New ListsFragment created.");
					// add the fragment to the Activity
					this.getSupportFragmentManager().beginTransaction()
							.add(R.id.frag_lists_placeholder, mListsFragment, "ListsFragment")
							.commit();
					MyLog.i("Lists_ACTIVITY", "onCreate. ListsFragment add.");
				}

				View fragmentMasterListPlaceholder = this.findViewById(R.id.frag_masterList_placeholder);
				mTwoFragmentLayout = fragmentMasterListPlaceholder != null
						&& fragmentMasterListPlaceholder.getVisibility() == View.VISIBLE;

				if (mTwoFragmentLayout) {

					mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager()
							.findFragmentByTag("MasterListFragment");
					if (mMasterListFragment == null) {
						// create MasterListFragment
						mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

						MyLog.i("Lists_ACTIVITY", "onCreate. New MasterListFragment created.");
						// add the fragment to the Activity
						this.getSupportFragmentManager().beginTransaction()
								.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
								.commit();
						MyLog.i("Lists_ACTIVITY", "onCreate. MasterListFragment add.");
					}
				} else {
					// one fragment layout
					mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager()
							.findFragmentByTag("MasterListFragment");
					if (mMasterListFragment != null) {
						// remove the fragment from the Activity
						this.getSupportFragmentManager().beginTransaction()
								.remove(mMasterListFragment)
								.commit();
						MyLog.i("Lists_ACTIVITY", "onCreate. MasterListFragment removed.");
					}
				}*/
	}

	private void SetToFirstList() {
		long firstListID = ListsTable.getFirstListID(this);
		if (firstListID > 1) {
			mActiveListID = firstListID;
		} else {
			// the application has no lists
			// so as that one be created.
			//TODO create new list
		}

	}

	private void StartMasterListActivity() {
		Intent masterListActivityIntent = new Intent(this, MasterListActivity.class);
		masterListActivityIntent.putExtra("ActiveListID", mActiveListID);
		startActivity(masterListActivityIntent);
	}

	private void StartStoresActivity() {
		Intent intent = new Intent(this, StoresActivity.class);
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

	protected void SetActiveListID(int position) {
		if (mAllListsCursor != null) {
			long listID = -1;
			try {
				mAllListsCursor.moveToPosition(position);
				listID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
			} catch (Exception e) {
				MyLog.d("Lists_ACTIVITY", "Exception in getlistID: " + e);
			}
			mActiveListID = listID;
			mActiveListPosition = position;
		}
	}

	protected void LoadMasterListFragment() {
		mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager()
				.findFragmentByTag("MasterListFragment");
		if (mMasterListFragment == null) {
			// create MasterListFragment
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = "
					+ mActiveListID);
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. MasterListFragment ADD. ListID = " + mActiveListID);
		} else {
			// MasterListFragment exists ... so replace it
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = "
					+ mActiveListID);
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
		mPager.setCurrentItem(mActiveListPosition);
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
		mAllListsCursor.close();
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
			/*mListsFragment.UnStrikeAndDeselectAllStruckOutItems();*/
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_addItem:
			StartMasterListActivity();
			return true;

		case R.id.action_newList:
			/*CreatNewList();*/
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_clearList:
			/*mListsFragment.DeselectAllItemsInList();*/
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_emailList:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_editListTitle:
			EditListTitle();
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		case R.id.action_deleteList:
			/*			Cursor listsCursor = ListsTable.getAllLists(this);
						int position = AListUtilities.getPositionById(listsCursor, mActiveListID);
						position++;
						if (listsCursor.moveToPosition(position)) {
							// get the next list
							mActiveListID = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
						} else {
							// get the previous list
							position--;
							position--;
							if (listsCursor.moveToPosition(position)) {
								mActiveListID = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
							} else {
								// No list to move to!
								// TODO Figure out what to show when there is not lists.
							}
						}

						mListsFragment.DeleteList();
						mListsFragment = ListsFragment.newInstance(mActiveListID);
						MyLog.i("Lists_ACTIVITY", "action_deleteList. New ListsFragment created.");
						// add the fragment to the Activity
						this.getSupportFragmentManager().beginTransaction()
								.replace(R.id.frag_lists_placeholder, mListsFragment, "ListsFragment")
								.commit();
						MyLog.i("Lists_ACTIVITY", "action_deleteList. ListsFragment replace.");*/

			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_stores:
			StartStoresActivity();
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		case R.id.action_Preferences:
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			StartListPreferencesActivity();
			return true;

		case R.id.action_about:
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			StartAboutActivity();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	/*	private void CreatNewList() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.newListTitle);
			builder.setIcon(R.drawable.ic_action_edit).show();

			LayoutInflater li = LayoutInflater.from(this);
			final View view = li.inflate(R.layout.dialog_edit_list_title, null);

			builder.setView(view);

			builder.setPositiveButton(getString(R.string.btn_apply_text), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					EditText txtEditListTitle = (EditText) view.findViewById(R.id.txtEditListTitle);
					String listTitle = txtEditListTitle.getText().toString();
					mActiveListID = ListsTable.CreateNewList(getApplicationContext(), listTitle);
					ShowNewList();
				}

			});

			builder.setNegativeButton(getString(R.string.btn_cancel_text), null);

			builder.show();

		}*/

	private void EditListTitle() {
		// Remove any currently showing dialog
		FragmentManager fm = this.getSupportFragmentManager();
		Fragment prev = fm.findFragmentByTag("dialog_edit_list_title");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}
		EditListTitleDialogFragment editListTitleDialog = EditListTitleDialogFragment.newInstance(mActiveListID);
		editListTitleDialog.show(fm, "dialog_edit_list_title");
	}

	/*	private void ShowNewList() {
			mListsFragment = ListsFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "ShowNewList. New ListsFragment created.");
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.frag_lists_placeholder, mListsFragment, "ListsFragment")
					.commit();
			MyLog.i("Lists_ACTIVITY", "ShowNewList. ListsFragment replace.");
		}*/

	@Override
	public void onListItemLongClick(int position, long itemID) {
		mActiveItemID = itemID;
		// Remove any currently showing dialog
		FragmentManager fm = getSupportFragmentManager();
		Fragment prev = fm.findFragmentByTag("dialog_edit_item");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}
		EditItemDialogFragment editItemDialog = EditItemDialogFragment.newInstance(itemID);
		editItemDialog.show(fm, "dialog_edit_item");
	}

	@Override
	public void onApplyEditItemDialog(String itemName, String itemNote, long itemGroupID) {
		ItemsTable.UpdateItem(this, mActiveItemID, itemName, itemNote, itemGroupID);
	}

	@Override
	public void onCancelEditItemDialog() {
		// Do nothing

	}

	@Override
	public void onApplyEditListTitleDialog(String newListTitle) {
		// TODO code to update newListTitle
		/*ContentValues cv = new ContentValues();
		cv.put(ListsTable.COL_LIST_TITLE, newListTitle);
		ListsTable.UpdateListsTableFieldValues(this, mActiveListID, cv);*/
		//mListsFragment.showListTitle(newListTitle);
	}

	@Override
	public void onCancelEditListTitleDialog() {
		// Do nothing

	}

	@Override
	public void onMasterListItemLongClick(int position, long itemID) {
		// TODO Edit Item ... Edit Item Dialog
		mActiveListID = itemID;

	}

}