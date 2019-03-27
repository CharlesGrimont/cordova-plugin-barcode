package com.convertigo.barcode;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;



public class CameraPreviewFragment	extends Fragment implements ConvZBarScannerView.ResultHandler{

	private ConvZBarScannerView _scannerView;
	private ArrayList<Integer> mSelectedIndices;
	private Button _button;
	boolean 	   _cameraState;
	private BarecodeOptions barecodeOpts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		
		int cameraPreviewFragmentId = getActivity().getApplication().getResources().getIdentifier("fragment_camera_preview","layout", getActivity().getApplication().getPackageName());
		ViewGroup rootView = (ViewGroup) inflater.inflate(cameraPreviewFragmentId, container, false);
		int cameraPreviewFrameLayoutId = getActivity().getApplication().getResources().getIdentifier("camera_preview_frame_layout","id", getActivity().getApplication().getPackageName());
		FrameLayout frame = (FrameLayout) rootView.findViewById(cameraPreviewFrameLayoutId);
		_scannerView = new ConvZBarScannerView(getActivity());
		frame.addView(_scannerView);

		// Get ids
		int idUp = getResources().getIdentifier("text_view_id_up","id", getActivity().getApplication().getPackageName());
		int idDown = getResources().getIdentifier("text_view_id_down","id", getActivity().getApplication().getPackageName());
		int idkbButton = getResources().getIdentifier("kbButton","id", getActivity().getApplication().getPackageName());
		int idscanButton = getResources().getIdentifier("scanButton","id", getActivity().getApplication().getPackageName());
		int idImg = getResources().getIdentifier("imageView1","id", getActivity().getApplication().getPackageName());


		// Get barcodeOpts
		this.barecodeOpts = ((ConvScannerActivity)inflater.getContext()).barecodeOpts;
		// Set barecode Formats
		if(this.barecodeOpts.getRestrict()){
			List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
			formats.add(BarcodeFormat.EAN13);
			formats.add(BarcodeFormat.CODE128);
			formats.add(BarcodeFormat.QRCODE);
			_scannerView.setFormats(formats);
		}
		else{
			_scannerView.setFormats(BarcodeFormat.ALL_FORMATS);
		}
	    
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
	    _cameraState = true;
	    return rootView;
	}

	public void pausePreview()
	{
		if(_cameraState)
		{
		    _scannerView.stopCamera();
		    _cameraState = false;
		}
	}

	public void resumePreview()
	{		
		if(!_cameraState)
		{
			_scannerView.startCamera();
		    _cameraState = true;
		}
	}
	
	@Override
	public void onCreate(Bundle state) {
	    super.onCreate(state);
	    setHasOptionsMenu(false);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    _scannerView.setResultHandler(this);
	    _scannerView.startCamera();
	    _scannerView.setFlash(this.barecodeOpts.getFlash());
	    _scannerView.setAutoFocus(true);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	}
	
	@Override
	public void handleResult(Result rawResult) {
		((ConvScannerActivity)	getActivity()).finish(rawResult.getContents(), rawResult.getBarcodeFormat().getName());
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    _scannerView.stopCamera();
	    
	}


}
