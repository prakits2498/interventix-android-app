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
    
    /**
     * State of ListView item that has never been determined.
     */
    private static final int STATE_UNKNOWN = 0;
    
    /**
     * State of a ListView item that is sectioned. A sectioned item must
     * display the separator.
     */
    private static final int STATE_SECTIONED_CELL = 1;
    
    /**
     * State of a ListView item that is not sectioned and therefore does not
     * display the separator.
     */
    private static final int STATE_REGULAR_CELL = 2;
    
    private int[] mCellStates;
    
    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    
    private int mNomeUserIndex;
    private int mCognomeUserIndex;
    private int mIdUtente;
    private int mTipoUtente;
    
    public ListUsersAdapter(Context context, Cursor cursor) {
	super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }
    
    @Override
    public void bindView(View row, Context context, Cursor cursor) {
	
	TextView separator = (TextView) row.findViewById(R.id.separator);
	CheckedTextView user = (CheckedTextView) row.getTag(R.id.users_nth);
	TextView id_user = (TextView) row.getTag(R.id.users_nth_id);
	
	/*
	 * Separator
	 */
	boolean needSeparator = false;
	
	mCellStates = cursor == null ? null : new int[cursor.getCount()];
	
	final int position = cursor.getPosition();
	
	switch (mCellStates[position]) {
	    case STATE_SECTIONED_CELL:
		
		needSeparator = true;
		
		break;
	    
	    case STATE_REGULAR_CELL:
		
		needSeparator = false;
		
		break;
	    
	    case STATE_UNKNOWN:
		
		break;
	    
	    default:
		
		// A separator is needed if it's the first itemview of the
		// ListView or if the group of the current cell is different
		// from the previous itemview.
		
		if (position == 0) {
		    needSeparator = true;
		}
		else {
		    
		    cursor.moveToPosition(position);
		}
		
		mCellStates[position] = needSeparator ? STATE_SECTIONED_CELL : STATE_REGULAR_CELL;
		
		break;
	}
	
	if (!mFoundIndexes) {
	    
	    mNomeUserIndex = cursor.getColumnIndex(UtenteDB.Fields.NOME);
	    mCognomeUserIndex = cursor.getColumnIndex(UtenteDB.Fields.COGNOME);
	    mIdUtente = cursor.getColumnIndex(UtenteDB.Fields.ID_UTENTE);
	    mTipoUtente = cursor.getColumnIndex(UtenteDB.Fields.TIPO);
	    
	    mFoundIndexes = true;
	}
	
	if (needSeparator) {
	    separator.setText(cursor.getString(mTipoUtente).equalsIgnoreCase(context.getString(R.string.add_user_list_users)) ? context.getString(R.string.add_user_list_users) : context.getString(R.string.add_user_list_admin));
	    separator.setVisibility(View.VISIBLE);
	}
	else {
	    separator.setVisibility(View.GONE);
	}
	
	String utente = cursor.getString(mNomeUserIndex) + " " + cursor.getString(mCognomeUserIndex);
	user.setText(utente);
	id_user.setText("" + cursor.getLong(mIdUtente));
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	
	View view = mInflater.inflate(R.layout.add_users_to_detail_user_row, parent, false);
	
	TextView separator = (TextView) view.findViewById(R.id.separator);
	CheckedTextView user = (CheckedTextView) view.findViewById(R.id.users_nth);
	TextView id_user = (TextView) view.findViewById(R.id.users_nth_id);
	
	view.setTag(R.id.separator, separator);
	view.setTag(R.id.users_nth, user);
	view.setTag(R.id.users_nth_id, id_user);
	
	return view;
    }
}
