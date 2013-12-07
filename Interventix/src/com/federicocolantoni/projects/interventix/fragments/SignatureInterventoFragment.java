package com.federicocolantoni.projects.interventix.fragments;

import java.util.concurrent.ExecutionException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetSignatureInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.Utils;

@SuppressLint("NewApi")
public class SignatureInterventoFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
	
	Bundle bundle = getArguments();
	
	final View view = inflater.inflate(R.layout.signature_fragment, container, false);
	
	Intervento interv = null;
	
	try {
	    interv = new GetSignatureInterventoAsyncTask(getActivity()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	}
	catch (InterruptedException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	TextView summary = (TextView) view.findViewById(R.id.tv_summary_intervention);
	
	DateTime dt_interv = new DateTime(interv.getmDataOra(), DateTimeZone.forID("Europe/Rome"));
	
	summary.setText("Interv. " + bundle.getLong(Constants.NUMERO_INTERVENTO) + " del " + dt_interv.toString("dd/MM/yyyy HH:mm"));
	
	ImageView signature = (ImageView) view.findViewById(R.id.signature);
	
	String hexSignature = interv.getmFirma();
	
	System.out.println("Hex signature: " + hexSignature + "\nByte[] signature: " + Utils.stringToByteArray(hexSignature));
	
	Bitmap bitmapSignature = BitmapFactory.decodeStream(IOUtils.toInputStream(hexSignature, Charsets.UTF_8));
	
	if (bitmapSignature != null)
	    signature.setImageBitmap(bitmapSignature);
	else
	    System.out.println("Error to generate image from hex string!");
	
	return view;
    }
}
