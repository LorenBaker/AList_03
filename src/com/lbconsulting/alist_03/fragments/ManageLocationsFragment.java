package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.adapters.LocationsSpinnerCursorAdapter;
import com.lbconsulting.alist_03.adapters.ManageLocationsCursorAdaptor;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.LocationsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ManageLocationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private long mActiveListID = -1;
	private long mActiveStoreID = -1;
	private Cursor mActiveStoreCursor;

	private static final int GROUPS_LOADER_ID = 4;
	private static final int LOCATIONS_LOADER_ID = 5;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mManageLocationsFragmentCallbacks;
	private ManageLocationsCursorAdaptor mManageLocationsCursorAdaptor;
	private LocationsSpinnerCursorAdapter mLocationsSpinnerCursorAdapter;

	//private boolean flag_FirstTimeLoadingGroupDataSinceOnResume = false;

	public static final String RESART_GROUPS_LOADER_KEY = "RestartGroupsLoaderKey";
	private BroadcastReceiver mRestartGroupsLoaderReceiver;

	private TextView tvStoreName;
	private Spinner spinLocations;
	private Button btnApplyLocation;
	private ListView groupsListView;

	public ManageLocationsFragment() {
		// Empty constructor
	}

	public static ManageLocationsFragment newInstance(long listID, long newStoreID) {
		if (listID < 2) {
			MyLog.e("ManageLocationsFragment: newInstance; listID = " + listID, " is less than 2!!!!");
			return null;

		} else {
			ManageLocationsFragment f = new ManageLocationsFragment();
			// Supply listID and newStoreID input as arguments.
			Bundle args = new Bundle();
			args.putLong("listID", listID);
			args.putLong("storeID", newStoreID);
			f.setArguments(args);
			return f;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("ManageLocationsFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("ManageLocationsFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store our listID and storeID
		outState.putLong("listID", this.mActiveListID);
		outState.putLong("storeID", this.mActiveStoreID);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("ManageLocationsFragment", "onCreateView. ");

		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
			mActiveStoreID = savedInstanceState.getLong("storeID", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				mActiveListID = bundle.getLong("listID", 0);
				mActiveStoreID = bundle.getLong("storeID", 0);
			}
		}

		View view = inflater.inflate(R.layout.frag_manage_locations, container, false);
		if (view != null) {

			// set the store's name in to the fragment
			mActiveStoreCursor = StoresTable.getStore(getActivity(), mActiveStoreID);
			if (mActiveStoreCursor != null && mActiveStoreCursor.getCount() > 0) {
				mActiveStoreCursor.moveToFirst();
				tvStoreName = (TextView) view.findViewById(R.id.tvStoreName);
				if (tvStoreName != null) {
					String storeName = mActiveStoreCursor.getString(mActiveStoreCursor
							.getColumnIndexOrThrow(StoresTable.COL_STORE_NAME));
					if (storeName == null) {
						storeName = "";
					}
					tvStoreName.setText(storeName);
				}
			}

			// populate spinLocations 
			mLocationsSpinnerCursorAdapter = new LocationsSpinnerCursorAdapter(getActivity(), null, 0);
			spinLocations = (Spinner) view.findViewById(R.id.spinLocations);
			spinLocations.setAdapter(mLocationsSpinnerCursorAdapter);

			// set btnApplyLocation onClickListener
			btnApplyLocation = (Button) view.findViewById(R.id.btnApplyLocation);
			btnApplyLocation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ApplyLocationToCheckedGroups();
				}
			});

			// populate groupsListView
			mManageLocationsCursorAdaptor = new ManageLocationsCursorAdaptor(getActivity(), null, 0);
			groupsListView = (ListView) view.findViewById(R.id.groupsListView);
			if (groupsListView != null) {
				groupsListView.setAdapter(mManageLocationsCursorAdaptor);
			}
		}

		groupsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View onItemClickView, int position, long groupID) {
				GroupsTable.ToggleCheckBox(getActivity(), groupID);
				//mLoaderManager.restartLoader(GROUPS_LOADER_ID, null, mManageLocationsFragmentCallbacks);
			}
		});

		groupsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			// edit item dialog
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View onItemLongClickView, int position,
					long groupID) {

				Toast.makeText(getActivity(), "Edit Group under construction.", Toast.LENGTH_SHORT).show();
				/*FragmentManager fm = getFragmentManager();
				Fragment prev = fm.findFragmentByTag("dialog_edit_group");
				if (prev != null) {
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(prev);
					ft.commit();
				}
				EditGroupDialogFragment editItemDialog = EditGroupDialogFragment.newInstance(activeItemID);
				editItemDialog.show(fm, "dialog_edit_group");*/

				return true;
			}
		});

		mManageLocationsFragmentCallbacks = this;

		return view;
	}

	protected void ApplyLocationToCheckedGroups() {
		long locationID = spinLocations.getSelectedItemId();
		GroupsTable.ApplyLocationToCheckedGroups(getActivity(),
				mActiveListID, mActiveStoreID, locationID);
		mLoaderManager.restartLoader(GROUPS_LOADER_ID, null, mManageLocationsFragmentCallbacks);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("ManageLocationsFragment", "onActivityCreated");
		getActivity().getActionBar().setTitle("Store Group Locations");

		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(GROUPS_LOADER_ID, null, mManageLocationsFragmentCallbacks);
		mLoaderManager.initLoader(LOCATIONS_LOADER_ID, null, mManageLocationsFragmentCallbacks);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("ManageLocationsFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("ManageLocationsFragment", "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		MyLog.i("ManageLocationsFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("ManageLocationsFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("ManageLocationsFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("ManageLocationsFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("ManageLocationsFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("ManageLocationsFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("ManageLocationsFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("ManageLocationsFragment: onCreateLoader; id = " + id,
				"; listID = " + mActiveListID + " storeID = " + mActiveStoreID);
		CursorLoader cursorLoader = null;

		switch (id) {

		case GROUPS_LOADER_ID:
			try {
				cursorLoader = GroupsTable.getAllGroupsInList(getActivity(), mActiveListID,
						GroupsTable.SORT_ORDER_GROUP);
			} catch (SQLiteException e) {
				MyLog.e("ManageLocationsFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("ManageLocationsFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}

			break;

		case LOCATIONS_LOADER_ID:
			try {
				cursorLoader = LocationsTable.getAllLocationssInListIncludeDefault(getActivity(),
						LocationsTable.SORT_ORDER_LOCATION);
			} catch (SQLiteException e) {
				MyLog.e("ManageLocationsFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("ManageLocationsFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}
			break;

		default:
			break;

		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		int id = loader.getId();
		MyLog.i("ManageLocationsFragment: onLoadFinished; id = " + id,
				"; listID = " + mActiveListID + " storeID = " + mActiveStoreID);
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the adapter to show the changed data.
		switch (loader.getId()) {

		case GROUPS_LOADER_ID:
			mManageLocationsCursorAdaptor.swapCursor(newCursor);
			break;

		case LOCATIONS_LOADER_ID:
			mLocationsSpinnerCursorAdapter.swapCursor(newCursor);
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
		MyLog.i("ManageLocationsFragment: onLoaderReset; id = " + id,
				"; listID = " + mActiveListID + " storeID = " + mActiveStoreID);

		switch (loader.getId()) {

		case GROUPS_LOADER_ID:
			mManageLocationsCursorAdaptor.swapCursor(null);
			break;

		case LOCATIONS_LOADER_ID:
			mLocationsSpinnerCursorAdapter.swapCursor(null);
			break;
		default:
			break;
		}
	}

}
