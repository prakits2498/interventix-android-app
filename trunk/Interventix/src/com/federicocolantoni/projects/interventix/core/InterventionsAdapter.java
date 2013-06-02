
package com.federicocolantoni.projects.interventix.core;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class InterventionsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Intervento> list;

    public InterventionsAdapter(Context context, List<Intervento> list) {

	this.list = list;
	inflater = LayoutInflater.from(context);
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

	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.interv_row, parent, false);
	}

	Intervento interv = getItem(position);

	TextView nomeIntervento = (TextView) convertView
		.findViewById(R.id.tv_nome);
	nomeIntervento.setText("Intervento n° " + interv.getmIdIntervento());

	return convertView;
    }
}
