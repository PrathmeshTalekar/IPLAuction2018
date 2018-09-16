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

    public static final int RC_SIGN_IN = 1;
    DatabaseReference reference, jreference;
    int x;
    //    Button create;
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
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        intent = new Intent(LobbyActivity.this, MainActivity.class);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in
                    Toast.makeText(LobbyActivity.this, "You're now signed in", Toast.LENGTH_SHORT).show();
                } else {
                    //user signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEmail = jEmail.getText().toString();
                joinPin = Integer.parseInt(jPin.getText().toString());
                Toast.makeText(LobbyActivity.this, "Joining", Toast.LENGTH_SHORT).show();
                joinLobby("techrace2k18gmailcom", 123);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPin = Integer.parseInt(cPin.getText().toString());
                Toast.makeText(LobbyActivity.this, "Creating", Toast.LENGTH_SHORT).show();
                createLobby(createPin);
            }
        });
    }


    void joinLobby(String email, final int pin) {
        email = email.replaceAll("\\.", "");
        email = email.replaceAll("@", "");
        final String path = "lobby/" + email + "/";
        Log.i("path", path);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(path);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                x = dataSnapshot.child("pin").getValue(Integer.class);
                if (x == pin) {
                    display_name = dName.getText().toString();
                    jreference = FirebaseDatabase.getInstance().getReference(path);
                    jreference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(display_name, "" + 99999999, "" + 0, true))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LobbyActivity.this, "All transaction Complete", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LobbyActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
//                    LobbyActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            startActivity(intent);
//
//                        }
//                    });
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
        String path = "lobby/" + email + "/";

        reference = FirebaseDatabase.getInstance().getReference(path);

        reference.child("pin").setValue(pin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                display_name = dName.getText().toString();
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(display_name, "" + 99999999, "" + 0, true));
                reference.child("player1").setValue("" + 5200000);
                reference.child("player2").setValue("" + 4000000);
                reference.child("player3").setValue("" + 9000000);
                reference.child("player4").setValue("" + 2800000);
                reference.child("player5").setValue("" + 3000000);
                startActivity(intent);
            }
        });
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
        Log.i("reached", "removed xml");

//        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        Log.i("reached", "removed xml");

    }
}
