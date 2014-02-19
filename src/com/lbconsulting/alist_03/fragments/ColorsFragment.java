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

public class ColorsFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	//OnTaskSelectedListener mCallback;
	private static final int TASKS_LIST_LOADER_ID = 1;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mColorsCallbacks;

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("ColorsFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("ColorsFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("ColorsFragment", "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("ColorsFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("ColorsFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("ColorsFragment", "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		MyLog.i("ColorsFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("ColorsFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("ColorsFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("ColorsFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("ColorsFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("ColorsFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("ColorsFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("ColorsFragment", "onAttach");
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
