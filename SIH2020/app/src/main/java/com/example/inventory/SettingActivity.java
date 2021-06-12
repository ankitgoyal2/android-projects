package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventory.serviceMan.SFaqActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    LinearLayout linearLayout_notification;
    LinearLayout linearLayout_fingerprint;
    LinearLayout linearLayout_feedback;
    LinearLayout linearLayout_faq;
    LinearLayout linearLayout_terms_and_conditions;
    LinearLayout linearLayout_about_our_app;

    Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        linearLayout_notification=findViewById(R.id.setting_notification);
        linearLayout_fingerprint=findViewById(R.id.setting_fingerprint);
        linearLayout_feedback=findViewById(R.id.setting_feedback);
        linearLayout_faq=findViewById(R.id.setting_faq);
        linearLayout_terms_and_conditions=findViewById(R.id.setting_terms_and_condition);
        linearLayout_about_our_app=findViewById(R.id.setting_about_our_app);

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        linearLayout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearLayout_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearLayout_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(intent);
                finish();

            }
        });

        linearLayout_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pass intent to serviceman and responsibleman saperately
                Intent intent=new Intent(getApplicationContext(), SFaqActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayout_terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearLayout_about_our_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
