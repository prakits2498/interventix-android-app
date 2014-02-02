package com.federicocolantoni.projects.interventix.fragments;

import java.io.ByteArrayOutputStream;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVGImageView;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.utils.DrawingView;
import com.federicocolantoni.projects.interventix.utils.Utils;

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

	DrawingView drawer;

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

			mode.setTitle("Opzioni");

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {

			switch (menuItem.getItemId()) {

				case R.id.menu_save_signature:

					mode.finish();

					Bitmap firma = drawer.getDrawingCache();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();

					firma.compress(Bitmap.CompressFormat.PNG, 100, stream);

					byte[] image = stream.toByteArray();

					String hexSignature = Utils.bytesToHex(image);

					InterventoController.controller.getIntervento().setFirma(hexSignature);

					layout_drawer.setVisibility(View.GONE);
					signature.setVisibility(View.VISIBLE);

					layout_drawer.removeView(drawer);

					updateUI();

					return true;

				case R.id.menu_cancel:

					mode.finish();

					layout_drawer.setVisibility(View.GONE);
					signature.setVisibility(View.VISIBLE);

					layout_drawer.removeView(drawer);

					return true;

				default:
					return false;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setHasOptionsMenu(true);
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {

		super.onStart();

		if (!InterventoController.controller.getIntervento().isNuovo())
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
					getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - " + getString(R.string.row_signature));
		else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_signature));

		signature.setDrawingCacheEnabled(true);

		brush.setOnClickListener(this);
		eraser.setOnClickListener(this);
	}

	@Override
	public void onResume() {

		super.onResume();

		updateUI();
	}

	private void updateUI() {

		if (InterventoController.controller.getIntervento().getFirma().length() != 0) {
			String hexSignature = InterventoController.controller.getIntervento().getFirma();

			byte[] byteSignature = Utils.hexToBytes(hexSignature.toCharArray());

			Bitmap bitmapSignature = BitmapFactory.decodeByteArray(byteSignature, 0, byteSignature.length);

			if (bitmapSignature != null)
				signature.setImageBitmap(bitmapSignature);
		} else {

			signature.setImageResource(R.drawable.signature_placeholder);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.brush:

				drawer.setErase(false);

				break;

			case R.id.eraser:

				drawer.setErase(true);

				break;
			default:
				break;
		}
	}

	@LongClick(R.id.signature)
	boolean initActionMode(View v) {

		if (mActionModeSignature != null)
			return false;

		mActionModeSignature = ((ActionBarActivity) getActivity()).startSupportActionMode(mActionModeCallback);
		v.setSelected(true);

		signature.setVisibility(View.GONE);

		layout_drawer.setVisibility(View.VISIBLE);

		drawer = new DrawingView(getActivity());
		drawer.setDrawingCacheEnabled(true);
		drawer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

		layout_drawer.addView(drawer);

		return true;
	}
}
