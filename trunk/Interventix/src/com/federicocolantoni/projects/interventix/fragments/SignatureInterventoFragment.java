package com.federicocolantoni.projects.interventix.fragments;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.caverock.androidsvg.SVGImageView;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.task.GetSignatureInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.DrawingView;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@SuppressLint("NewApi")
@EFragment(R.layout.signature_fragment)
public class SignatureInterventoFragment extends Fragment implements OnClickListener {
    
    @ViewById(R.id.signature)
    ImageView signature;
    
    @ViewById(R.id.brush)
    SVGImageView brush;
    
    @ViewById(R.id.eraser)
    SVGImageView eraser;
    
    @ViewById(R.id.layout_drawer)
    LinearLayout layout_drawer;
    
    @ViewById(R.id.signature_drawer)
    DrawingView drawer;
    
    @ViewById(R.id.tv_summary_intervention)
    TextView summary;
    
    private ActionMode mActionModeSignature;
    
    public static long sId_Intervento;
    
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
	    
	    mode.setTitle("Opzioni");
	    
	    return true;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
	    
	    switch (menuItem.getItemId()) {
	    
		case R.id.save_signature:
		    mode.finish();
		    
		    DrawingView newSignature = (DrawingView) layout_drawer.findViewById(R.id.signature_drawer);
		    newSignature.setDrawingCacheEnabled(true);
		    
		    Bitmap firma = newSignature.getDrawingCache();
		    
		    ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    
		    firma.compress(Bitmap.CompressFormat.JPEG, 0, stream);
		    byte[] image = stream.toByteArray();
		    
		    String hexSignature = Utils.bytesToHex(image);
		    
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
    
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
    };
    
    @Override
    public void onStart() {
	
	super.onStart();
	
	Bundle bundle = getArguments();
	
	sId_Intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
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
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Intervento " + sId_Intervento);
	
	summary.setText("Firma");
	
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
		
		mActionModeSignature = ((ActionBarActivity) getActivity()).startSupportActionMode(mActionModeCallback);
		v.setSelected(true);
		
		signature.setVisibility(View.GONE);
		
		layout_drawer.setVisibility(View.VISIBLE);
		
		return true;
	    }
	});
	
	brush.setOnClickListener(this);
	eraser.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
	
	switch (v.getId()) {
	    case R.id.brush:
		
		InterventixToast.makeToast(getActivity(), "Modalità scrittura", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.eraser:
		
		InterventixToast.makeToast(getActivity(), "Modalità gomma", Toast.LENGTH_SHORT);
		
		break;
	    default:
		break;
	}
    }
}
