package com.adnan.healthAppMaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText imePrezimeEdit, godinaRodjenjaEdit, slikaEdit, spolEdit, tezinaEdit, krvnaGrupaEdit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Profil profil;
    private ProgressBar loadingPB;
    // creating a string for our course id.
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button editProfileBtn = findViewById(R.id.idBtnUpdateProfile);
        imePrezimeEdit = findViewById(R.id.idEditImePrezime);
        godinaRodjenjaEdit = findViewById(R.id.idEditGodinaRodjenja);
        slikaEdit = findViewById(R.id.idEditSlika);
        spolEdit = findViewById(R.id.idEditSpol);
        tezinaEdit = findViewById(R.id.idEditTezina);
        krvnaGrupaEdit = findViewById(R.id.idEditKrvnaGrupa);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line we are getting our modal class on which we have passed.
        profil = getIntent().getParcelableExtra("profil");
        System.out.println(profil);

        if (profil != null) {
            // on below line we are setting data to our edit text from our modal class.
            imePrezimeEdit.setText(profil.getImeprezime());
            godinaRodjenjaEdit.setText(profil.getGodinaRodjenja());
            slikaEdit.setText(profil.getSlika());
            spolEdit.setText(profil.getSpol());
            tezinaEdit.setText(profil.getTezina());
            krvnaGrupaEdit.setText(profil.getKrvnaGrupa());
            userID = "user";
        }

        // on below line we are initialing our database reference and we are adding a child as our course id.
        databaseReference = firebaseDatabase.getReference("profil/user");
        // on below line we are adding click listener for our add course button.
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are making our progress bar as visible.
                loadingPB.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String imePrezime = imePrezimeEdit.getText().toString();
                String godinaRodjenja = godinaRodjenjaEdit.getText().toString();
                String krvnaGrupa = krvnaGrupaEdit.getText().toString();
                String slika = slikaEdit.getText().toString();
                String spol = spolEdit.getText().toString();
                String tezina = tezinaEdit.getText().toString();
                // on below line we are creating a map for
                // passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("imeprezime", imePrezime);
                map.put("godinaRodjenja", godinaRodjenja);
                map.put("krvnaGrupa", krvnaGrupa);
                map.put("slika", slika);
                map.put("spol", spol);
                map.put("tezina", tezina);
                map.put("userID", userID);

                // on below line we are calling a database reference on
                // add value event listener and on data change method
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        // adding a map to our database.
                        databaseReference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditProfileActivity.this, "Podaci izmjenjeni..", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on toast.
                        Toast.makeText(EditProfileActivity.this, "Greska prilikom izmjene podataka..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}