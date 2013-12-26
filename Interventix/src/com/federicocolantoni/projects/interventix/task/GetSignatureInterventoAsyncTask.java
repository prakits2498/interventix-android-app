package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Intervento;

public class GetSignatureInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {
    
    private Context mContext;
    
    public GetSignatureInterventoAsyncTask(Context context) {
	
	mContext = context.getApplicationContext();
    }
    
    @Override
    protected Intervento doInBackground(Long... params) {
	
	ContentResolver cr = mContext.getContentResolver();
	
	String[] projection = new String[] {
		InterventoDB.Fields._ID, InterventoDB.Fields.FIRMA, InterventoDB.Fields.DATA_ORA
	};
	
	String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(InterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
	
	Intervento signatureIntervento = new Intervento();
	
	if (cursor.moveToFirst()) {
	    
	    signatureIntervento.setFirma(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
	    signatureIntervento.setDataOra(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for " + this.getClass().getSimpleName() + " is closed");
	}
	
	return signatureIntervento;
    }
    
    @Override
    protected void onPostExecute(Intervento result) {
	
	super.onPostExecute(result);
    }
}
