
package com.federicocolantoni.projects.interventix.intervento;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.federicocolantoni.projects.interventix.R;

public class ClienteDetailFragment extends Fragment implements OnClickListener {

    private static final String TAG_LIST_CLIENTS = "LIST_CLIENTS";
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.client_detail_fragment,
		container, false);

	v.findViewById(R.id.btn_client_dtl_back).setOnClickListener(this);

	mBundle = getArguments();

	EditText edit_nome = (EditText) v.findViewById(R.id.edit_nomin_dtl);
	edit_nome.setText(mBundle.getString("NOMINATIVO"));
	edit_nome.setEnabled(false);
	edit_nome.setTextColor(Color.BLACK);

	EditText edit_title = (EditText) v.findViewById(R.id.edit_codfis_dtl);
	edit_title.setText(mBundle.getString("CODICE_FISCALE"));
	edit_title.setEnabled(true);
	edit_nome.setTextColor(Color.BLACK);

	EditText edit_date = (EditText) v
		.findViewById(R.id.edit_partitaiva_dtl);
	edit_date.setText(mBundle.getString("PARTITA_IVA"));
	edit_date.setEnabled(false);
	edit_nome.setTextColor(Color.BLACK);

	return v;
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	    case R.id.btn_client_dtl_back:

		FragmentManager fragMng = getFragmentManager();
		FragmentTransaction xact = fragMng.beginTransaction();
		xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		xact.replace(R.id.frame_for_fragments, new ClientiFragment(),
			TAG_LIST_CLIENTS);
		xact.commit();
		break;
	}
    }
}
