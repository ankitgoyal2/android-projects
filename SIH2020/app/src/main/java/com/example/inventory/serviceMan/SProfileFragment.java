package com.example.inventory.serviceMan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inventory.MainActivity;
import com.example.inventory.R;
import com.example.inventory.SettingActivity;
import com.example.inventory.model.CustomDialogBox;
import com.example.inventory.model.ServiceMan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class SProfileFragment extends Fragment {


    ImageView profilePicChange, profilePic;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser user;
    UploadTask uploadTask;
    CustomDialogBox dialogBox;

    TextView name,email,phoneNumber;


    private Toolbar mTopToolbar;
    public SProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sprofile, container, false);
        mTopToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mTopToolbar);
        setHasOptionsMenu(true);
        profilePicChange = view.findViewById(R.id.s_change_profile);
        profilePic = view.findViewById(R.id.profilepic);

        dialogBox = new CustomDialogBox(getActivity());
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.show();

        user = FirebaseAuth.getInstance().getCurrentUser();

        name = view.findViewById(R.id.sm_profile_name);
        email = view.findViewById(R.id.sm_profile_email);
        phoneNumber = view.findViewById(R.id.sm_profile_phone);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("ServiceMan")
                .child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ServiceMan serviceMan = dataSnapshot.getValue(ServiceMan.class);
                Picasso.get().load(serviceMan.getImageURL()).into(profilePic);
                name.setText(serviceMan.getUserName());
                email.setText(serviceMan.getEmail());

                dialogBox.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid()+".jpg");

        profilePicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Activity activity = getActivity();
//                if (activity != null)
                startActivityForResult(i, 12);
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent= new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("PRofile", "helo");
        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("PRofile", "helo1");

            dialogBox.show();

            Uri imageUri = data.getData();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            profilePic.setDrawingCacheEnabled(true);
            profilePic.buildDrawingCache();

            Picasso.get().load(imageUri).transform(new CircleTransform()).into(profilePic, new Callback() {
                @Override
                public void onSuccess() {

                    ((BitmapDrawable) profilePic.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);

                    Log.i("hello ankit", "ankit");
                    final byte[] image_data = baos.toByteArray();
                    uploadTask = storageReference.putBytes(image_data);

                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogBox.dismiss();
                            uploadTask.cancel();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageLink = uri.toString();

                                    HashMap<String, Object> updateProfilePic = new HashMap<>();

                                    updateProfilePic.put("/Users/ServiceMan/" + user.getUid() + "/imageURL", imageLink);

                                    FirebaseDatabase.getInstance().getReference().updateChildren(updateProfilePic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogBox.dismiss();
                                        }
                                    });


                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }




}

