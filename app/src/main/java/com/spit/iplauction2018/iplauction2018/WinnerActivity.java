package com.spit.iplauction2018.iplauction2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String lobby_in, winnerName, winnerPoints;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.point)
    TextView point;
    @BindView(R.id.exit)
    Button button_exit;
    private DatabaseReference winnerReference;
    private Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("MY_GAME", Context.MODE_PRIVATE);
        lobby_in = sharedPreferences.getString("lobby", null);
        winnerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "winner");
        winnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                winnerName = dataSnapshot.child("name").getValue(String.class);
                winnerPoints = dataSnapshot.child("points").getValue(String.class);
                name.setText(winnerName);
                point.setText(winnerPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
