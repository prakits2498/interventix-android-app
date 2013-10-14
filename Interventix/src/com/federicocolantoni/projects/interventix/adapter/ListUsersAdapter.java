package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;

public class ListUsersAdapter extends CursorAdapter {
    
    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    
    private int mNomeUserIndex;
    private int mCognomeUserIndex;
    private int mIdUtente;
    private int mTipoUtente;
    
    private boolean separator;
    
    public ListUsersAdapter(Context context, Cursor cursor) {
	
	super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
	separator = false;
    }
    
    @Override
    public void bindView(View row, Context context, Cursor cursor) {
	
	TextView separator = (TextView) row.findViewById(R.id.separator);
	CheckedTextView user = (CheckedTextView) row.getTag(R.id.users_nth);
	TextView id_user = (TextView) row.getTag(R.id.users_nth_id);
	
	if (!mFoundIndexes) {
	    
	    mNomeUserIndex = cursor.getColumnIndex(UtenteDB.Fields.NOME);
	    mCognomeUserIndex = cursor.getColumnIndex(UtenteDB.Fields.COGNOME);
	    mIdUtente = cursor.getColumnIndex(UtenteDB.Fields.ID_UTENTE);
	    mTipoUtente = cursor.getColumnIndex(UtenteDB.Fields.TIPO);
	    
	    mFoundIndexes = true;
	}
	
	String utente = cursor.getString(mNomeUserIndex) + " " + cursor.getString(mCognomeUserIndex);
	user.setText(utente);
	id_user.setText("" + cursor.getLong(mIdUtente));
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	
	View view = mInflater.inflate(R.layout.add_user_to_detail_user_row, parent, false);
	
	TextView separator = (TextView) view.findViewById(R.id.separator);
	CheckedTextView user = (CheckedTextView) view.findViewById(R.id.users_nth);
	TextView id_user = (TextView) view.findViewById(R.id.users_nth_id);
	
	view.setTag(R.id.separator, separator);
	view.setTag(R.id.users_nth, user);
	view.setTag(R.id.users_nth_id, id_user);
	
	return view;
    }
}
