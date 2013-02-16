
package com.federicocolantoni.projects.interventix.intervento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;

public class ClienteDetailFragment extends Fragment implements OnClickListener {

    private static final String TAG_LIST_CLIENTS = "LIST_CLIENTS";
    private Bundle mBundle;

    private TextView mView_title, mView_body;
    private ScrollView mScroll_client_dtl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.client_detail_fragment,
		container, false);

	v.findViewById(R.id.btn_client_dtl_back).setOnClickListener(this);

	mBundle = getArguments();

	/*EditText edit_nome = (EditText) v.findViewById(R.id.edit_nome);
	edit_nome.setText(bun.getString("NAME"));
	edit_nome.setEnabled(false);

	edit_title = (EditText) v.findViewById(R.id.edit_title);
	edit_title.setText(bun.getString("TITLE"));
	edit_title.setEnabled(true);

	EditText edit_date = (EditText) v.findViewById(R.id.edit_date);
	edit_date.setText(DateFormat.format("MMM dd, yyyy hh:mmaa",
		bun.getLong("DATE")));
	edit_date.setEnabled(false);

	edit_body = (EditText) v.findViewById(R.id.edit_body);
	edit_body.setText(bun.getString("BODY"));
	edit_body.setEnabled(true);*/

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
