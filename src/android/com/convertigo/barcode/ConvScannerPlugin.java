package com.convertigo.barcode;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Build;

import com.convertigo.*;
import com.convertigo.barcode.BarecodeOptions;

public class ConvScannerPlugin extends CordovaPlugin {

	private static int SCAN_REQUEST_CODE = 555;
	private static int QUICKSCAN_REQUEST_CODE = 666;
	private static int SETTINGSSCAN_REQUEST_CODE = 777;

	private CordovaPlugin	_this;
	private CallbackContext _cordovaCallbackContext;

	public ConvScannerPlugin() {}

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		Log.i("convertigo","Init Plugin!");
		this._this = this;
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
		
	public void launchQuickScan()
	{
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