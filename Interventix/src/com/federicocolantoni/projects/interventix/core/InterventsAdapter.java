
package com.federicocolantoni.projects.interventix.core;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class InterventsAdapter extends BaseAdapter {

    private List<Intervento> list;

    public InterventsAdapter(List<Intervento> list) {

	this.list = list;
    }

    @Override
    public int getCount() {

	return list.size();
    }

    @Override
    public Intervento getItem(int position) {

	return list.get(position);
    }

    @Override
    public long getItemId(int position) {

	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	return null;
    }
}
