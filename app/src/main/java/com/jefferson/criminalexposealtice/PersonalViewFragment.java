package com.jefferson.criminalexposealtice;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalViewFragment extends Fragment {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    Button sett, logOut;
    public PersonalViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();

        View view= inflater.inflate(R.layout.fragment_personal_view, container, false);
        sett = view.findViewById(R.id.settins);
        logOut = view.findViewById(R.id.log_out);
        sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UserSettingActivity.class));
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });
        return view;
    }






}
