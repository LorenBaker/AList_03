package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.adapters.MasterListCursorAdaptor;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.EditItemDialogFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class MasterListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	//OnMasterListItemLongClickListener mMasterListItemLongClickCallback;

	// Container Activity must implement this interface
	public interface OnMasterListItemLongClickListener {
		public void onMasterListItemLongClick(int position, long itemID);
	}

	private String[] loaderNames = { "Lists_Loader", "Items_Loader", "Stores_Loader", "Groups_Loader" };

	private long mActiveListID;
	private long mActiveItemID;
	private BroadcastReceiver mItemChangedReceiver;

	private ListSettings listSettings;
	private boolean flag_FirstTimeLoadingItemDataSinceOnResume = false;

	private EditText txtItemName;
	private EditText txtItemNote;
	private Button btnAddToMasterList;
	private Button btnClearEditText;
	private ListView lvItemsListView;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mMasterListFragmentCallbacks;
	private MasterListCursorAdaptor mMasterListCursorAdaptor;

	public MasterListFragment() {
		// Empty constructor
	}

	/**
	 * Create a new instance of EditItemDialogFragment
	 * 
	 * @param itemID
	 * @return EditItemDialogFragment
	 */
	public static MasterListFragment newInstance(long listID) {
		MasterListFragment f = new MasterListFragment();
		// Supply itemID input as an argument.
		Bundle args = new Bundle();
		args.putLong("listID", listID);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("MasterListFragment", "onAttach");
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		/*		try {
					//mMasterListItemLongClickCallback = (OnMasterListItemLongClickListener) activity;
				} catch (ClassCastException e) {
					throw new ClassCastException(activity.toString()
							+ " must implement OnMasterListItemLongClickListener");
				}*/
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		MyLog.i("MasterListFragment", "onSaveInstanceState");
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onCreateView");

		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null)
				mActiveListID = bundle.getLong("listID", 0);
		}

		View view = inflater.inflate(R.layout.frag_master_list, container, false);

		listSettings = new ListSettings(getActivity(), mActiveListID);

		txtItemName = (EditText) view.findViewById(R.id.txtItemName);
		btnAddToMasterList = (Button) view.findViewById(R.id.btnAddToMasterList);

		btnAddToMasterList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectItemForList();
				// hide the soft input keyboard
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
						Service.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtItemName.getWindowToken(), 0);
			}
		});

		btnClearEditText = (Button) view.findViewById(R.id.btnClearEditText);
		btnClearEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ClearEditText();
			}
		});

		txtItemNote = (EditText) view.findViewById(R.id.txtItemNote);

		lvItemsListView = (ListView) view.findViewById(R.id.lvItemsListView);
		mMasterListCursorAdaptor = new MasterListCursorAdaptor(getActivity(), null, 0, listSettings);
		lvItemsListView.setAdapter(mMasterListCursorAdaptor);
		lvItemsListView.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());

		mMasterListFragmentCallbacks = this;

		lvItemsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mActiveItemID = id;
				ItemsTable.ToggleSelection(getActivity(), id);
				txtItemName.setText("");
				txtItemNote.setText("");
				mLoaderManager.restartLoader(AListUtilities.ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
			}
		});

		lvItemsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View listView, int position, long itemID) {
				mActiveItemID = itemID;
				FragmentManager fm = getFragmentManager();
				Fragment prev = fm.findFragmentByTag("dialog_edit_item");
				if (prev != null) {
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(prev);
					ft.commit();
				}
				EditItemDialogFragment editItemDialog = EditItemDialogFragment.newInstance(mActiveListID, itemID);
				editItemDialog.show(fm, "dialog_edit_item");

				return true;
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		// setup txtListItem Listeners
		txtItemName.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				boolean result = false;
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.FLAG_EDITOR_ACTION || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {

					SelectItemForList();
					//getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					result = true;
				}
				return result;
			}
		});

		txtItemName.addTextChangedListener(new TextWatcher() {
			// filter master list as the user inputs text
			@Override
			public void afterTextChanged(Editable s) {

				MyLog.i("MasterListFragment", "onActivityCreated; txtItemName.afterTextChanged -- "
						+ txtItemName.getText().toString());
				mLoaderManager.restartLoader(AListUtilities.ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				MyLog.i("MasterListFragment", "onActivityCreated; txtItemName.beforeTextChanged -- "
						+ txtItemName.getText().toString());
				// Do nothing

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				MyLog.i("MasterListFragment", "onActivityCreated; txtItemName.onTextChanged -- "
						+ txtItemName.getText().toString());
				// Do nothing

			}

		});

		mItemChangedReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mLoaderManager.restartLoader(AListUtilities.ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
			}
		};

		String itemChangedReceiverKey = String.valueOf(mActiveListID) + ItemsTable.ITEM_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mItemChangedReceiver,
				new IntentFilter(itemChangedReceiverKey));

		MyLog.i("MasterListFragment", "onActivityCreated");
		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(AListUtilities.ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
		super.onActivityCreated(savedInstanceState);

	}

	private void SelectItemForList() {
		String newItemName = txtItemName.getText().toString().trim();
		if (!newItemName.isEmpty()) {
			long newItemNameID = ItemsTable.CreateNewItem(getActivity(), mActiveListID, newItemName);
			ItemsTable.SelectItem(getActivity(), newItemNameID, true);

			String newItemNote = txtItemNote.getText().toString().trim();
			//if (!newItemNote.isEmpty()) {
			ContentValues newFieldValues = new ContentValues();
			newFieldValues.put(ItemsTable.COL_ITEM_NOTE, newItemNote);
			ItemsTable.UpdateItemFieldValues(getActivity(), newItemNameID, newFieldValues);
			//}
		}
		txtItemNote.setText("");
		txtItemName.setText("");

		txtItemName.post(new Runnable()
		{
			public void run()
			{
				txtItemName.requestFocus();
			}
		});
	}

	private void ClearEditText() {
		String itemNote = txtItemNote.getText().toString();
		if (itemNote != null && !itemNote.isEmpty()) {
			// the item has a note ... so clear it
			txtItemNote.setText("");
		} else {
			// the item has no note ... so clear the item
			txtItemName.setText("");
		}
	}

	@Override
	public void onStart() {
		MyLog.i("MasterListFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("MasterListFragment", "onResume");
		super.onResume();
		flag_FirstTimeLoadingItemDataSinceOnResume = true;
	}

	@Override
	public void onPause() {
		MyLog.i("MasterListFragment", "onPause");

		// save ItemsListView position
		View v = lvItemsListView.getChildAt(0);
		int ListViewTop = (v == null) ? 0 : v.getTop();
		ContentValues newFieldValues = new ContentValues();
		newFieldValues.put(ListsTable.COL_MASTER_LISTVIEW_FIRST_VISIBLE_POSITION,
				lvItemsListView.getFirstVisiblePosition());
		newFieldValues.put(ListsTable.COL_MASTER_LISTVIEW_TOP, ListViewTop);
		ListsTable.UpdateListsTableFieldValues(getActivity(), mActiveListID, newFieldValues);
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("MasterListFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("MasterListFragment", "onDestroyView");
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mItemChangedReceiver);
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("MasterListFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("MasterListFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("MasterListFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String loaderName = loaderNames[id - 1];
		MyLog.i("MasterListFragment: onCreateLoader; " + loaderName, "; listID = " + mActiveListID);

		CursorLoader cursorLoader = null;
		switch (id) {

		case AListUtilities.ITEMS_LOADER_ID:

			int masterListSortOrder = listSettings.getMasterListSortOrder();
			String sortOrder = "";
			switch (masterListSortOrder) {
			case ListPreferencesFragment.ALPHABETICAL:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			/*			case ListPreferencesFragment.BY_GROUP:
							sortOrder = ItemsTable.SORT_ORDER_BY_GROUP;
							break;*/

			case ListPreferencesFragment.SELECTED_AT_TOP:
				sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_TOP;
				break;

			case ListPreferencesFragment.SELECTED_AT_BOTTOM:
				sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_BOTTOM;
				break;

			case ListPreferencesFragment.LAST_USED:
				sortOrder = ItemsTable.SORT_ORDER_LAST_USED;
				break;

			default:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;
			}

			// filter the cursor based on user typed text in txtListItem and the activeListTypeID
			String itemNameText = txtItemName.getText().toString().trim();
			String selection = null;
			if (!itemNameText.isEmpty()) {
				selection = ItemsTable.COL_ITEM_NAME + " Like '%" + itemNameText + "%'";
			}
			try {
				if (listSettings.getShowGroupsInMasterListFragment()) {
					cursorLoader = ItemsTable.getAllItemsInListWithGroups(getActivity(), mActiveListID, selection);

				} else {
					cursorLoader = ItemsTable.getAllItemsInList(getActivity(), mActiveListID, selection, sortOrder);
				}

			} catch (SQLiteException e) {
				MyLog.e("MasterListFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("MasterListFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}
			break;

		/*		case GROUPS_LOADER_ID:
					try {
						cursorLoader = GroupsTable.getAllGroupsInList(getActivity(), mActiveListID,
								GroupsTable.SORT_ORDER_GROUP);

					} catch (SQLiteException e) {
						MyLog.e("MasterListFragment: onCreateLoader SQLiteException: ", e.toString());
						return null;

					} catch (IllegalArgumentException e) {
						MyLog.e("MasterListFragment: onCreateLoader IllegalArgumentException: ", e.toString());
						return null;
					}
					break;*/

		default:
			return null;
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		int id = loader.getId();
		String loaderName = loaderNames[id - 1];
		MyLog.i("MasterListFragment: onLoadFinished; " + loaderName, "; listID = " + mActiveListID + "; text = "
				+ txtItemName.getText().toString());
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case AListUtilities.ITEMS_LOADER_ID:
			mMasterListCursorAdaptor.swapCursor(newCursor);
			if (flag_FirstTimeLoadingItemDataSinceOnResume) {
				lvItemsListView.setSelectionFromTop(
						listSettings.getMasterListViewFirstVisiblePosition(), listSettings.getMasterListViewTop());
				flag_FirstTimeLoadingItemDataSinceOnResume = false;
			}
			if (newCursor != null && newCursor.getCount() == 1) {
				newCursor.moveToFirst();
				String itemName = newCursor.getString(newCursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NAME));
				if (itemName.equals(txtItemName.getText().toString())) {
					String itemNote = newCursor.getString(newCursor.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NOTE));
					if (itemNote != null && !itemNote.isEmpty()) {
						txtItemNote.setText(itemNote);
					}
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
		String loaderName = loaderNames[id - 1];
		MyLog.i("MasterListFragment: onLoaderReset; " + loaderName, "; listID = " + mActiveListID);
		switch (loader.getId()) {
		case AListUtilities.ITEMS_LOADER_ID:
			mMasterListCursorAdaptor.swapCursor(null);
			break;

		default:
			break;
		}

	}
}
