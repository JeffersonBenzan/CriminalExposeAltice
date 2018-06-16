package com.jefferson.criminalexposealtice;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeViewFragment extends Fragment {

    private ArrayList<CrimeReport> crimeReports = new ArrayList<>();
    private RecyclerView recyclerView;
    private CrimeReportAdapter mAdapter;
    ImageButton btnShare;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myReportRef = database.getReference("Reports");
    public CrimeViewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_crime_view, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);

        mAdapter = new CrimeReportAdapter(getActivity(), crimeReports);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);


        myReportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                crimeReports.removeAll(crimeReports);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    CrimeReport crimeReport = snapshot.getValue(CrimeReport.class);
                    crimeReports.add(crimeReport);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    class CrimeReportAdapter extends RecyclerView.Adapter<CrimeReportAdapter.MyViewHolder> {
        private Context context;
        private ArrayList<CrimeReport> reportList;

        public CrimeReportAdapter(Context context, ArrayList<CrimeReport> reportList) {
            this.context = context;
            this.reportList = reportList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView location, descripcion;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                location = view.findViewById(R.id.location);
                descripcion = view.findViewById(R.id.description);
                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }




        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.crime_report_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final CrimeReport crimeReport = crimeReports.get(position);
            holder.location.setText(crimeReport.getLocation());
            holder.descripcion.setText(crimeReport.getDescription());

            Glide.with(context)
                    .load(crimeReport.getImage())
                    .into(holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return crimeReports.size();
        }
    }

}
