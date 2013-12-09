package com.federicocolantoni.projects.interventix.fragments;

import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetSignatureInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;

@SuppressLint("NewApi")
public class SignatureInterventoFragment extends Fragment {
    
    private ImageView signature;
    
    private LinearLayout layout_drawer;
    
    private ActionMode mActionModeSignature;
    
    private ActionMode.Callback mActionModeCallback = new Callback() {
	
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    
	    return false;
	}
	
	@Override
	public void onDestroyActionMode(ActionMode mode) {
	    
	    mActionModeSignature = null;
	}
	
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    
	    MenuInflater inflater = mode.getMenuInflater();
	    inflater.inflate(R.menu.context_menu_signature, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
	    
	    switch (menuItem.getItemId()) {
	    
		case R.id.save_signature:
		    mode.finish();
		    layout_drawer.setVisibility(View.GONE);
		    signature.setVisibility(View.VISIBLE);
		    return true;
		    
		case R.id.cancel:
		    mode.finish();
		    layout_drawer.setVisibility(View.GONE);
		    signature.setVisibility(View.VISIBLE);
		    return true;
		    
		default:
		    return false;
	    }
	}
    };
    
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
	
	summary.setText("Firma");
	
	signature = (ImageView) view.findViewById(R.id.signature);
	signature.setDrawingCacheEnabled(true);
	
	String hexSignature = interv.getmFirma();
	
	byte[] byteSignature = Utils.hexToBytes(hexSignature.toCharArray());
	
	System.out.println("Hex signature: " + hexSignature);
	System.out.println("Byte[] signature: " + byteSignature);
	
	Bitmap bitmapSignature = BitmapFactory.decodeByteArray(byteSignature, 0, byteSignature.length);
	
	if (bitmapSignature != null)
	    signature.setImageBitmap(bitmapSignature);
	else
	    System.out.println("Error to generate image from hex string!");
	
	signature.setOnLongClickListener(new OnLongClickListener() {
	    
	    @Override
	    public boolean onLongClick(View v) {
		
		if (mActionModeSignature != null)
		    return false;
		
		// TODO aprire un menu contestuale per salvare o annullare
		// l'operazione di modifica della firma
		
		InterventixToast.makeToast(getActivity(), "TODO: modificare la firma", Toast.LENGTH_SHORT);
		
		mActionModeSignature = ((ActionBarActivity) getActivity()).startSupportActionMode(mActionModeCallback);
		
		signature.setVisibility(View.GONE);
		
		layout_drawer = (LinearLayout) ((ActionBarActivity) getActivity()).findViewById(R.id.layout_signature_drawer);
		layout_drawer.setVisibility(View.VISIBLE);
		
		return true;
	    }
	});
	
	return view;
    }
}
