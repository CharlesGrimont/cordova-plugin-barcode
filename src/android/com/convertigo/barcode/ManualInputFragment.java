package com.convertigo.barcode;

import java.util.ArrayList;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ImageView;
import android.util.Log;

public class ManualInputFragment extends Fragment{

	private EditText _inputField;
	private BarecodeOptions barecodeOpts;
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int manualInputFragmentId = getActivity().getApplication().getResources().getIdentifier("fragment_manual_input","layout", getActivity().getApplication().getPackageName());
		ViewGroup rootView = (ViewGroup) inflater.inflate(manualInputFragmentId, container, false);
        
		int manualInputFieldId = getActivity().getApplication().getResources().getIdentifier("manualInputField","id", getActivity().getApplication().getPackageName());
        _inputField =  rootView.findViewById(manualInputFieldId);

		// Get ids
		 int idUp = getResources().getIdentifier("text_view_id_up","id", getActivity().getApplication().getPackageName());
		 int idDown = getResources().getIdentifier("text_view_id_down","id", getActivity().getApplication().getPackageName());
		 int idkbButton = getResources().getIdentifier("kbButton","id", getActivity().getApplication().getPackageName());
		 int idscanButton = getResources().getIdentifier("scanButton","id", getActivity().getApplication().getPackageName());
		 int idImg = getResources().getIdentifier("imageView1","id", getActivity().getApplication().getPackageName());

		 // Get barcodeOpts
		 this.barecodeOpts = ((ConvScannerActivity)inflater.getContext()).barecodeOpts;

		 //Set text up value and color
		 TextView tvtUp = rootView.findViewById(idUp);
		 tvtUp.setText(this.barecodeOpts.getTextUp());
		 tvtUp.setTextColor(this.barecodeOpts.getTextUpColor());

		 //Set text view down value and color
		 TextView tvtDown = rootView.findViewById(idDown);
		 tvtDown.setText(this.barecodeOpts.getTextDown());
		 tvtDown.setTextColor(this.barecodeOpts.getTextDownColor());

		 if(this.barecodeOpts.getImgPath() != null){
			if(!this.barecodeOpts.getImgPath().equals("")){
				try {
					InputStream is = getContext().getAssets().open("www/assets/" + this.barecodeOpts.getImgPath() );
					ImageView imgV = rootView.findViewById(idImg);
					imgV.setImageBitmap(BitmapFactory.decodeStream(is));
					
				} catch(final Exception tx) {
		
				}
			}
		 }
		 

		// Disable button
		 if(this.barecodeOpts.getOnlyKeyboard() || this.barecodeOpts.getOnlyScan()){
			 View kbButton = rootView.findViewById(idkbButton);
			 View scanButton = rootView.findViewById(idscanButton);
			 kbButton.setVisibility(View.GONE);
			 scanButton.setVisibility(View.GONE);
		 }
		 _inputField.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				String text = v.getText().toString();
				if(text.length() == 5 || text.length() == 7 || text.length() == 9 || text.length() == 13)
				{
					 String scanType =  "EAN13"; // Default value

		             switch(text.length()){
		                case 5:
		                case 7:scanType = "CODE128";break;
		                case 9:scanType = "SALECB";break;
		             }

					 if(_inputField != null)
					 {
						 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						 imm.hideSoftInputFromWindow(_inputField.getWindowToken(), 0);
					 }
					 
				    ((ConvScannerActivity)	getActivity()).finish(text, scanType);
					return true;
				}
				else
				{
					Toast.makeText(getActivity(), "Veuillez saisir un CODIC, un EAN, ou un numÃ¯Â¿Â½ro de vente valide", Toast.LENGTH_LONG).show();

					return true;
				}
			}
		});
        
        
        return rootView;
    }

	 public void onFragmentShown()
	 {
		 if(_inputField != null)
		 {
			 _inputField.requestFocus();
			 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			 imm.showSoftInput(_inputField, InputMethodManager.SHOW_FORCED);
		 }
	 }
	 
	 public void onFragmentHidden()
	 {
		 if(_inputField != null)
		 {
			 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			 imm.hideSoftInputFromWindow(_inputField.getWindowToken(), 0);
		 }
	 }
}
