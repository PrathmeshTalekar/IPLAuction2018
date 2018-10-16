package com.spit.iplauction2018.iplauction2018;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

    DatabaseReference playerReference, userReference;
    SharedPreferences sharedPreferences;
    String lobby_in;
    User user;
    int price;
    int i = 0;

    public PlayerAdapter(Context TeamFragment, ArrayList<Player> player) {
        super(TeamFragment, 0, player);
        sharedPreferences = TeamFragment.getSharedPreferences("MY_GAME", Context.MODE_PRIVATE);
        lobby_in = sharedPreferences.getString("lobby", null);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Player currentPlayer = getItem(position);
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        final FoldingCell fc = (FoldingCell)listItemView.findViewById(R.id.foldingCell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.playername);
        nameTextView.setText(currentPlayer.getName());
        TextView typeTextView = (TextView) listItemView.findViewById(R.id.playertype);
        typeTextView.setText(currentPlayer.getType());
        TextView price = (TextView) listItemView.findViewById(R.id.bid_price);
        price.setText(currentPlayer.getPrice());
        TextView finalPrice = (TextView) listItemView.findViewById(R.id.final_price);
        finalPrice.setText(currentPlayer.getBidprice());
        TextView expandedName = (TextView) listItemView.findViewById(R.id.playernameExpanded);
        expandedName.setText(currentPlayer.getName());
        TextView expandedType = (TextView) listItemView.findViewById(R.id.playertypeExpanded);
        expandedType.setText(currentPlayer.getType());

        Button sell = (Button)listItemView.findViewById(R.id.sell);
        sell.setTag(currentPlayer);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Player clickedPlayer = (Player) v.getTag();
                    clickedPlayer.setSold(0);
                    playerReference = FirebaseDatabase.getInstance().getReference(lobby_in + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/players/player" + (position + 1));
                    playerReference.child("sold").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userReference = FirebaseDatabase.getInstance().getReference(lobby_in + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    user.setDisplayName(dataSnapshot.child("displayName").getValue(String.class));
                                    user.setCash(dataSnapshot.child("cash").getValue(String.class));
                                    user.setPoints(dataSnapshot.child("points").getValue(String.class));
                                    user.setNumberOfPlayers(dataSnapshot.child("numberOfPlayers").getValue(Integer.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    i = 1;
                } else {
                    Toast.makeText(getContext(), "Already 1 player sold", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return listItemView;
    }
}
