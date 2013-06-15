
package com.federicocolantoni.projects.interventix.view;

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

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class InformationsInterventoFragment extends SherlockFragment implements
	OnTouchListener {

    private GestureDetectorCompat mDetector;

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

	Bundle bundle = getArguments();

	Intervento interv = (Intervento) bundle
		.getSerializable(Constants.INTERVENTO);

	TextView info_interv = (TextView) view
		.findViewById(R.id.tv_info_intervention);
	info_interv.setText("Informazioni Intervento "
		+ interv.getmIdIntervento());

	view.setOnTouchListener(this);

	return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

	if (mDetector.onTouchEvent(event)) {
	    return true;
	} else {
	    return false;
	}
    }

    private class MyGestureListener extends
	    GestureDetector.SimpleOnGestureListener {

	@Override
	public boolean onDown(MotionEvent e) {

	    return true;
	}

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
}
