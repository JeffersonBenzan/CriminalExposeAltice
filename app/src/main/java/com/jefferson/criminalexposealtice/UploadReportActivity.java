package com.jefferson.criminalexposealtice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UploadReportActivity extends AppCompatActivity {

    public static final int MAP_INTENT =1;

    EditText description, location;
    Uri uri;
    Button upload;
    ProgressDialog progressDialog ;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myReportRef = database.getReference("Reports");

    private StorageReference mStorageRef;

//    @Override
//    protected void onStart() {
//        super.onStart();
////        Intent intent = new Intent(Intent.CATEGORY_APP_MAPS);
////
////        startActivityForResult(intent,MAP_INTENT);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);


        Intent intent = getIntent();
        String extra = intent.getStringExtra("ImageUri");
        uri = Uri.parse(extra);
        upload = findViewById(R.id.btn_upload);
        description = findViewById(R.id.description_add);
        location = findViewById(R.id.location_add);

        String path = "Reports/" +  UUID.randomUUID() + ".png";

        mStorageRef = FirebaseStorage.getInstance().getReference(path);

        progressDialog = new ProgressDialog(this);



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading...");
                progressDialog.setMessage("Uploading to database");
                progressDialog.setCancelable(false);
                progressDialog.show();


                mStorageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri imageFirebaseUri = taskSnapshot.getDownloadUrl();
                        loadReport(imageFirebaseUri);
                        progressDialog.dismiss();
                        Toast.makeText(UploadReportActivity.this,"Image uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadReportActivity.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadReportActivity.this,"Hubo un error \n: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });



    }



    private void loadReport(Uri uri){


        String locationText = String.valueOf(location.getText());
        String descr = String.valueOf(description.getText());

        CrimeReport crimeReport = new CrimeReport(locationText,uri.toString(),descr);
        myReportRef.push().setValue(crimeReport);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
