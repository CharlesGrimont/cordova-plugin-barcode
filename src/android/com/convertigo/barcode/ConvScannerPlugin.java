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
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
	public static final String CAMERA = Manifest.permission.CAMERA;
	private JSONArray _args;

	public ConvScannerPlugin() {}

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		Log.i("convertigo","Init Plugin!");
		this._this = this;
	}
	

    
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                         int[] grantResults) throws JSONException
    {
        for(int r:grantResults)
        {
            if(r == PackageManager.PERMISSION_DENIED)
            {
                _cordovaCallbackContext.error("Permission denied for camera");
                return;
            }
        }
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                this.start();
                break;
        }
    }

	protected void getCameraPermission(int requestCode)
    {
        cordova.requestPermission(this, requestCode, this.CAMERA);
    }
	
	public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this._cordovaCallbackContext = callbackContext;
		this._args = args;
		
        if(action.equals("Scan"))
        {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    
                    if(cordova.hasPermission(CAMERA))
                    {
                        //Exec
                        start();
                    }
                    else
                    {

                        getCameraPermission(MY_PERMISSIONS_REQUEST_CAMERA);
                    }

                }
            });
        }
		
		return true;
	}
		
	public void start(){
        String imgPath;
		String laserColor;
        String maskColor;
        String textDown;
        String textUp;
        String borderColor;
        String textUpColor;
        String textDownColor;
        Boolean laserEnabled;
        Boolean squareEnabled;
        float maskOpacity;
        Boolean modeRestrict;

        BarecodeOptions barecodeOpts = new BarecodeOptions();

        try {
            JSONObject jObj = (JSONObject)this._args.get(0);

            try{
                modeRestrict = jObj.getBoolean("restrict");
                barecodeOpts.SetRestrict(modeRestrict);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter restrict not set or incorrect");
            }

            try{
                imgPath = jObj.getString("imgPath");
                barecodeOpts.setImgPath(imgPath);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter imgPath not set or incorrect");
            }

            try{
                laserColor = jObj.getString("laserColor");
                barecodeOpts.setLaserColor(laserColor);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter laserColor not set or incorrect");
            }

            try{
                laserEnabled = jObj.getBoolean("laserEnabled");
                barecodeOpts.setLaserEnabled(laserEnabled);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter laserEnabled not set or incorrect");
            }

            try{
                maskColor = jObj.getString("maskColor");
                barecodeOpts.setMaskColor(maskColor);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter maskColor not set or incorrect");
            }

            try{
                maskOpacity = (float)jObj.getDouble("maskOpacity");
                barecodeOpts.setMaskOpacity(maskOpacity);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter maskOpacity not set or incorrect");
            }

            try{
                textDown = jObj.getString("textDown");
                barecodeOpts.setTextDown(textDown);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter textDown not set or incorrect");
            }

            try{
                textUp = jObj.getString("textUp");
                barecodeOpts.setTextUp(textUp);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter textUp not set or incorrect");
            }

            try{
                squareEnabled = jObj.getBoolean("squareEnabled");
                barecodeOpts.setSquaredEnabled(squareEnabled);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter squareEnabled not set or incorrect");
            }

            try{
                borderColor = jObj.getString("borderColor");
                barecodeOpts.setBorderColor(borderColor);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter borderColor not set or incorrect");
            }

            try{
                textUpColor = jObj.getString("textUpColor");
                barecodeOpts.setTextUpColor(textUpColor);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter textUpColor not set or incorrect");
            }

            try{
                textDownColor = jObj.getString("textDownColor");
                barecodeOpts.setTextDownColor(textDownColor);
            }
            catch (JSONException e){
                Log.d("ConvBarcode", "Parameter textDownColor not set or incorrect");
            }


        } catch (JSONException e) {
            Log.d("ConvBarCode", "Cannot read parameters we will use default parameters");
            barecodeOpts.setLaserColor("#ff0000");
            barecodeOpts.setLaserEnabled(true);
            barecodeOpts.setMaskColor("#eeeeee");
            barecodeOpts.setMaskOpacity((float)0.5);
            barecodeOpts.setTextDown("My text down");
            barecodeOpts.setTextUp("My text up");
            barecodeOpts.setSquaredEnabled(false);
            barecodeOpts.setBorderColor("#ff0000");
            barecodeOpts.setTextUpColor("#000000");
            barecodeOpts.setTextDownColor("#000000");
        }
		Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, ConvScannerActivity.class);
        intent.putExtra("options",barecodeOpts);
		cordova.startActivityForResult((CordovaPlugin) _this,	intent, QUICKSCAN_REQUEST_CODE);
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
}