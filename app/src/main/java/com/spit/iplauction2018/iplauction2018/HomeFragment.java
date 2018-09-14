package com.spit.iplauction2018.iplauction2018;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //@BindView(R.id.button_100)Button button_100;
    //@BindView(R.id.bid_price)TextView bid_price;

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
        TextView base_price=(TextView)getActivity().findViewById(R.id.price_text_view);
        TextView bid_price_set=(TextView)getActivity().findViewById(R.id.bid_price);
        bid_price_set.setText(""+Double.parseDouble(base_price.getText().toString()));
        Button button_100=(Button)getActivity().findViewById(R.id.button_100);
        button_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView bid_price=(TextView)getActivity().findViewById(R.id.bid_price);
                bid_price.setText(""+(Double.parseDouble(bid_price.getText().toString())+100));
            }
        });
        Button button_100000=(Button)getActivity().findViewById(R.id.button_100000);
        button_100000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView bid_price=(TextView)getActivity().findViewById(R.id.bid_price);
                bid_price.setText(""+(Double.parseDouble(bid_price.getText().toString())+100000));
            }
        });
    }
}
