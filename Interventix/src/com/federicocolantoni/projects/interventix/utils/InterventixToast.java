package com.federicocolantoni.projects.interventix.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.Interventix;
import com.federicocolantoni.projects.interventix.R;

public class InterventixToast {
    
    public static void makeToast(String text, int duration) {
    
	LayoutInflater inflater = (LayoutInflater) Interventix.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	View custom_toast_layout = inflater.inflate(R.layout.toast_layout_custom, null);
	
	TextView text_toast = (TextView) custom_toast_layout.findViewById(R.id.text_toast);
	text_toast.setText(text);
	
	Toast custom_toast = new Toast(Interventix.getContext());
	custom_toast.setDuration(duration);
	custom_toast.setView(custom_toast_layout);
	custom_toast.show();
    }
}
