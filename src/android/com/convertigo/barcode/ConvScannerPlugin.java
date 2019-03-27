package com.convertigo.barcode;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.convertigo.*;
import com.convertigo.barcode.BarecodeOptions;

public class ConvScannerPlugin extends CordovaPlugin {

	private static int SCAN_REQUEST_CODE = 555;
	private static int QUICKSCAN_REQUEST_CODE = 666;
	private static int SETTINGSSCAN_REQUEST_CODE = 777;
	private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

	private CordovaPlugin	_this;
	private CallbackContext _cordovaCallbackContext;

	public ConvScannerPlugin() {}

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		Log.i("convertigo","Init Plugin!");
		this._this = this;
	}

	
    public void requestPerm(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this,"Please add permission for camera", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            this.start();
        }
    }
	
	public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		this._cordovaCallbackContext = callbackContext;
        if(action.equals("Scan"))
        {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launchQuickScan();
                }
            });
        }
		
		return true;
	}
		
	public void start(){
		BarecodeOptions barecodeOpts = new BarecodeOptions();
        barecodeOpts.setLaserColor("#ff0000");
        barecodeOpts.setLaserEnabled(true);
        barecodeOpts.setMaskColor("#eeeeee");
        barecodeOpts.setMaskOpacity((float)0.5);
        barecodeOpts.setTextDown("My text down");
        barecodeOpts.setTextUp("My text uo");
        barecodeOpts.setSquaredEnabled(false);
        barecodeOpts.setBorderColor("#ff0000");
        barecodeOpts.setTextUpColor("#000000");
        barecodeOpts.setTextDownColor("#000000");
		Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, ConvScannerActivity.class);
        intent.putExtra("options",barecodeOpts);
		cordova.startActivityForResult((CordovaPlugin) _this,	intent, QUICKSCAN_REQUEST_CODE);
	}
	public void launchQuickScan()
	{
        this.requestPerm();
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if(_cordovaCallbackContext != null)
		{
			if(requestCode != SETTINGSSCAN_REQUEST_CODE){
				try{
					JSONObject result = new JSONObject();
					result.put("returnCode", resultCode);
					if(resultCode != android.app.Activity.RESULT_CANCELED)
					{				
						String scanResultType = intent.getStringExtra("SCAN_RESULT_TYPE");
						String scanResult = intent.getStringExtra("SCAN_RESULT");
						result.put("type", scanResultType);
						result.put("result", scanResult);
					}
					_cordovaCallbackContext.success(result);
				} catch (JSONException e){
					_cordovaCallbackContext.error("JSON Error @ Java side!");
				}	
			} else {
				try{
					JSONObject result = new JSONObject();
					result.put("returnCode", resultCode);
					result.put("armis_host", intent.getStringExtra("ARMIS_HOST"));
					result.put("armis_port", intent.getStringExtra("ARMIS_PORT"));
					_cordovaCallbackContext.success(result);
				} catch (JSONException e){
					_cordovaCallbackContext.error("JSON Error @ Java side!");
				}	
			}
		}
	}

	@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.start(v);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}