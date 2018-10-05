package com.spit.iplauction2018.iplauction2018;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    View rootView;
    private Unbinder unbinder;
    String lobby_in;
    int numberOfPlayers;
    Player displayPlayer;
    ArrayList<Player> player = new ArrayList<Player>();
    private DatabaseReference userReference;

    public TeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team, container, false);
        lobby_in = getArguments().getString("lobby");
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userReference = FirebaseDatabase.getInstance().getReference(lobby_in + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/players");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("numberOfPlayers").exists()) {
                    numberOfPlayers = dataSnapshot.child("numberOfPlayers").getValue(Integer.class);
                    for (int i = 1; i <= numberOfPlayers; i++) {
                        displayPlayer = dataSnapshot.child("player" + i).getValue(Player.class);
                        player.add(displayPlayer);
                    }
                    PlayerAdapter adapter = new PlayerAdapter(getContext(), player);
                    ListView listView = (ListView) getActivity().findViewById(R.id.list);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        ArrayList<Player> player = new ArrayList<Player>();
//        player.add(new Player("Shikar Dhawan", "batsman"));
//        player.add(new Player("Virendra Sehwag","batsman"));
//        player.add(new Player("Ajinkya Rahane", "batsman"));
//        player.add(new Player("Sachin Tendulkar","batsman"));
//        player.add(new Player("Ricky Ponting","batsman"));
//        player.add(new Player("MS Dhoni","wicketkeeper"));
//        player.add(new Player("Yuvraj Singh","all rounder"));
//        player.add(new Player("Ravindra Ashwin","bowler"));
//        player.add(new Player("Lasith Malinga","bowler"));
//        player.add(new Player("Ishan Sharma","bowler"));
//        player.add(new Player("Sunil Narine","bowler"));


    }


}
