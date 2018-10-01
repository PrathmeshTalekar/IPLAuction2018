package com.spit.iplauction2018.iplauction2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;int x;
    @BindView(R.id.lobby)
    Button create;
    @BindView(R.id.join)
    Button join;
    @BindView(R.id.join_email)
    EditText jEmail;
    @BindView(R.id.join_pin)
    EditText jPin;
    @BindView(R.id.create_pin)
    EditText cPin;
    @BindView(R.id.display_name)
    EditText dName;
    String display_name;
    String joinEmail;
    int joinPin, createPin;
    Intent intent;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,reference, jreference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        intent = new Intent(LobbyActivity.this, MainActivity.class);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LobbyActivity.this, "You're now signed in", Toast.LENGTH_SHORT).show();
                } else {
                    //user signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.log)
                                    .setTheme(R.style.LoginTheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEmail = jEmail.getText().toString();
                if(joinEmail.length()==0&&jPin.getText().toString().length()==0){
                    Toast.makeText(LobbyActivity.this, "Enter Host Email and Lobby Pin", Toast.LENGTH_SHORT).show();
                }else if(joinEmail.length()==0){
                    Toast.makeText(LobbyActivity.this, "Enter Host Email", Toast.LENGTH_SHORT).show();
                }else if(jPin.getText().toString().length()==0){
                    Toast.makeText(LobbyActivity.this, "Enter Lobby Pin", Toast.LENGTH_SHORT).show();
                }else {
                    joinPin = Integer.parseInt(jPin.getText().toString());
                    joinLobby(joinEmail, joinPin);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cPin.getText().toString().length()==0){
                    Toast.makeText(LobbyActivity.this, "Enter Lobby Pin", Toast.LENGTH_SHORT).show();
                }else{
                    createPin = Integer.parseInt(cPin.getText().toString());
                    createLobby(createPin);
                }
            }
        });
    }


    void joinLobby(String email, final int pin) {
        email = email.replaceAll("\\.", "");
        email = email.replaceAll("@", "");
        path = "lobby/" + email + "/";
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(path);

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        x = dataSnapshot.child("pin").getValue(Integer.class);
                        if (x != pin) {
                            Toast.makeText(LobbyActivity.this, "Incorrect Pin", Toast.LENGTH_SHORT).show();
                        } else if (x == pin) {
                            display_name = dName.getText().toString();
                            if (display_name.length() == 0) {
                                Toast.makeText(LobbyActivity.this, "Enter Display Name", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LobbyActivity.this, "Joining", Toast.LENGTH_SHORT).show();
                                jreference = FirebaseDatabase.getInstance().getReference(path);
                                jreference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(display_name, "" + 99999999, "" + 0))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                intent.putExtra("lobby_path", path);
                                                startActivity(new Intent(LobbyActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        });
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(LobbyActivity.this,"Invalid Host Email",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

    }

    void createLobby(int pin) {

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email = email.replaceAll("\\.", "");
        email = email.replaceAll("@", "");
        path = "lobby/" + email + "/";
        display_name = dName.getText().toString();
        if (display_name.length() == 0) {
            Toast.makeText(LobbyActivity.this, "Enter Display Name", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LobbyActivity.this, "Creating", Toast.LENGTH_SHORT).show();
            reference = FirebaseDatabase.getInstance().getReference(path);
            reference.child("pin").setValue(pin).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(display_name, "" + 99999999, "" + 0));
                    reference.child("player1").setValue("" + 5200000);
                    reference.child("player2").setValue("" + 4000000);
                    reference.child("player3").setValue("" + 9000000);
                    reference.child("player4").setValue("" + 2800000);
                    reference.child("player5").setValue("" + 3000000);
                    reference.child("player6").setValue("" + 2000000);
                    reference.child("player7").setValue("" + 3000000);
                    reference.child("player8").setValue("" + 1800000);
                    reference.child("player9").setValue("" + 7800000);
                    reference.child("player10").setValue("" + 11000000);
                    reference.child("player11").setValue("" + 7600000);
                    reference.child("player12").setValue("" + 9400000);
                    reference.child("player13").setValue("" + 2000000);
                    reference.child("player14").setValue("" + 4200000);
                    reference.child("player15").setValue("" + 3000000);
                    reference.child("player16").setValue("" + 4200000);
                    reference.child("player17").setValue("" + 6000000);
                    reference.child("player18").setValue("" + 9000000);
                    reference.child("player19").setValue("" + 6200000);
                    reference.child("player20").setValue("" + 11500000);
                    reference.child("player21").setValue("" + 5400000);
                    reference.child("player22").setValue("" + 12500000);
                    reference.child("player23").setValue("" + 6400000);
                    reference.child("player24").setValue("" + 4000000);
                    reference.child("player25").setValue("" + 7400000);
                    reference.child("player26").setValue("" + 6200000);
                    reference.child("player27").setValue("" + 7400000);
                    reference.child("player28").setValue("" + 7400000);
                    reference.child("player29").setValue("" + 8000000);
                    reference.child("player30").setValue("" + 4400000);
                    intent.putExtra("lobby_path", path);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lobby_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "You're Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
