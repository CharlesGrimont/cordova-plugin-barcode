package com.convertigo.barcode;

import android.graphics.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;

public class BarecodeOptions implements Serializable {
    private boolean laserEnabled;
    private int laserColor;
    private boolean squaredEnabled;
    private int maskColor;
    private int borderColor;
    private float maskOpacity;
    private String textUp = "Text Up";
    private String textDown = "Text Down";
    private String imgUri;
    private Boolean flash = false;
    private int textDownColor;
    private int textUpColor;
    private Boolean onlyScan = false;
    private Boolean onlyKeyboard = false;
    private String imgPath;
    private boolean restrict;

    public Boolean getRestrict() {
        return restrict;
    }

    public void SetRestrict(Boolean restrict) {
        this.restrict = restrict;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Boolean getOnlyScan() {
        return onlyScan;
    }

    public void setOnlyScan(Boolean onlyScan) {
        this.onlyScan = onlyScan;
    }

    public Boolean getOnlyKeyboard() {
        return onlyKeyboard;
    }

    public void setOnlyKeyboard(Boolean onlyKeyboard) {
        this.onlyKeyboard = onlyKeyboard;
    }

    public int getTextUpColor() {
        return textUpColor;
    }

    public void setTextUpColor(String textUpColor) {
        int color = Color.parseColor(textUpColor);
        this.textUpColor = color;
    }
    public int getTextDownColor() {
        return textDownColor;
    }

    public void setTextDownColor(String textDownColor) {
        int color = Color.parseColor(textDownColor);
        this.textDownColor = color;
    }


    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        int color = Color.parseColor(borderColor);
        this.borderColor = color;
    }

    public Boolean getFlash() {
        return flash;
    }

    public void setFlash(Boolean flash) {
        this.flash = flash;
    }
    public String getImgUri() {
        return this.imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public boolean isLaserEnabled() {
        return laserEnabled;
    }

    public void setLaserEnabled(boolean laserEnabled) {
        this.laserEnabled = laserEnabled;
    }

    public int getLaserColor() {
        return laserColor;
    }

    public void setLaserColor(String laserColor) {
        int color = Color.parseColor(laserColor);
        this.laserColor = color;
    }

    public boolean isSquaredEnabled() {
        return squaredEnabled;
    }

    public void setSquaredEnabled(boolean squaredEnabled) {
        this.squaredEnabled = squaredEnabled;
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(String maskColor) {
        int color = Color.parseColor(maskColor);
        this.maskColor = color;
    }

    public float getMaskOpacity() {
        return maskOpacity;
    }

    public void setMaskOpacity(float maskOpacity) {
        this.maskOpacity = maskOpacity;
    }

    public String getTextUp() {
        return textUp;
    }

    public void setTextUp(String textUp) {
        this.textUp = textUp;
    }

    public String getTextDown() {
        return textDown;
    }

    public void setTextDown(String textDown) {
        this.textDown = textDown;
    }


}