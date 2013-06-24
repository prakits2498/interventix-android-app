
package com.federicocolantoni.projects.interventix.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

@SuppressLint("NewApi")
public class InformationsInterventoFragment extends SherlockFragment implements
	OnTouchListener, android.view.View.OnClickListener {

    private GestureDetectorCompat mDetector;

    private BroadcastReceiver mMakeChangesReceiver;

    //    private VelocityTracker mVelocity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	mDetector = new GestureDetectorCompat(getActivity(),
		new MyGestureListener());

	final View view = inflater.inflate(
		R.layout.information_intervento_fragment, container, false);

	mMakeChangesReceiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {

		SharedPreferences prefs = getActivity().getSharedPreferences(
			Constants.PREFERENCES, Context.MODE_PRIVATE);

		updateUI(prefs.getBoolean(Constants.EDIT_MODE, false), view);
	    }
	};

	final SharedPreferences prefs = getActivity().getSharedPreferences(
		Constants.PREFERENCES, Activity.MODE_PRIVATE);

	Bundle bundle = getArguments();

	Intervento interv = (Intervento) bundle
		.getSerializable(Constants.INTERVENTO);

	TextView info_interv = (TextView) view
		.findViewById(R.id.tv_info_intervention);
	info_interv.setText("Informazioni Intervento "
		+ interv.getmIdIntervento());

	View tipologia = view.findViewById(R.id.row_tipology);

	tipologia.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		//		float x1 = 0, x2, y1 = 0, y2, dx = 0, dy = 0;

		//		int pointer = event.getPointerId(0);

		int actonIndex = event.getActionIndex();
		int pointerID = event.getPointerId(actonIndex);

		float firstXPoint = 0, lastXPoint = 0, firstYPoint = 0, lastYPoint = 0;

		switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:

			firstXPoint = event.getX(pointerID);
			firstYPoint = event.getY(pointerID);

			if (prefs.getBoolean(Constants.EDIT_MODE, false)) {

			    new SetTipologia().show(getActivity()
				    .getSupportFragmentManager(),
				    Constants.TIPOLOGIA_DIALOG_FRAGMENT);
			}

			break;

		    case MotionEvent.ACTION_MOVE:

			lastXPoint = event.getX(event.getPointerCount() - 1);
			lastYPoint = event.getY(event.getPointerCount() - 1);

			if (Math.abs(firstYPoint - lastYPoint) > Constants.SWIPE_MAX_OFF_PATH) {
			    return false;
			} else if (lastXPoint - firstXPoint > Constants.SWIPE_MIN_DISTANCE) {

			    if (!prefs.getBoolean(Constants.EDIT_MODE, false)) {
				FragmentManager manager = getActivity()
					.getSupportFragmentManager();
				manager.popBackStackImmediate();
			    }
			}

			break;

		    case MotionEvent.ACTION_UP:

			break;
		}

		return true;
	    }
	});

	TextView tv_tipology = (TextView) tipologia
		.findViewById(R.id.tv_row_tipology);
	tv_tipology.setText(interv.getmTipologia());

	View mode = view.findViewById(R.id.row_mode);
	mode.setOnClickListener(this);

	TextView tv_mode = (TextView) mode.findViewById(R.id.tv_row_mode);
	tv_mode.setText(interv.getmModalita());

	View product = view.findViewById(R.id.row_product);
	product.setOnClickListener(this);

	TextView tv_product = (TextView) product
		.findViewById(R.id.tv_row_product);
	tv_product.setText(interv.getmProdotto());

	View motivation = view.findViewById(R.id.row_motivation);
	motivation.setOnClickListener(this);

	TextView tv_motivation = (TextView) motivation
		.findViewById(R.id.tv_row_motivation);
	tv_motivation.setText(interv.getmMotivo());

	View nominativo = view.findViewById(R.id.row_name);
	nominativo.setOnClickListener(this);

	TextView tv_nominativo = (TextView) nominativo
		.findViewById(R.id.tv_row_name);
	tv_nominativo.setText(interv.getmNominativo());

	View date_interv = view.findViewById(R.id.row_date);
	date_interv.setOnClickListener(this);

	TextView tv_date_interv = (TextView) date_interv
		.findViewById(R.id.tv_row_date);
	tv_date_interv.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm",
		Locale.ITALY).format(new Date(interv.getmDataOra())));

	view.setOnTouchListener(this);

	return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {

	super.onPause();

	getActivity().unregisterReceiver(mMakeChangesReceiver);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {

	super.onResume();

	getActivity().registerReceiver(mMakeChangesReceiver,
		new IntentFilter(Constants.EDIT_MODE));
    }

    private void updateUI(boolean editMode, View view) {

	if (editMode) {

	    System.out.println("EDIT_MODE enabled");
	    View tipologia = view.findViewById(R.id.row_tipology);

	    tipologia.setBackgroundColor(Color.rgb(255, 0, 51));
	} else {

	    System.out.println("EDIT_MODE disabled");
	    View tipologia = view.findViewById(R.id.row_tipology);

	    tipologia.setBackgroundColor(Color.WHITE);
	}
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

	if (mDetector.onTouchEvent(event)) {
	    return true;
	} else {
	    return false;
	}
    }

    public class MyGestureListener extends
	    GestureDetector.SimpleOnGestureListener {

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
		float velocityY) {

	    // int delta = 0;
	    if (Math.abs(e1.getY() - e2.getY()) > Constants.SWIPE_MAX_OFF_PATH) {
		return false;
	    } else if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
		//left to right swipe

		FragmentManager manager = getActivity()
			.getSupportFragmentManager();
		manager.popBackStackImmediate();
	    }

	    return true;
	}
    }

    public static class SetTipologia extends SherlockDialogFragment implements
	    OnClickListener {

	public SetTipologia() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder tipologia = new Builder(getActivity());

	    tipologia.setTitle(getResources().getString(
		    R.string.tipologia_title));
	    final String[] choices = getResources().getStringArray(
		    R.array.tipologia_choose);
	    tipologia.setSingleChoiceItems(choices, -1,
		    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			    if (which == 0) {
				TextView tv_tipology = (TextView) getActivity()
					.findViewById(R.id.tv_row_tipology);
				tv_tipology.setText(choices[which]);
			    } else {
				TextView tv_tipology = (TextView) getActivity()
					.findViewById(R.id.tv_row_tipology);
				tv_tipology.setText(choices[which]);
			    }
			}
		    });

	    tipologia.setPositiveButton(
		    getResources().getString(R.string.ok_btn), this);

	    return tipologia.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    dialog.dismiss();
	}
    }
}
