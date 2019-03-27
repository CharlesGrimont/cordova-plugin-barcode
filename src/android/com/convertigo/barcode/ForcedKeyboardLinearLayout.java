package com.convertigo.barcode;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class ForcedKeyboardLinearLayout extends LinearLayout{
    private static Activity _activity;

    public ForcedKeyboardLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForcedKeyboardLinearLayout(Context context) {
        super(context);
    }

    public static void setActivity(Activity activity) {
    	_activity = activity;
    }

    /**
     * Overrides the handling of the back key to move back to the 
     * previous sources or dismiss the search dialog, instead of 
     * dismissing the input method.
     */
    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (_activity != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) 
        {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) 
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) 
                {
                    state.startTracking(event, this);
                    return true;
                } 
                else if (event.getAction() == KeyEvent.ACTION_UP&& !event.isCanceled() && state.isTracking(event)) 
                {
                	_activity.onBackPressed();
                    return true;
                }
            }
        }

        return super.dispatchKeyEventPreIme(event);
    }
}
