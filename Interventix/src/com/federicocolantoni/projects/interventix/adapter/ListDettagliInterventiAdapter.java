package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;

public class ListDettagliInterventiAdapter extends BaseAdapter {

	private final LayoutInflater mInflater;

	private static SparseBooleanArray modifiedAndNewDetalis = new SparseBooleanArray();

	public ListDettagliInterventiAdapter(Context context) {

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return InterventoController.controller.getListaDettagli().size();
	}

	@Override
	public DettaglioIntervento getItem(int position) {

		return InterventoController.controller.getListaDettagli().get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.list_details_row, parent, false);
		}

		DettaglioIntervento dettaglio = getItem(position);

		if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO) || dettaglio.modificato.equals(Constants.DETTAGLIO_MODIFICATO))
			modifiedAndNewDetalis.put(position, true);
		else
			modifiedAndNewDetalis.put(position, false);

		if (modifiedAndNewDetalis.get(position))
			convertView.setBackgroundResource(R.drawable.list_pressed_modified_item);
		else
			convertView.setBackgroundResource(R.drawable.list_pressed_item);

		TextView tvTypeDetail = (TextView) convertView.findViewById(R.id.tv_type_detail_interv);
		TextView tvObjectDetail = (TextView) convertView.findViewById(R.id.tv_object_detail_interv);

		tvTypeDetail.setText(dettaglio.tipo);
		tvObjectDetail.setText(dettaglio.oggetto);

		if (dettaglio.nuovo) {
			String oldText = tvTypeDetail.getText().toString();
			tvTypeDetail.setText(oldText + " (N)");
		}
		return convertView;
	}
}
