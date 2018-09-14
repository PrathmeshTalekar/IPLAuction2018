package com.spit.iplauction2018.iplauction2018;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //@BindView(R.id.button_100)Button button_100;
    //@BindView(R.id.bid_price)TextView bid_price;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    Player player;
    TextView bid_price;
    TextView name;
    TextView base_price;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bid_price=(TextView)getActivity().findViewById(R.id.bid_price);
        name=(TextView)getActivity().findViewById(R.id.name_text_view);
        base_price=(TextView)getActivity().findViewById(R.id.price_text_view);

        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference("players/player"+Integer.toString(1)+"/");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                player=dataSnapshot.getValue(Player.class);
                name.setText(player.getName());
                base_price.setText(player.getPrice());
                bid_price.setText(player.getBidprice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button button_100=(Button)getActivity().findViewById(R.id.button_100);
        button_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+100);
                player.setBidprice(newPrice);
                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bid_price.setText(""+(newPrice));
                    }
                });
            }
        });
        Button button_500=(Button)getActivity().findViewById(R.id.button_500);
        button_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+500);
                player.setBidprice(newPrice);
                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bid_price.setText(""+(newPrice));
                    }
                });
            }
        });
        Button button_1000=(Button)getActivity().findViewById(R.id.button_1000);
        button_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+1000);
                player.setBidprice(newPrice);
                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bid_price.setText(""+(newPrice));
                    }
                });
            }
        });
    }
}
