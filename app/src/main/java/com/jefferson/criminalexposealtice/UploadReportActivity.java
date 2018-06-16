package com.jefferson.criminalexposealtice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class UploadReportActivity extends AppCompatActivity {

    public static final int MAP_INTENT = 1;

    EditText description, mLocation;
    Uri uri;
    Button upload;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myReportRef = database.getReference("Reports");

    private StorageReference mStorageRef;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        Geocoder geocoder = new Geocoder(UploadReportActivity.this, Locale.getDefault());
                                        try {
//                                    mUbicacion = location;
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Address obj = addresses.get(0);
                                            mLocation.setText(obj.getSubLocality() + ", " + obj.getLocality() + ", " + obj.getCountryName());
                                            Log.i("Androidddd ", mLocation.toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(UploadReportActivity.this,"Como se han negado los permisos, se requiere poner la ubicacion manualmente", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);


        Intent intent = getIntent();
        String extra = intent.getStringExtra("ImageUri");
        uri = Uri.parse(extra);
        upload = findViewById(R.id.btn_upload);
        description = findViewById(R.id.description_add);
        mLocation = findViewById(R.id.location_add);

        String path = "Reports/" +  UUID.randomUUID() + ".png";

        mStorageRef = FirebaseStorage.getInstance().getReference(path);

        progressDialog = new ProgressDialog(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission( UploadReportActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(UploadReportActivity.this, Locale.getDefault());
                                try {
//                                    mUbicacion = location;
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Address obj = addresses.get(0);
                                    mLocation.setText(obj.getSubLocality()+", "+obj.getLocality()+", "+obj.getCountryName());
                                    Log.i("Androidddd ",mLocation.toString());
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
        else
        {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                       android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            3);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }




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


        String locationText = String.valueOf(mLocation.getText());
        String descr = String.valueOf(description.getText());

        CrimeReport crimeReport = new CrimeReport(locationText,uri.toString(),descr);
        myReportRef.push().setValue(crimeReport);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
