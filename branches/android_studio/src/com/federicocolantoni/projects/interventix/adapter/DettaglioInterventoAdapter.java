package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.federicocolantoni.projects.interventix.R;

public class DettaglioInterventoAdapter extends CursorAdapter {

	private final LayoutInflater mInflater;
	private final boolean mFoundIndexes;

	public DettaglioInterventoAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mInflater = LayoutInflater.from(context);
		mFoundIndexes = false;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup listView) {

		View view = mInflater.inflate(R.layout.interv_row, listView, false);

		return view;
	}
}
