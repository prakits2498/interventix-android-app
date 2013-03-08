
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

	EditText edit_nomin = (EditText) v.findViewById(R.id.edit_nomin_dtl);
	edit_nomin.setText(mBundle.getString("NOMINATIVO"));
	edit_nomin.setEnabled(false);
	edit_nomin.setTextColor(Color.BLACK);

	EditText edit_codfis = (EditText) v.findViewById(R.id.edit_codfis_dtl);
	edit_codfis.setText(mBundle.getString("CODICE_FISCALE"));
	edit_codfis.setEnabled(true);
	edit_codfis.setTextColor(Color.BLACK);

	EditText edit_partitaiva = (EditText) v
		.findViewById(R.id.edit_partitaiva_dtl);
	edit_partitaiva.setText(mBundle.getString("PARTITA_IVA"));
	edit_partitaiva.setEnabled(false);
	edit_partitaiva.setTextColor(Color.BLACK);

	EditText edit_telefono = (EditText) v
		.findViewById(R.id.edit_telefono_dtl);
	edit_telefono.setText(mBundle.getString("TELEFONO"));
	edit_telefono.setEnabled(false);
	edit_telefono.setTextColor(Color.BLACK);

	EditText edit_fax = (EditText) v.findViewById(R.id.edit_fax_dtl);
	edit_fax.setText(mBundle.getString("FAX"));
	edit_fax.setEnabled(false);
	edit_fax.setTextColor(Color.BLACK);

	EditText edit_email = (EditText) v.findViewById(R.id.edit_email_dtl);
	edit_email.setText(mBundle.getString("EMAIL"));
	edit_email.setEnabled(false);
	edit_email.setTextColor(Color.BLACK);

	EditText edit_refer = (EditText) v
		.findViewById(R.id.edit_referente_dtl);
	edit_refer.setText(mBundle.getString("REFERENTE"));
	edit_refer.setEnabled(false);
	edit_refer.setTextColor(Color.BLACK);

	EditText edit_citta = (EditText) v.findViewById(R.id.edit_citta_dtl);
	edit_citta.setText(mBundle.getString("CITTA"));
	edit_citta.setEnabled(false);
	edit_citta.setTextColor(Color.BLACK);

	EditText edit_indirizzo = (EditText) v
		.findViewById(R.id.edit_indirizzo_dtl);
	edit_indirizzo.setText(mBundle.getString("INDIRIZZO"));
	edit_indirizzo.setEnabled(false);
	edit_indirizzo.setTextColor(Color.BLACK);

	EditText edit_interno = (EditText) v
		.findViewById(R.id.edit_interno_dtl);
	edit_interno.setText(mBundle.getString("INTERNO"));
	edit_interno.setEnabled(false);
	edit_interno.setTextColor(Color.BLACK);

	EditText edit_ufficio = (EditText) v
		.findViewById(R.id.edit_ufficio_dtl);
	edit_ufficio.setText(mBundle.getString("UFFICIO"));
	edit_ufficio.setEnabled(false);
	edit_ufficio.setTextColor(Color.BLACK);

	EditText edit_note = (EditText) v.findViewById(R.id.edit_note_dtl);
	edit_note.setText(mBundle.getString("NOTE"));
	edit_note.setEnabled(false);
	edit_note.setTextColor(Color.BLACK);

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
