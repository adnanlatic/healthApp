package com.adnan.healthAppMaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton editProfileButton, sendEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private TextView imeprezimePacijenta, status, izmjerenEKG, izmjerenaTemperature, ekgPrikaziSve, temperaturePrikaziSve, alertMessage;
    private ImageView slikaPacijenta;
    private Profil profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // DB
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Info
        editProfileButton = findViewById(R.id.idAddFAB);
        sendEmail = findViewById(R.id.idSendEmail);
        imeprezimePacijenta = findViewById(R.id.idPatientName);
        slikaPacijenta = findViewById(R.id.idProfilePicture);
        status = findViewById(R.id.idPatientStatus);
        izmjerenEKG = findViewById(R.id.idVrijednostMjerenjaEKG);
        izmjerenaTemperature = findViewById(R.id.idVrijednostMjerenjaTemp);
        ekgPrikaziSve = findViewById(R.id.idPrikaziSveEKG);
        temperaturePrikaziSve = findViewById(R.id.idPrikaziSveTemp);
        alertMessage = findViewById(R.id.idStatusMessage);
        loadingPB = findViewById(R.id.idPBLoading);
        // Uredi profil
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(MainActivity.this, EditProfileActivity.class);
                // on below line we are passing our course modal
                i.putExtra("profil", profil);
                startActivity(i);
            }
        });

        sendEmail.setOnClickListener(view -> {
            // define Intent object with action attribute as ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);

            // add three fields to intent using putExtra function
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"adnanlatic@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Pracenje pacijenta");
            intent.putExtra(Intent.EXTRA_TEXT,
                    "Izvjestaj za pacijenta:" + imeprezimePacijenta.getText() + "\n" +
                        "Trenutna vrijednost EKG:" + izmjerenEKG.getText() + "\n" +
                            "Trenutna vrijednost tjelesne temperature:" + izmjerenaTemperature.getText() + "\n" +
                        "Upozorenje:"+ alertMessage.getText()
            );

            // set type of intent
            intent.setType("message/rfc822");

            // startActivity with intent with chooser as Email client using createChooser function
            startActivity(Intent.createChooser(intent, "Odaberite email klijenta :"));
        });

        // Prikazi sve EKG
        ekgPrikaziSve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(MainActivity.this, EcgActivity.class);
                startActivity(i);
            }
        });
        //Prikazi sve Temperatura
        temperaturePrikaziSve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(i);
            }
        });
        // Senzori
        getProfil();
        getEcgSensorData();
        getTemperatureSensorData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getEcgSensorData() {
        databaseReference = firebaseDatabase.getReference("sensors/ecg");
        ArrayList<Object> ecgSenzor = new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                ecgSenzor.add(data.getValue());
                if (Float.parseFloat(data.getValue()) < 0) {
                    alertMessage.setText("EKG je ispod normalne vrijednosti!");
                } else if(Float.parseFloat(data.getValue()) > 1200) {
                    alertMessage.setText("EKG je iznad normalne vrijednosti!");
                }
                izmjerenEKG.setText(ecgSenzor.get(ecgSenzor.size()-1)+ "bpm");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                ecgSenzor.add(data.getValue());
                if (Float.parseFloat(data.getValue()) < 0) {
                    alertMessage.setText("EKG je ispod normalne vrijednosti!");
                } else if(Float.parseFloat(data.getValue()) > 1200) {
                    alertMessage.setText("EKG je iznad normalne vrijednosti!");
                }
                izmjerenEKG.setText(ecgSenzor.get(ecgSenzor.size()-1)+ "bpm");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTemperatureSensorData() {
        databaseReference = firebaseDatabase.getReference("sensors/temperature");
        ArrayList<Object> tempSenzor = new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                tempSenzor.add(data.getValue());
                if (Float.parseFloat(data.getValue()) < 36) {
                    alertMessage.setText("Tjelesna temperatura iznosi "+data.getValue()+"°C. Tjelesna temperatura je ispod normalne vrijednosti!");
                } else if(Float.parseFloat(data.getValue()) > 37) {
                    alertMessage.setText("Tjelesna temperatura iznosi "+data.getValue()+"°C. Tjelesna temperatura je iznad normalne vrijednosti!");
                } else {
                    alertMessage.setText("Nema odstupanja od normalnih vrijednosti!");
                }
                izmjerenaTemperature.setText(tempSenzor.get(tempSenzor.size()-1)+ "°C");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                tempSenzor.add(data.getValue());
                if (Float.parseFloat(data.getValue()) < 36) {
                    alertMessage.setText("Tjelesna temperatura je ispod normalne vrijednosti!");
                } else if(Float.parseFloat(data.getValue()) > 37) {
                    alertMessage.setText("Tjelesna temperatura je iznad normalne vrijednosti!");
                } else {
                    alertMessage.setText("Nema odstupanja od normalnih vrijednosti!");
                }
                izmjerenaTemperature.setText(tempSenzor.get(tempSenzor.size()-1)+ "°C");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProfil() {
        databaseReference = firebaseDatabase.getReference("profil/");
       databaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               profil = snapshot.getValue(Profil.class);
               Calendar calendar = Calendar.getInstance();
               int year = calendar.get(Calendar.YEAR);
               imeprezimePacijenta.setText(profil.getImeprezime() + '(' + (year - Integer.parseInt(profil.getGodinaRodjenja())) + ')');
               Picasso.get()
                       .load(profil.getSlika())
                       .resize(150, 150).into(slikaPacijenta);
               loadingPB.setVisibility(View.GONE);
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Profil profil = snapshot.getValue(Profil.class);
               Calendar calendar = Calendar.getInstance();
               int year = calendar.get(Calendar.YEAR);
               imeprezimePacijenta.setText(profil.getImeprezime() + '(' + (year - Integer.parseInt(profil.getGodinaRodjenja())) + ')');
               loadingPB.setVisibility(View.GONE);
           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                Toast.makeText(this, "Korisnik uspjesno odjavljen...", Toast.LENGTH_SHORT).show();
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}