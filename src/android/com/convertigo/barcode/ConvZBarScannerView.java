package com.convertigo.barcode;

import android.content.Context;
import android.hardware.Camera;

import net.sourceforge.zbar.ImageScanner;
import java.util.List;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.nio.charset.StandardCharsets;

public class ConvZBarScannerView extends ZBarScannerView {
    private static String TAG = "ConvZBarScannerView";
    private ImageScanner mScanner;
    private List<BarcodeFormat> mFormats;
    private ResultHandler mResultHandler;
    private BarecodeOptions barecodeOpts;

    public ConvZBarScannerView(Context context) {
        super(context);
    }

    public ConvZBarScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);

        mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
        for(BarcodeFormat format : getFormats()) {
            mScanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mResultHandler == null) {
            return;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            int width = size.width;
            int height = size.height;

            if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                int rotationCount = getRotationCount();
                if (rotationCount == 1 || rotationCount == 3) {
                    int tmp = width;
                    width = height;
                    height = tmp;
                }
                data = getRotatedData(data, camera);
            }

            Rect rect = getFramingRectInPreview(width, height);
            Image barcode = new Image(width, height, "Y800");
            barcode.setData(data);
            barcode.setCrop(rect.left, rect.top, rect.width(), rect.height());

            int result = mScanner.scanImage(barcode);

            if (result != 0) {
                SymbolSet syms = mScanner.getResults();
                final Result rawResult = new Result();
                for (Symbol sym : syms) {
                    // In order to retreive QR codes containing null bytes we need to
                    // use getDataBytes() rather than getData() which uses C strings.
                    // Weirdly ZBar transforms all data to UTF-8, even the data returned
                    // by getDataBytes() so we have to decode it as UTF-8.
                    String symData;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        symData = new String(sym.getDataBytes(), StandardCharsets.UTF_8);
                    } else {
                        symData = sym.getData();
                    }
                    if (!TextUtils.isEmpty(symData)) {
                        rawResult.setContents(symData);
                        rawResult.setBarcodeFormat(BarcodeFormat.getFormatById(sym.getType()));
                        break;
                    }
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Stopping the preview can take a little long.
                        // So we want to set result handler to null to discard subsequent calls to
                        // onPreviewFrame.
                        ResultHandler tmpResultHandler = mResultHandler;
                        mResultHandler = null;

                        stopCameraPreview();
                        if (tmpResultHandler != null) {
                            tmpResultHandler.handleResult(rawResult);
                        }
                    }
                });
            } else {
                camera.setOneShotPreviewCallback(this);
            }
        } catch(RuntimeException e) {
            // TODO: Terrible hack. It is possible that this method is invoked after camera is released.
            Log.e(TAG, e.toString(), e);
        }
    }

    public IViewFinder createViewFinderView(Context context) {
        // Get view finder
        ViewFinderView viewFinderView = new ViewFinderView(context);

        //Get barecodeOpts
        BarecodeOptions barecodeOpts = ((ConvScannerActivity)context).barecodeOpts;

        // Set laser enabled
        viewFinderView.setLaserEnabled(barecodeOpts.isLaserEnabled());

        // Set laser color
        viewFinderView.setLaserColor(barecodeOpts.getLaserColor());

        // Set square enabled
        viewFinderView.setSquareViewFinder(barecodeOpts.isSquaredEnabled());

        // Set mask color
        viewFinderView.setMaskColor(barecodeOpts.getMaskColor());

        //Set mask opacity
        viewFinderView.setAlpha(barecodeOpts.getMaskOpacity());

        // Set border color
        viewFinderView.setBorderColor(barecodeOpts.getBorderColor());

        return viewFinderView;
    }
    public void resumeCameraPreview(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
        super.resumeCameraPreview();
    }

    public void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }
}
