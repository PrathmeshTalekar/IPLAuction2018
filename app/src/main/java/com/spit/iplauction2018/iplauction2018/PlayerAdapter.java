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
    SharedPreferences.Editor editor;
    String lobby_in;
    User user = new User();
    int i = 0, j = 0;
    int sell_done;
    Player player;

    public PlayerAdapter(Context TeamFragment, ArrayList<Player> player) {
        super(TeamFragment, 0, player);
        sharedPreferences = TeamFragment.getSharedPreferences("MY_GAME", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lobby_in = sharedPreferences.getString("lobby", null);
        sell_done = sharedPreferences.getInt("sell", 0);
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
                if (sell_done == 0) {
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
                                    playerReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            player = dataSnapshot.getValue(Player.class);
                                            changeDetails();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    sell_done = 1;
                    editor.putInt("sell", 1);
                    editor.commit();
                } else {
                    Toast.makeText(getContext(), "Already 1 player sold", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return listItemView;
    }

    public void changeDetails() {
        if (j == 0) {
            if (player.getType().compareTo("wicketkeeper") == 0) {

                userReference.child("numberOfwk").setValue(user.getNumberOfwk());
                j = 1;
            }
            if (player.getType().compareTo("allrounder") == 0) {
                userReference.child("numberOfallrounder").setValue(user.getNumberOfallrounder());
                j = 1;
            }
            user.setNumberOfPlayers(user.getNumberOfPlayers() - 1);
            userReference.child("numberOfPlayers").setValue(user.getNumberOfPlayers());
            userReference.child("players").child("numberOfPlayers").setValue(user.getNumberOfPlayers());
            double x = Double.parseDouble(user.getCash()) + Double.parseDouble(player.getPrice());
            user.setCash("" + x);
            userReference.child("cash").setValue(user.getCash());
            double y = Double.parseDouble(user.getPoints()) - Double.parseDouble(player.getPoints());
            user.setPoints("" + y);
            userReference.child("points").setValue(user.getPoints());
        }
    }
}