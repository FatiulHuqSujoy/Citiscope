package com.example.sujoy.citi1.UI;

import android.app.Activity;

/**
 * Created by Sujoy on 4/28/2017.
 */

public abstract class ConfirmationActivity extends Activity {

    public abstract void cancelConfCode();

    public abstract void resendConfCode();

    public abstract void validateConfCode(String s);
}
