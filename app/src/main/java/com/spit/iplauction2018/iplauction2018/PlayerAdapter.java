package com.spit.iplauction2018.iplauction2018;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

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

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.playername);
        nameTextView.setText(currentPlayer.getName());

        TextView typeTextView = (TextView) listItemView.findViewById(R.id.playertype);
        typeTextView.setText(currentPlayer.getType());

        return listItemView;
    }
}
