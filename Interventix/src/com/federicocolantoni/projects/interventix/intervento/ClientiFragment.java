
package com.federicocolantoni.projects.interventix.intervento;

import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.MainActivity;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixAPI;
import com.federicocolantoni.projects.interventix.services.InterventixIntentService;

@SuppressLint({ "NewApi", "ValidFragment", "HandlerLeak" })
public class ClientiFragment extends Fragment implements
	LoaderCallbacks<Cursor> {

    public static final String GET_LISTA_CLIENTI = "com.federico.colantoni.projects.interventix.GET_LISTA_CLIENTI";
    static final String GLOBAL_PREFERENCES = "Preferences";

    private static final String TAG_CLIENTE_DETAIL = "DETAIL";

    private final static int CLIENT_LOADER = 1;

    private SharedPreferences mPrefs;

    private ListView mListClients;
    private ClientAdapter mAdapter;

    private FragmentManager fragMng;

    private String mJson_req;
    public static boolean firstTimeDownloaded = false;

    private Handler mHandler = new Handler() {

	@Override
	public void handleMessage(Message msg) {

	    String action = (String) msg.obj;

	    if (action.equals(GET_LISTA_CLIENTI)) {
		getActivity();
		if (msg.arg1 == Activity.RESULT_OK) {
		    Bundle bundle = msg.getData();

		    if (bundle.getBoolean("DOWNLOADED") == true) {

			Toast.makeText(
				getActivity(),
				"La lista clienti e' stata scaricata e aggiornata.",
				Toast.LENGTH_LONG).show();
		    } else {

			if (bundle.getBoolean("REV_UPDATED") == true) {

			    Toast.makeText(getActivity(),
				    "La lista clienti e' gia' aggiornata.",
				    Toast.LENGTH_LONG).show();
			} else {

			    DialogFragment newFragment = new SimpleDialogFragment();
			    newFragment.show(getActivity()
				    .getSupportFragmentManager(),
				    "ALERT_DIALOG");
			}
		    }
		} else {

		    Toast.makeText(
			    getActivity(),
			    "Impossibile recuperare le informazioni sui clienti",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	}
    };

    private class SimpleDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    // Use the Builder class for convenient dialog construction
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(
		    "La lista clienti e' gia' scaricata ma potrebbe non essere aggiornata.\n"
			    + "Per aggiornarla, cliccare su \"Ricarica lista clienti\"")
		    .setPositiveButton("OK",
			    new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog,
					int id) {

				    dialog.dismiss();
				}
			    });

	    // Create the AlertDialog object and return it
	    return builder.create();
	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.clienti_fragment, container,
		false);

	mListClients = (ListView) v.findViewById(R.id.list_clients);
	mListClients.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view,
		    int position, long id) {

		/*Cursor cur = (Cursor) adapter.getItemAtPosition(position);

		boolean ok = cur.moveToPosition(position);

		if (ok) {
		    FragmentTransaction xact = fragMng.beginTransaction();

		    ClienteDetailFragment msgFrag = new ClienteDetailFragment();

		    Bundle bun = new Bundle();

		    bun.putString(
		        "NAME",
		        cur.getString(cur
		    	    .getColumnIndex(MicroBloggingAPI.Messaggio.Fields.NAME)));
		    bun.putString(
		        "TITLE",
		        cur.getString(cur
		    	    .getColumnIndex(MicroBloggingAPI.Messaggio.Fields.TITLE)));
		    bun.putLong(
		        "DATE",
		        cur.getLong(cur
		    	    .getColumnIndex(MicroBloggingAPI.Messaggio.Fields.DATE)));
		    bun.putString(
		        "BODY",
		        cur.getString(cur
		    	    .getColumnIndex(MicroBloggingAPI.Messaggio.Fields.BODY)));

		    msgFrag.setArguments(bun);

		    xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    xact.replace(R.id.frame_for_fragments, msgFrag,
			    TAG_CLIENTE_DETAIL);
		    xact.commit();
		}*/
	    }
	});

	v.findViewById(R.id.btn_reload_clients).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(View v) {

			try {

			    Map<String, Long> param = new HashMap<String, Long>();
			    param.put("revision", mPrefs.getLong("REVISION", 0));

			    mJson_req = JsonCR2.createRequest(
				    "clients",
				    "syncro",
				    param,
				    mPrefs.getInt("ID_USER",
					    Integer.valueOf(-1)));
			} catch (NumberFormatException e) {
			    Log.d(MainActivity.DEBUG_TAG,
				    "NUMBER_FORMAT_EXCEPTION!", e);
			} catch (Exception e) {
			    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!",
				    e);
			}

			Intent intent = new Intent(GET_LISTA_CLIENTI, null,
				getActivity(), InterventixIntentService.class);

			Messenger msn = new Messenger(mHandler);

			intent.putExtra("REQUEST_GET_LISTA_CLIENTI", mJson_req);
			intent.putExtra("MESSENGER", msn);
			intent.putExtra("DOWNLOADED", false);

			getActivity().startService(intent);
		    }
		});

	return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

	super.onActivityCreated(savedInstanceState);

	mPrefs = getActivity().getSharedPreferences(GLOBAL_PREFERENCES,
		Context.MODE_PRIVATE);

	mAdapter = new ClientAdapter(getActivity(), null);
	mListClients.setAdapter(mAdapter);
	getActivity().getSupportLoaderManager().initLoader(CLIENT_LOADER, null,
		this);

	try {

	    Map<String, Long> param = new HashMap<String, Long>();
	    param.put("revision", mPrefs.getLong("REVISION", 0));

	    mJson_req = JsonCR2.createRequest("clients", "syncro", param,
		    mPrefs.getInt("ID_USER", Integer.valueOf(-1)));
	} catch (NumberFormatException e) {
	    Log.d(MainActivity.DEBUG_TAG, "NUMBER_FORMAT_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}

	Intent intent = new Intent(GET_LISTA_CLIENTI, null, getActivity(),
		InterventixIntentService.class);

	Messenger msn = new Messenger(mHandler);

	intent.putExtra("REQUEST_GET_LISTA_CLIENTI", mJson_req);
	intent.putExtra("MESSENGER", msn);
	intent.putExtra("DOWNLOADED", firstTimeDownloaded);

	getActivity().startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

	CursorLoader loader = new CursorLoader(getActivity(),
		InterventixAPI.Cliente.URI, null, " cancellato=0 ", null,
		InterventixAPI.Cliente.Fields.KEY_NOMINATIVO + " asc");

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

	mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

	mAdapter.swapCursor(null);
    }
}
