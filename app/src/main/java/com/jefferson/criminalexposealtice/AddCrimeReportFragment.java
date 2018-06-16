package com.jefferson.criminalexposealtice;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.hardware.Camera;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCrimeReportFragment extends Fragment {

    public static final int GALLERY_INTENT =1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    Button next,change;
    ImageView imageView;
    Uri uri;
    ImageButton imageButton;
    private Bitmap Image;

    public AddCrimeReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_add_crime_report, container, false);
         next = view.findViewById(R.id.next_to_upload);
         change = view.findViewById(R.id.change_photo);
         imageView = view.findViewById(R.id.image_to_upload);
         imageButton = view.findViewById(R.id.btn_camera);

         imageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dispatchTakePictureIntent();
             }
         });

         Intent intent = new Intent(Intent.ACTION_PICK);
         intent.setType("image/*");
         startActivityForResult(intent,GALLERY_INTENT);

         change.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 startActivityForResult(intent,GALLERY_INTENT);

             }
         });


         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), UploadReportActivity.class);
                 intent.putExtra("ImageUri",uri.toString());
                 Log.i("TAC Extraaaaaaaaaaaaaaa",uri.toString() ) ;

                 startActivity(intent);
             }
         });
         return view;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            imageView.setImageURI(uri);
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uri = data.getData();
            imageView.setImageURI(uri);
        }
    }
}
