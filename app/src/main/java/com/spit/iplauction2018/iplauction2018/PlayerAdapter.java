package com.spit.iplauction2018.iplauction2018;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

    DatabaseReference playerReference;

    public PlayerAdapter(Context TeamFragment, ArrayList<Player> player) {
        super(TeamFragment, 0, player);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
                Player clickedPlayer = (Player)v.getTag();
                Toast.makeText(getContext(), "Selling Player "+clickedPlayer.getName(), Toast.LENGTH_SHORT).show();
                clickedPlayer.setSold(0);
                //TODO: SELL THE PLAYER
            }
        });
        return listItemView;
    }
}
