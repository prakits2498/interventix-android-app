
package com.federicocolantoni.projects.interventix.intervento;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;

public class ClientAdapter extends BaseAdapter {

    private final List<Cliente> mList;
    private final LayoutInflater mInflater;

    public ClientAdapter(List<Cliente> list, Context context) {

	mList = list;
	mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

	return mList.size();
    }

    @Override
    public Object getItem(int position) {

	return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	final View v;
	final ViewHolder holder;

	if (convertView == null) {
	    v = mInflater.inflate(R.layout.client_row, parent, false);
	} else {
	    v = convertView;
	    holder = (ViewHolder) v.getTag();
	}

	return v;
    }

    private static class ViewHolder {

	public TextView nome;
	public TextView lang;
    }
}
