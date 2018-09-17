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

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    View rootView;
    private Unbinder unbinder;
    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Player> player = new ArrayList<Player>();
        player.add(new Player("Shikar Dhawan", "batsman"));
        player.add(new Player("Virendra Sehwag","batsman"));
        player.add(new Player("Ajinkya Rahane", "batsman"));
        player.add(new Player("Sachin Tendulkar","batsman"));
        player.add(new Player("Ricky Ponting","batsman"));
        player.add(new Player("MS Dhoni","wicketkeeper"));
        player.add(new Player("Yuvraj Singh","all rounder"));
        player.add(new Player("Ravindra Ashwin","bowler"));
        player.add(new Player("Lasith Malinga","bowler"));
        player.add(new Player("Ishan Sharma","bowler"));
        player.add(new Player("Sunil Narine","bowler"));
        PlayerAdapter adapter = new PlayerAdapter(getContext(), player);
        ListView listView = (ListView) getActivity().findViewById(R.id.list);
        listView.setAdapter(adapter);

    }


}
