package com.spit.iplauction2018.iplauction2018;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //@BindView(R.id.button_100)Button button_100;
    //@BindView(R.id.bid_price)TextView bid_price;
    private FirebaseDatabase mFirebaseDatabase;
    @BindView(R.id.bid_price)
    TextView bid_price;
    private ChildEventListener mChildEventListener;
    Player player;
    @BindView(R.id.name_text_view)
    TextView name;
    @BindView(R.id.price_text_view)
    TextView base_price;
    @BindView(R.id.button_100)
    Button button_100;
    @BindView(R.id.button_500)
    Button button_500;
    @BindView(R.id.button_1000)
    Button button_1000;
    @BindView(R.id.timer_text)
    TextView timer_text_view;
    CountDownTimer timer;
    int counter = 10, flag;
    String lobby_in, cash;
    View rootView;
    MenuItem myItem;
    private Unbinder unbinder;
    Menu globalMenu;
    private DatabaseReference mDatabaseReference, reference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        lobby_in = getArguments().getString("lobby");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email = email.replaceAll("\\.", "");
        email = email.replaceAll("@", "");
        reference = FirebaseDatabase.getInstance().getReference(lobby_in);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cash = dataSnapshot.child("cash").getValue(String.class);
                if (globalMenu != null) {
                    myItem = globalMenu.findItem(R.id.moneyBox);
                    myItem.setTitle("" + cash);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        button_1000.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+1000);
//                player.setBidprice(newPrice);
//                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        bid_price.setText(""+(newPrice));
//                    }
//                });
//            }
//        });
//        button_500.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+500);
//                player.setBidprice(newPrice);
//                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        bid_price.setText(""+(newPrice));
//                    }
//                });
//            }
//        });
        button_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag != 0) {
                    timer.cancel();
                }
                counter = 10;
                final String newPrice = ""+(Double.parseDouble(player.getBidprice())+100);
                player.setBidprice(newPrice);
                mDatabaseReference.setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bid_price.setText(""+(newPrice));
                    }
                });
                timer = new CountDownTimer(11000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timer_text_view.setText(String.valueOf(counter));
                        counter--;
                        flag = 1;
                    }

                    public void onFinish() {
                        timer_text_view.setText("SOLD!!");
                    }
                }.start();
            }


        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
