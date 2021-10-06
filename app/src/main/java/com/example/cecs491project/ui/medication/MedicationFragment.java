package com.example.cecs491project.ui.medication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cecs491project.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MedicationFragment extends Fragment implements MedicationAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    MedicationAdapter medicationAdapter;
    FirebaseFirestore db;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medication, container, false);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener(){
        db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query query = db.collection("User Database")
                .document(uid).collection("Medication");

        FirestoreRecyclerOptions<Medications> options = new FirestoreRecyclerOptions.Builder<Medications>()
                .setQuery(query,Medications.class)
                .build();

        medicationAdapter = new MedicationAdapter(options);

        recyclerView = view.findViewById(R.id.med_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(medicationAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                medicationAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        medicationAdapter.setOnItemClickListener(new MedicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                String docID = documentSnapshot.getId();
                Map<String, Object> s = documentSnapshot.getData();

                String name = (String) s.get("medicationName");
                int pillCount = Math.toIntExact(Long.parseLong(String.valueOf(s.get("pillCount"))));
                int refillCount = Math.toIntExact(Long.parseLong(String.valueOf(s.get("refillCount"))));
                double dosage = (double) (s.get("dosage"));
                String note = (String) s.get("note");
                String cate = (String) s.get("categories");

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("docID", docID);
                bundle.putString("note", note);
                bundle.putString("cate", cate);
                bundle.putInt("count", pillCount);
                bundle.putInt("refill", refillCount);
                bundle.putDouble("dos", dosage);

                Intent i = new Intent(getContext(), MedicationDetails.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        medicationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        medicationAdapter.stopListening();
    }

    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(getContext(), AddMedication.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {

    }
}