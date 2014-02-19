package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbconsulting.alist_03.utilities.MyLog;

public class GroupsFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	//OnTaskSelectedListener mCallback;
	private static final int TASKS_LIST_LOADER_ID = 1;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mGroupsCallbacks;

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("GroupsFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("GroupsFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("GroupsFragment", "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("GroupsFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("GroupsFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("GroupsFragment", "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		MyLog.i("GroupsFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("GroupsFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("GroupsFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("GroupsFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("GroupsFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("GroupsFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("GroupsFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("GroupsFragment", "onAttach");
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}
}
