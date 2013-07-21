package com.federicocolantoni.projects.interventix.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.R;

public class InterventixToast {

	public static void makeToast(Context context, String text, int duration) {

		Activity activity = context instanceof Activity ? (Activity) context
				: null;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View custom_toast_layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) activity.findViewById(R.id.toast_layout));

		TextView text_toast = (TextView) custom_toast_layout
				.findViewById(R.id.text_toast);
		text_toast.setText(text);

		Toast custom_toast = new Toast(context.getApplicationContext());
		custom_toast.setDuration(duration);
		custom_toast.setView(custom_toast_layout);
		custom_toast.show();
	}
}
