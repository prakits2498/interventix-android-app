
package com.federicocolantoni.projects.interventix.intervento;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixAPI;

public class ClientAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    private int nominativoIndex;
    private int codFisIndex;
    private int partitaIVAIndex;

    public ClientAdapter(Context context, Cursor c) {

	super(context, c, ClientAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {

	TextView row_nomin = (TextView) row.getTag(R.id.row_nominativo);
	TextView row_cod_fis = (TextView) row.getTag(R.id.row_cod_fis);
	TextView row_p_iva = (TextView) row.getTag(R.id.row_p_iva);

	if (!mFoundIndexes) {
	    nominativoIndex = cursor
		    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_NOMINATIVO);
	    codFisIndex = cursor
		    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_CODICE_FISCALE);
	    partitaIVAIndex = cursor
		    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_PARTITA_IVA);
	    mFoundIndexes = true;
	}
	String nomin = cursor.getString(nominativoIndex);
	String cod_fis = cursor.getString(codFisIndex);
	String p_iva = cursor.getString(partitaIVAIndex);

	row_nomin.setText(nomin);
	row_cod_fis.setText(cod_fis);
	row_p_iva.setText(p_iva);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup listView) {

	View view = mInflater.inflate(R.layout.client_row, listView, false);

	View tv_nomin = view.findViewById(R.id.row_nominativo);
	view.setTag(R.id.row_nominativo, tv_nomin);

	View tv_cod_fis = view.findViewById(R.id.row_cod_fis);
	view.setTag(R.id.row_cod_fis, tv_cod_fis);

	View tv_p_iva = view.findViewById(R.id.row_p_iva);
	view.setTag(R.id.row_p_iva, tv_p_iva);

	return view;
    }
}
