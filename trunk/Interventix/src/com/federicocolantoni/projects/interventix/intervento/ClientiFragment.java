
package com.federicocolantoni.projects.interventix.intervento;

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
import android.support.v4.app.FragmentTransaction;
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
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixAPI;
import com.federicocolantoni.projects.interventix.services.InterventixIntentService;
import multiface.crypto.cr2.JsonCR2;

import java.util.HashMap;
import java.util.Map;

public class ClientiFragment extends Fragment implements
        LoaderCallbacks<Cursor> {

    private static final String TAG_CLIENTE_DETAIL = "DETAIL";

    private final static int CLIENT_LOADER = 1;

    private SharedPreferences mPrefs;

    private ListView mListClients;
    private ClientAdapter mAdapter;

    private FragmentManager mFragMng;

    private String mJson_req;
    public static boolean sFirstTimeDownloaded = false;

    private MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {

            String action = (String) msg.obj;

            if (action.equals(Constants.GET_LISTA_CLIENTI)) {
                getActivity();
                if (msg.arg1 == Activity.RESULT_OK) {
                    Bundle bundle = msg.getData();

                    if (bundle.getBoolean(Constants.DOWNLOADED) == true) {

                        Toast.makeText(
                                getActivity(),
                                "La lista clienti e' stata scaricata e aggiornata.",
                                Toast.LENGTH_LONG).show();
                    } else {

                        if (bundle.getBoolean(Constants.REV_UPDATED) == true) {

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

    private static class MyHandler extends Handler {

    }

    public static class SimpleDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

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

            return builder.create();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.clienti_fragment, container,
                false);

        mFragMng = getActivity().getSupportFragmentManager();

        mListClients = (ListView) v.findViewById(R.id.list_clients);
        mListClients.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {

                Cursor cur = (Cursor) adapter.getItemAtPosition(position);

                boolean ok = cur.moveToPosition(position);

                if (ok) {
                    FragmentTransaction xact = mFragMng.beginTransaction();

                    ClienteDetailFragment msgFrag = new ClienteDetailFragment();

                    Bundle bun = new Bundle();

                    bun.putString(
                            Constants.NOMINATIVO_CLIENTE,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_NOMINATIVO)));
                    bun.putString(
                            Constants.CODICE_FISCALE,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_CODICE_FISCALE)));
                    bun.putString(
                            Constants.PARTITA_IVA,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_PARTITA_IVA)));
                    bun.putString(
                            Constants.TELEFONO,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_TELEFONO)));
                    bun.putString(
                            Constants.FAX,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_FAX)));
                    bun.putString(
                            Constants.EMAIL,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_EMAIL)));
                    bun.putString(
                            Constants.REFERENTE,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_REFERENTE)));
                    bun.putString(
                            Constants.CITTA,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_CITTA)));
                    bun.putString(
                            Constants.INDIRIZZO,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_INDIRIZZO)));
                    bun.putString(
                            Constants.INTERNO,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_INTERNO)));
                    bun.putString(
                            Constants.UFFICIO,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_UFFICIO)));
                    bun.putString(
                            Constants.NOTE,
                            cur.getString(cur
                                    .getColumnIndex(InterventixAPI.Cliente.Fields.KEY_NOTE)));

                    msgFrag.setArguments(bun);

                    xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    xact.replace(R.id.frame_for_fragments, msgFrag,
                            TAG_CLIENTE_DETAIL);
                    xact.commit();
                }
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
                            Log.d(Constants.DEBUG_TAG,
                                    "NUMBER_FORMAT_EXCEPTION!", e);
                        } catch (Exception e) {
                            Log.d(Constants.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
                        }

                        Intent intent = new Intent(Constants.GET_LISTA_CLIENTI,
                                null, getActivity(),
                                InterventixIntentService.class);

                        Messenger msn = new Messenger(mHandler);

                        intent.putExtra(Constants.REQUEST_GET_LISTA_CLIENTI,
                                mJson_req);
                        intent.putExtra(Constants.MESSENGER, msn);
                        intent.putExtra(Constants.DOWNLOADED, false);

                        getActivity().startService(intent);
                    }
                });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mPrefs = getActivity().getSharedPreferences(
                Constants.GLOBAL_PREFERENCES, Context.MODE_PRIVATE);

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
            Log.d(Constants.DEBUG_TAG, "NUMBER_FORMAT_EXCEPTION!", e);
        } catch (Exception e) {
            Log.d(Constants.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
        }

        Intent intent = new Intent(Constants.GET_LISTA_CLIENTI, null,
                getActivity(), InterventixIntentService.class);

        Messenger msn = new Messenger(mHandler);

        intent.putExtra(Constants.REQUEST_GET_LISTA_CLIENTI, mJson_req);
        intent.putExtra(Constants.MESSENGER, msn);
        intent.putExtra(Constants.DOWNLOADED, sFirstTimeDownloaded);

        getActivity().startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

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
