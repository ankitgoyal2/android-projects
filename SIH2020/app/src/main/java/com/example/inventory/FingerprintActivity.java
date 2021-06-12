package com.example.inventory;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.inventory.handler.FingerprintHandler;

public class FingerprintActivity extends AppCompatActivity {

    private ImageView mFingerPrintImage;
    private TextView mHeadingLabel;
    private TextView mParaLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        mFingerPrintImage = (ImageView)findViewById(R.id.f_icon);
        mHeadingLabel = (TextView)findViewById(R.id.fingerprint_text);
        mParaLabel = findViewById(R.id.t_t1);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

            if(!fingerprintManager.isHardwareDetected())
            {
                mParaLabel.setText("FingerPrint Scanner not detected in device");
            }
            else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            {
                mParaLabel.setText("Permission not granted to use fingerprint scanner");
            }
            else if(!keyguardManager.isKeyguardSecure())
            {
                mParaLabel.setText("Add Lock to your Phone");
            }
            else if(!fingerprintManager.hasEnrolledFingerprints())
            {
                mParaLabel.setText("You should add atleast 1 fingerprint to use this feature");
            }
            else
            {
                mParaLabel.setText("Place a finger on the scanner to access");
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, null);

//                Intent i = new Intent(this, HelloActivity.class);
//                startActivity(i);

            }


            }
        }
}
