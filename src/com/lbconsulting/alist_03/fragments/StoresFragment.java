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

public class StoresFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	//OnTaskSelectedListener mCallback;
	private static final int TASKS_LIST_LOADER_ID = 1;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mStoresCallbacks;

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("StoresFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("StoresFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("StoresFragment", "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("StoresFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("StoresFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("StoresFragment", "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		MyLog.i("StoresFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("StoresFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("StoresFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("StoresFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("StoresFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("StoresFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("StoresFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("StoresFragment", "onCreateLoader");
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		MyLog.i("StoresFragment", "onLoadFinished");

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		MyLog.i("StoresFragment", "onLoaderReset");

	}
}
