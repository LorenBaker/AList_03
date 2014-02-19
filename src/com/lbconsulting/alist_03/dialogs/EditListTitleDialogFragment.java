package com.lbconsulting.alist_03.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class EditListTitleDialogFragment extends DialogFragment {

	private EditListTitleDialogListener mFinishEditListTitleDialogCallback;

	private EditText txtEditListTitle;
	private Button btnApply;
	private Button btnCancel;
	private long mListID;
	private ListSettings listSettings;

	public interface EditListTitleDialogListener {
		void onApplyEditListTitleDialog(String newListTitle);

		void onCancelEditListTitleDialog();
	}

	public EditListTitleDialogFragment() {
		// Empty constructor required for DialogFragment
	}

	/**
	 * Create a new instance of EditListTitleDialogFragment
	 * 
	 * @param itemID
	 * @return EditListTitleDialogFragment
	 */
	public static EditListTitleDialogFragment newInstance(long listID) {
		EditListTitleDialogFragment f = new EditListTitleDialogFragment();
		// Supply itemID input as an argument.
		Bundle args = new Bundle();
		args.putLong("listID", listID);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("EditListTitleDialogFragment", "onAttach");
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception

		// TODO EditListTitleDialogFragment onAttach ... removed to allow implementing via a fragment
		try {
			mFinishEditListTitleDialogCallback = (EditListTitleDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EditListTitleDialogListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.i("EditListTitleDialogFragment", "onCreateView");
		View view = inflater.inflate(R.layout.dialog_edit_list_title, container);
		txtEditListTitle = (EditText) view.findViewById(R.id.txtEditListTitle);

		btnApply = (Button) view.findViewById(R.id.btnApply);
		btnApply.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String newListTitle = txtEditListTitle.getText().toString().trim();
				ContentValues newFieldValues = new ContentValues();
				newFieldValues.put(ListsTable.COL_LIST_TITLE, newListTitle);
				listSettings.updateListsTableFieldValues(newFieldValues);
				mFinishEditListTitleDialogCallback.onApplyEditListTitleDialog(newListTitle);
				getDialog().dismiss();
			}
		});

		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mFinishEditListTitleDialogCallback.onCancelEditListTitleDialog();
				getDialog().dismiss();
			}
		});

		getDialog().setTitle(R.string.dialog_edit_list_title);

		// Show soft keyboard automatically
		txtEditListTitle.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("EditListTitleDialogFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mListID = bundle.getLong("listID", 0);
		}
		if (mListID > 0) {
			listSettings = new ListSettings(getActivity(), mListID);
		}

		txtEditListTitle.setText(listSettings.getListTitle());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("EditListTitleDialogFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyLog.i("EditListTitleDialogFragment", "onCreateDialog");
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		MyLog.i("EditListTitleDialogFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		MyLog.i("EditListTitleDialogFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		MyLog.i("EditListTitleDialogFragment", "onDismiss");
		super.onDismiss(dialog);
	}

	@Override
	public void onStart() {
		MyLog.i("EditListTitleDialogFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		MyLog.i("EditListTitleDialogFragment", "onStop");
		super.onStop();
	}

}
