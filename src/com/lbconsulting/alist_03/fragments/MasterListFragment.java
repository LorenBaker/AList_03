package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.adapters.GroupsSpinnerCursorAdapter;
import com.lbconsulting.alist_03.adapters.MasterListCursorAdaptor;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class MasterListFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	OnMasterListItemLongClickListener mMasterListItemLongClickCallback;

	// Container Activity must implement this interface
	public interface OnMasterListItemLongClickListener {
		public void onMasterListItemLongClick(int position, long itemID);
	}

	//OnListItemSelectedListener mListsCallback;
	//private static final int LISTS_LOADER_ID = 1;
	private static final int ITEMS_LOADER_ID = 2;
	//private static final int STORES_LOADER_ID = 3;
	private static final int GROUPS_LOADER_ID = 4;
	private String[] loaderNames = { "Lists_Loader", "Items_Loader", "Stores_Loader", "Groups_Loader" };

	private long mActiveListID;
	private long mActiveItemID;
	private int mMasterListViewFirstVisiblePosition;
	private int mMasterListViewTop;

	private ListSettings listSettings;
	private boolean flag_FirstTimeLoadingItemDataSinceOnResume = false;

	//private Cursor mList;
	private EditText txtItemName;
	private Button btnAddToMasterList;
	private EditText txtItemNote;
	//private Spinner spnGroupSpinner;
	private ListView lvItemsListView;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mMasterListFragmentCallbacks;
	private MasterListCursorAdaptor mMasterListCursorAdaptor;
	private GroupsSpinnerCursorAdapter mGroupsSpinnerCursorAdapter;

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
		try {
			mMasterListItemLongClickCallback = (OnMasterListItemLongClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMasterListItemLongClickListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onCreateView");
		View view = inflater.inflate(R.layout.frag_master_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("MasterListFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		View frag_masterList_placeholder = getActivity().findViewById(R.id.frag_masterList_placeholder);
		if (frag_masterList_placeholder != null) {
			// there is a place for the masterListFragment
			Bundle bundle = this.getArguments();
			if (bundle != null) {
				mActiveListID = bundle.getLong("listID", 0);
			}

			listSettings = new ListSettings(getActivity(), mActiveListID);

			txtItemName = (EditText) getActivity().findViewById(R.id.txtItemName);
			btnAddToMasterList = (Button) getActivity().findViewById(R.id.btnAddToMasterList);
			txtItemNote = (EditText) getActivity().findViewById(R.id.txtItemNote);

			lvItemsListView = (ListView) getActivity().findViewById(R.id.lvItemsListView);
			mMasterListCursorAdaptor = new MasterListCursorAdaptor(getActivity(), null, 0, listSettings);
			lvItemsListView.setAdapter(mMasterListCursorAdaptor);
			lvItemsListView.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());

			mMasterListFragmentCallbacks = this;

			mLoaderManager = getLoaderManager();
			mLoaderManager.initLoader(ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
			//mLoaderManager.initLoader(GROUPS_LOADER_ID, null, mMasterListFragmentCallbacks);

			lvItemsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mActiveItemID = id;
					ItemsTable.ToggleSelection(getActivity(), id);
					mLoaderManager.restartLoader(ITEMS_LOADER_ID, null, mMasterListFragmentCallbacks);
				}
			});

			lvItemsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					mActiveItemID = id;
					mMasterListItemLongClickCallback.onMasterListItemLongClick(position, id);
					return true;
				}
			});
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

		case ITEMS_LOADER_ID:
			try {
				cursorLoader = ItemsTable.getAllItemsInList(getActivity(), mActiveListID,
						ItemsTable.SORT_ORDER_ITEM_NAME);

			} catch (SQLiteException e) {
				MyLog.e("MasterListFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("MasterListFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}
			break;

		case GROUPS_LOADER_ID:
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
			break;

		default:
			return null;
		}
		return cursorLoader;
	}

	/*	@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
			MyLog.i("MasterListFragment", "onLoadFinished; loader id = " + loader.getId());
			// The asynchronous load is complete and the newCursor is now available for use. 
			// Update the masterListAdapter to show the changed data.
			switch (loader.getId()) {
			case ITEMS_LOADER_ID:
				mMasterListCursorAdaptor.swapCursor(newCursor);

				// TODO add two new fields in ListsTable for Master List View position
				if (flag_FirstTimeLoadingItemDataSinceOnResume) {
					lvItemsListView.setSelectionFromTop(
							listSettings.getListViewFirstVisiblePosition(), listSettings.getListViewTop());
					flag_FirstTimeLoadingItemDataSinceOnResume=false;
				}
				break;

			case GROUPS_LOADER_ID:
				mGroupsSpinnerCursorAdapter.swapCursor(newCursor);
				break;
			default:
				break;
			}
		}*/

	/*	@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			MyLog.i("ListsFragment", "onLoaderReset; loader id = " + loader.getId());
			switch (loader.getId()) {
			case ITEMS_LOADER_ID:
				mMasterListCursorAdaptor.swapCursor(null);
				break;

			case GROUPS_LOADER_ID:
				mGroupsSpinnerCursorAdapter.swapCursor(null);
				break;

			default:
				break;
			}

		}*/

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		int id = loader.getId();
		String loaderName = loaderNames[id - 1];
		MyLog.i("MasterListFragment: onLoadFinished; " + loaderName, "; listID = " + mActiveListID);
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case ITEMS_LOADER_ID:
			mMasterListCursorAdaptor.swapCursor(newCursor);

			// TODO add two new fields in ListsTable for Master List View position
			/*if (flag_FirstTimeLoadingItemDataSinceOnResume) {
				lvItemsListView.setSelectionFromTop(
						listSettings.getListViewFirstVisiblePosition(), listSettings.getListViewTop());
				flag_FirstTimeLoadingItemDataSinceOnResume=false;
			}*/
			break;

		case GROUPS_LOADER_ID:
			mGroupsSpinnerCursorAdapter.swapCursor(newCursor);
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
		case ITEMS_LOADER_ID:
			mMasterListCursorAdaptor.swapCursor(null);
			break;

		case GROUPS_LOADER_ID:
			mGroupsSpinnerCursorAdapter.swapCursor(null);
			break;

		default:
			break;
		}

	}
}
