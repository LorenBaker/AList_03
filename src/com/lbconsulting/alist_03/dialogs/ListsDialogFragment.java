package com.lbconsulting.alist_03.dialogs;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.BridgeTable;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.LocationsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.database.contentprovider.AListContentProvider;
import com.lbconsulting.alist_03.fragments.ListPreferencesFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsDialogFragment extends DialogFragment {

	public static final int LIST_SORT_ORDER = 10;
	public static final int MASTER_LIST_SORT_ORDER = 20;
	public static final int EDIT_LIST_TITLE = 30;
	public static final int NEW_LIST = 40;

	private static final int BLANK_LIST_TEMPLATE = 0;
	private static final int GROCERIES_LIST_TEMPLATE = 1;
	private static final int TO_DO_LIST_TEMPLATE = 2;

	private Button btnApply;
	private Button btnCancel;
	private static EditText txtEditListTitle;
	private static Spinner spinListTemplate;
	private static ProgressBar pbLoadingIndicator;
	private static LinearLayout llButtons;

	ProgressDialog progressDialog;

	private long mActiveListID;
	private ListSettings listSettings;
	private int mDialogType;
	private int mSortOrderResult;
	private String mListTitle;

	public interface SortOrderDialogListener {
		void onApplySortOrderDialog(int sortOrderResult);
	}

	public ListsDialogFragment() {
		// Empty constructor required for DialogFragment
	}

	/**
	 * Create a new instance of SortOrderDialogFragment
	 * 
	 * @param itemID
	 * @return SortOrderDialogFragment
	 */
	public static ListsDialogFragment newInstance(long listID, int dialogType) {
		ListsDialogFragment f = new ListsDialogFragment();
		// Supply itemID input as an argument.
		Bundle args = new Bundle();
		args.putLong("listID", listID);
		args.putInt("dialogType", dialogType);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("SortOrderDialogFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		outState.putLong("dialogType", this.mDialogType);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
			mDialogType = savedInstanceState.getInt("dialogType", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				mActiveListID = bundle.getLong("listID", 0);
				mDialogType = bundle.getInt("dialogType", 0);
			}
		}

		if (mActiveListID > 0) {
			listSettings = new ListSettings(getActivity(), mActiveListID);
		}

		// inflate view
		View view = null;
		switch (mDialogType) {
		case LIST_SORT_ORDER:
			MyLog.i("SortOrderDialogFragment", "onCreateView: List Sort Order");
			view = inflater.inflate(R.layout.dialog_list_sort_order, container);
			getDialog().setTitle(R.string.dialog_title_list_sort_order);
			break;

		case MASTER_LIST_SORT_ORDER:
			MyLog.i("SortOrderDialogFragment", "onCreateView: Master List Sort Order");
			view = inflater.inflate(R.layout.dialog_master_list_sort_order, container);
			getDialog().setTitle(R.string.dialog_title_master_list_sort_order);
			break;

		case EDIT_LIST_TITLE:
			MyLog.i("SortOrderDialogFragment", "onCreateView: Edit List Title");
			view = inflater.inflate(R.layout.dialog_edit_list_title, container);
			getDialog().setTitle(R.string.dialog_edit_list_title);
			break;

		case NEW_LIST:
			MyLog.i("SortOrderDialogFragment", "onCreateView: New List");
			view = inflater.inflate(R.layout.dialog_new_list, container);
			getDialog().setTitle(R.string.dialog_title_create_new_list);
			break;

		default:
			break;
		}

		if (view != null) {
			btnApply = (Button) view.findViewById(R.id.btnApply);
			btnApply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ContentValues newFieldValues = new ContentValues();
					String key = String.valueOf(mActiveListID)
							+ ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
					Intent intent = new Intent(key);
					intent.putExtra("listID", mActiveListID);
					switch (mDialogType) {
					case LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						getDialog().dismiss();
						break;

					case MASTER_LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_MASTER_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newMasterListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						getDialog().dismiss();
						break;

					case EDIT_LIST_TITLE:
						String newListTitle = txtEditListTitle.getText().toString();
						newListTitle = newListTitle.trim();
						newFieldValues.put(ListsTable.COL_LIST_TITLE, newListTitle);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("editedListTitle", newListTitle);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						getDialog().dismiss();
						break;

					case NEW_LIST:
						long newListID = -1;
						newListTitle = txtEditListTitle.getText().toString();
						newListTitle = newListTitle.trim();
						if (newListTitle != "") {
							newListID = ListsTable.CreateNewList(getActivity(), newListTitle);
							if (newListID > 0) {
								switch (spinListTemplate.getSelectedItemPosition()) {
								case BLANK_LIST_TEMPLATE:
									intent.putExtra("newListID", newListID);
									LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
									getDialog().dismiss();
									break;

								case GROCERIES_LIST_TEMPLATE:
									new CreateGroceriesList().execute(newListID);
									break;

								case TO_DO_LIST_TEMPLATE:
									FillToDoList(newListID);
									intent.putExtra("newListID", newListID);
									LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
									getDialog().dismiss();
									break;

								default:
									break;
								}
							}
						}
						break;

					default:
						break;
					}
				}
			});

			btnCancel = (Button) view.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getDialog().dismiss();
				}
			});

			txtEditListTitle = (EditText) view.findViewById(R.id.txtEditListTitle);
			if (txtEditListTitle != null) {
				switch (mDialogType) {
				case EDIT_LIST_TITLE:
					// We're displaying the Edit List Title dialog
					mListTitle = listSettings.getListTitle();
					txtEditListTitle.setText(mListTitle);
					break;

				case NEW_LIST:
					// We're creating a new List
					pbLoadingIndicator = (ProgressBar) view.findViewById(R.id.pbLoadingIndicator);
					// rlCircularLoadingProgressIndicator = (RelativeLayout)
					// view.findViewById(R.id.rlCircularLoadingProgressIndicator);
					llButtons = (LinearLayout) view.findViewById(R.id.llButtons);
					spinListTemplate = (Spinner) view.findViewById(R.id.spinListTemplate);
					if (spinListTemplate != null) {
						fillSpinListTemplate();
						spinListTemplate.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
								switch (position) {
								case BLANK_LIST_TEMPLATE:
									txtEditListTitle.setText("");
									break;
								case GROCERIES_LIST_TEMPLATE:
									txtEditListTitle.setText(
											getActivity().getString(R.string.dialog_lists_groceries_list_text));
									break;

								case TO_DO_LIST_TEMPLATE:
									txtEditListTitle.setText(
											getActivity().getString(R.string.dialog_lists_to_do_list_text));
									break;

								default:
									break;
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// do nothing
							}
						});
					}
					break;

				default:
					break;

				}

				if (txtEditListTitle.requestFocus()) {
					getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
			}
		}

		return view;
	}

	public static void ShowLoadingIndicator() {

		if (pbLoadingIndicator != null) {
			pbLoadingIndicator.setVisibility(View.VISIBLE);
			spinListTemplate.setVisibility(View.GONE);
			txtEditListTitle.setVisibility(View.GONE);
			llButtons.setVisibility(View.GONE);
		}
	}

	protected void FillToDoList(long todosListID) {
		AListContentProvider.SuppressChangeNotification(true);

		ArrayList<Long> todoGroupIDs = new ArrayList<Long>();
		// create to do groups
		String[] todoGroups = this.getResources().getStringArray(R.array.todo_groups);
		for (int i = 0; i < todoGroups.length; i++) {
			todoGroupIDs.add(GroupsTable.CreateNewGroup(getActivity(), todosListID, todoGroups[i]));
		}

		// create to do items
		String[] todoItems = getActivity().getResources().getStringArray(R.array.todo_items);
		for (int i = 0; i < todoItems.length; i++) {
			ItemsTable.CreateNewItem(getActivity(), todosListID, todoItems[i], todoGroupIDs.get(i));
		}

		mActiveListID = todosListID;
		AListContentProvider.SuppressChangeNotification(false);
	}

	private void fillSpinListTemplate() {
		ArrayList<String> spinListItems = new ArrayList<String>();
		spinListItems.add(getActivity().getString(R.string.dialog_lists_blank_list_text)); // 0
		spinListItems.add(getActivity().getString(R.string.dialog_lists_groceries_list_text)); // 1
		spinListItems.add(getActivity().getString(R.string.dialog_lists_to_do_list_text)); // 2
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinListItems);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinListTemplate.setAdapter(dataAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}
	}

	/*	public void onRadioButtonClicked(View view) {
			MyLog.i("SortOrderDialogFragment", "onRadioButtonClicked; view id = " + view.getId());
			switch (view.getId()) {
			case R.id.rbAlphabetical_list:
				mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
				break;

			case R.id.rbManual:
				mSortOrderResult = ListPreferencesFragment.MANUAL;
				break;
			case R.id.rbAlphabetical_master_list:
				mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
				break;
			case R.id.rbSelectedItemsAtTop:
				mSortOrderResult = ListPreferencesFragment.SELECTED_AT_TOP;
				break;
			case R.id.rbSelectedItemsAtBottom:
				mSortOrderResult = ListPreferencesFragment.SELECTED_AT_BOTTOM;
				break;
			case R.id.rbLastUsed:
				mSortOrderResult = ListPreferencesFragment.LAST_USED;
				break;
			default:
				break;
			}
			String toastMsg = "Selection = " + mSortOrderResult;
			Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
		}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onCreateDialog");
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		MyLog.i("SortOrderDialogFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		MyLog.i("SortOrderDialogFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		MyLog.i("SortOrderDialogFragment", "onDismiss");
		super.onDismiss(dialog);
	}

	@Override
	public void onStart() {
		MyLog.i("SortOrderDialogFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		MyLog.i("SortOrderDialogFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		MyLog.i("SortOrderDialogFragment", "onDestroy");
		super.onDestroy();
	}

	private class CreateGroceriesList extends AsyncTask<Long, Void, Long> {

		@Override
		protected void onPreExecute() {
			getDialog().setTitle(R.string.dialog_lists_loading_groceries_list_text);
			ShowLoadingIndicator();
		}

		@Override
		protected Long doInBackground(Long... newListID) {
			FillGroceriesList(newListID[0]);
			return (newListID[0]);
		}

		@Override
		protected void onPostExecute(Long newListID) {
			String key = String.valueOf(mActiveListID)
					+ ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
			Intent intent = new Intent(key);
			intent.putExtra("newListID", newListID);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
			getDialog().dismiss();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	private void FillGroceriesList(long groceriesListID) {

		AListContentProvider.SuppressChangeNotification(true);

		Hashtable<String, Long> groceryGroupsHashTable = new Hashtable<String, Long>();

		groceryGroupsHashTable.put("[No Group]", (long) 1); // enter the default group
		// create grocery groups
		String[] groceryGroups = this.getResources().getStringArray(R.array.grocery_groups);
		for (int i = 0; i < groceryGroups.length; i++) {
			long groupID = GroupsTable.CreateNewGroup(getActivity(), groceriesListID, groceryGroups[i]);
			groceryGroupsHashTable.put(groceryGroups[i], groupID);
		}

		// create grocery items
		// NOTE: this only works if R.array.grocery_items and
		// R.array.grocery_items_groups
		// are in the proper order!!!!!!
		String[] groceryItems = this.getResources().getStringArray(R.array.grocery_items);
		String[] groceryItemGroups = this.getResources().getStringArray(R.array.grocery_items_groups);

		for (int i = 0; i < groceryItems.length; i++) {
			long groupID = groceryGroupsHashTable.get(groceryItemGroups[i]);
			ItemsTable.CreateNewItem(getActivity(), groceriesListID, groceryItems[i], groupID);
		}

		// create grocery stores
		Hashtable<String, Long> storesHashTable = new Hashtable<String, Long>();
		String[] groceryStores = this.getResources().getStringArray(R.array.grocery_stores);
		for (int i = 0; i < groceryStores.length; i++) {
			long storeID = StoresTable.CreateNewStore(getActivity(), groceriesListID, groceryStores[i]);
			storesHashTable.put(groceryStores[i], storeID);
		}

		// create locations
		Hashtable<String, Long> locationsHashTable = new Hashtable<String, Long>();
		locationsHashTable.put("[No LOCATION]", (long) 1); // enter the default location
		String[] storeLocations = this.getResources().getStringArray(R.array.locations);
		for (int i = 0; i < storeLocations.length; i++) {
			long locationID = LocationsTable.CreateNewLocation(getActivity(), storeLocations[i]);
			locationsHashTable.put(storeLocations[i], locationID);
		}

		// create Bridge table
		long locationID = -1;
		long groupID = -1;

		/*		String[] Albertons = this.getResources().getStringArray(R.array.Albertsons_Eastgate_Locations);
				long storeID = storesHashTable.get(groceryStores[0]);
				for (int i = 0; i < Albertons.length; i++) {
					String groupLocation = Albertons[i];
					if (groupLocation.equals("[No LOCATION]")) {
						locationID = 1;
					} else {
						locationID = locationsHashTable.get(Albertons[i]);
					}
					if (i == 0) {
						groupID = 1;
					} else {
						groupID = groceryGroupsHashTable.get(groceryGroups[i - 1]);
					}

					BridgeTable.CreateNewBridgeRow(getActivity(), groceriesListID, storeID, groupID, locationID);
				}*/

		/*		String[] QFC = this.getResources().getStringArray(R.array.QFC_Factoria_Locations);
				storeID = storesHashTable.get(groceryStores[1]);
				for (int i = 0; i < QFC.length; i++) {
					String groupLocation = QFC[i];
					if (groupLocation.equals("[No LOCATION]")) {
						locationID = 1;
					} else {
						locationID = locationsHashTable.get(QFC[i]);
					}
					if (i == 0) {
						groupID = 1;
					} else {
						groupID = groceryGroupsHashTable.get(groceryGroups[i - 1]);
					}
					BridgeTable.CreateNewBridgeRow(getActivity(), groceriesListID, storeID, groupID, locationID);
				}*/

		/*		String[] sw_belfair = this.getResources().getStringArray(R.array.Safeway_Belfair_Locations);
				storeID = storesHashTable.get(groceryStores[2]);
				for (int i = 0; i < sw_belfair.length; i++) {
					String groupLocation = sw_belfair[i];
					if (groupLocation.equals("[No LOCATION]")) {
						locationID = 1;
					} else {
						locationID = locationsHashTable.get(sw_belfair[i]);
					}
					if (i == 0) {
						groupID = 1;
					} else {
						groupID = groceryGroupsHashTable.get(groceryGroups[i - 1]);
					}
					BridgeTable.CreateNewBridgeRow(getActivity(), groceriesListID, storeID, groupID, locationID);
				}*/

		/*		String[] sw_evergreen = this.getResources().getStringArray(R.array.Safeway_Evergreen_Village_Locations);
				storeID = storesHashTable.get(groceryStores[3]);
				for (int i = 0; i < sw_evergreen.length; i++) {
					String groupLocation = sw_evergreen[i];
					if (groupLocation.equals("[No LOCATION]")) {
						locationID = 1;
					} else {
						locationID = locationsHashTable.get(sw_evergreen[i]);
					}
					if (i == 0) {
						groupID = 1;
					} else {
						groupID = groceryGroupsHashTable.get(groceryGroups[i - 1]);
					}
					BridgeTable.CreateNewBridgeRow(getActivity(), groceriesListID, storeID, groupID, locationID);
				}*/

		String[] sw_Issaquah = this.getResources().getStringArray(R.array.Safeway_Issaquah_Locations);
		long storeID = storesHashTable.get(groceryStores[0]);
		for (int i = 0; i < sw_Issaquah.length; i++) {
			locationID = locationsHashTable.get(sw_Issaquah[i]);
			groupID = groceryGroupsHashTable.get(groceryGroups[i]);

			if (locationID == 1) {
				continue;
			}

			BridgeTable.ReviseBridgeRow(getActivity(), groceriesListID, storeID, groupID, locationID);
		}

		AListContentProvider.SuppressChangeNotification(false);
	}

}
