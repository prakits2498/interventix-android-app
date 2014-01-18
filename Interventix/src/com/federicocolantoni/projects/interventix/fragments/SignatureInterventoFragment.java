package com.federicocolantoni.projects.interventix.fragments;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.task.GetSignatureInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.DrawingView;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
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

	@ViewById(R.id.tv_summary_intervention)
	TextView summary;

	private SharedPreferences prefs;

	private ActionMode mActionModeSignature;

	private Intervento interv = null;
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

			case R.id.menu_save_signature:

				mode.finish();

				Bitmap firma = drawer.getDrawingCache();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				firma.compress(Bitmap.CompressFormat.PNG, 100, stream);

				byte[] image = stream.toByteArray();

				String hexSignature = Utils.bytesToHex(image);

				AsyncQueryHandler saveSignature = new AsyncQueryHandler(getActivity().getContentResolver()) {

					@Override
					protected void onUpdateComplete(int token, Object cookie, int result) {

						InterventixToast.makeToast(getActivity(), "Firma aggiornata", Toast.LENGTH_SHORT);

						prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

						final Editor edit = prefs.edit();

						edit.putBoolean(Constants.INTERV_MODIFIED, true);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
							edit.apply();
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {

									edit.commit();
								}
							}).start();
						}

						onStart();
					};
				};

				ContentValues values = new ContentValues();
				values.put(InterventoDB.Fields.FIRMA, hexSignature);
				values.put(InterventoDB.Fields.MODIFICATO, "M");

				String selection = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";

				String[] selectionArgs = new String[] { InterventoDB.INTERVENTO_ITEM_TYPE, "" + sId_Intervento };

				saveSignature.startUpdate(0, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);

				layout_drawer.setVisibility(View.GONE);
				signature.setVisibility(View.VISIBLE);

				layout_drawer.removeView(drawer);

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

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setHasOptionsMenu(true);
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();

		sId_Intervento = bundle.getLong(Constants.ID_INTERVENTO);
	}

	@Override
	public void onStart() {

		super.onStart();

		try {

			interv = new GetSignatureInterventoAsyncTask(getActivity()).execute(sId_Intervento).get();
		} catch (InterruptedException e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		} catch (ExecutionException e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		}

		((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Intervento " + sId_Intervento);

		summary.setText(R.string.signature);

		signature.setDrawingCacheEnabled(true);

		String hexSignature = interv.getFirma();

		byte[] byteSignature = Utils.hexToBytes(hexSignature.toCharArray());

		Bitmap bitmapSignature = BitmapFactory.decodeByteArray(byteSignature, 0, byteSignature.length);

		if (bitmapSignature != null)
			signature.setImageBitmap(bitmapSignature);

		signature.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

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
		});

		brush.setOnClickListener(this);
		eraser.setOnClickListener(this);
	}

	@Override
	public void onPause() {

		super.onPause();

		System.out.println(this.getClass().getSimpleName() + " in pause");
	}

	@Override
	public void onResume() {

		super.onResume();

		System.out.println(this.getClass().getSimpleName() + " in resume");
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
}
