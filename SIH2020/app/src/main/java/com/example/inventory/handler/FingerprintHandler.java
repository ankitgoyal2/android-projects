package com.example.inventory.handler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.inventory.LoginActivity;
import com.example.inventory.R;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;


    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject)
    {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void  onAuthenticationError(int errorCode, CharSequence errString) {


    }
    @Override
    public void onAuthenticationFailed() {
        this.update("Auth failed",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error "+ helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Done", true);
    }

    private void update(String s, boolean b) {
        TextView paralabel = (TextView)((Activity)context).findViewById(R.id.t_t1);
        ImageView fingerPrintImage = (ImageView)((Activity)context).findViewById(R.id.f_icon);

        paralabel.setText(s);

        if(b==false)
        {
            paralabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else
        {
            paralabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            fingerPrintImage.setImageResource(R.mipmap.done_image_asset);

            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            Activity activity = (Activity)context;
            activity.finish();

        }

    }
}