package com.lbconsulting.alist_03.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class SortOrderDialogFragment extends DialogFragment {

	public static final int LIST_SORT_ORDER = 10;
	public static final int MASTER_LIST_SORT_ORDER = 20;

	//private SortOrderDialogListener mFinishSortOrderDialogCallback;

	private Button btnApply;
	private Button btnCancel;

	private RadioGroup radioGroup_list_sort_order;
	private RadioGroup radioGroup_master_list_sort_order;

	/*private RadioButton rbAlphabetical_list;
	private RadioButton rbByGroup_list;
	private RadioButton rbManual;
	
	private RadioButton rbAlphabetical_master_list;
	private RadioButton rbByGroup_master_list;
	private RadioButton rbSelectedItemsAtTop;
	private RadioButton rbSelectedItemsAtBottom;
	private RadioButton rbLastUsed;*/

	private long mActiveListID;
	private ListSettings listSettings;
	private int mDialogType;
	private int mSortOrderResult;

	public interface SortOrderDialogListener {
		void onApplySortOrderDialog(int sortOrderResult);
	}

	public SortOrderDialogFragment() {
		// Empty constructor required for DialogFragment
	}

	/**
	 * Create a new instance of SortOrderDialogFragment
	 * 
	 * @param itemID
	 * @return SortOrderDialogFragment
	 */
	public static SortOrderDialogFragment newInstance(long listID, int dialogType) {
		SortOrderDialogFragment f = new SortOrderDialogFragment();
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

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception

		/*		try {
					mFinishSortOrderDialogCallback = (SortOrderDialogListener) activity;
				} catch (ClassCastException e) {
					throw new ClassCastException(activity.toString()
							+ " must implement SortOrderDialogListener");
				}*/
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		outState.putLong("dialogType", this.mDialogType);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
			mDialogType = savedInstanceState.getInt("dialogType", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null)
				mActiveListID = bundle.getLong("listID", 0);
			mDialogType = bundle.getInt("dialogType", 0);
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

		default:
			break;
		}

		if (view != null) {
			btnApply = (Button) view.findViewById(R.id.btnApply);
			btnApply.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ContentValues newFieldValues = new ContentValues();
					Intent intent = new Intent("list_preferences_changed");
					intent.putExtra("listID", mActiveListID);
					switch (mDialogType) {
					case LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					case MASTER_LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_MASTER_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newMasterListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					default:
						break;
					}

					getDialog().dismiss();
				}
			});

			btnCancel = (Button) view.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getDialog().dismiss();
				}
			});

			radioGroup_list_sort_order = (RadioGroup) view.findViewById(R.id.radioGroup_list_sort_order);
			if (radioGroup_list_sort_order != null) {
				radioGroup_list_sort_order.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbAlphabetical_list:
							mSortOrderResult = 0;
							break;
						case R.id.rbByGroup_list:
							mSortOrderResult = 1;
							break;
						case R.id.rbManual:
							mSortOrderResult = 2;
							break;
						default:
							mSortOrderResult = 0;
							break;
						}
					}
				});
			}
			radioGroup_master_list_sort_order = (RadioGroup) view
					.findViewById(R.id.radioGroup_master_list_sort_order);
			if (radioGroup_master_list_sort_order != null) {
				radioGroup_master_list_sort_order.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbAlphabetical_master_list:
							mSortOrderResult = 0;
							break;
						case R.id.rbByGroup_master_list:
							mSortOrderResult = 1;
							break;
						case R.id.rbSelectedItemsAtTop:
							mSortOrderResult = 2;
							break;
						case R.id.rbSelectedItemsAtBottom:
							mSortOrderResult = 3;
							break;
						case R.id.rbLastUsed:
							mSortOrderResult = 4;
							break;
						default:
							mSortOrderResult = 0;
							break;
						}
					}
				});
			}
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}
		if (mActiveListID > 0) {
			listSettings = new ListSettings(getActivity(), mActiveListID);
		}

	}

	public void onRadioButtonClicked(View view) {
		MyLog.i("SortOrderDialogFragment", "onRadioButtonClicked; view id = " + view.getId());
		switch (view.getId()) {
		case R.id.rbAlphabetical_list:
			mSortOrderResult = 0;
			break;
		case R.id.rbByGroup_list:
			mSortOrderResult = 1;
			break;
		case R.id.rbManual:
			mSortOrderResult = 2;
			break;
		case R.id.rbAlphabetical_master_list:
			mSortOrderResult = 0;
			break;
		case R.id.rbByGroup_master_list:
			mSortOrderResult = 1;
			break;
		case R.id.rbSelectedItemsAtTop:
			mSortOrderResult = 2;
			break;
		case R.id.rbSelectedItemsAtBottom:
			mSortOrderResult = 3;
			break;
		case R.id.rbLastUsed:
			mSortOrderResult = 4;
			break;
		default:
			break;
		}
		String toastMsg = "Selection = " + mSortOrderResult;
		Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
	}

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

}