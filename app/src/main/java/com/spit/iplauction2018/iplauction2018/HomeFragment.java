package com.spit.iplauction2018.iplauction2018;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
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
    @BindView(R.id.owner)
    TextView owner;
    CountDownTimer timer;
    int counter = 10, flag = 0;
    String lobby_in, cash;
    View rootView;
    MenuItem myItem;
    private Unbinder unbinder;
    Menu globalMenu;
    User user = new User();
    String points;
    int i = 1;
    int j = 1;
    //    long time;
    private DatabaseReference playerReference, userReference;

    public void setGlobalMenu(Menu globalMenu) {
        this.globalMenu = globalMenu;
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    void setMoney(final String money) {
        if (globalMenu != null) {
            myItem = globalMenu.findItem(R.id.moneyBox);
            myItem.setTitle("" + money);
        } else {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    setMoney(money);
                }
            };
            new Handler().postDelayed(r, 100);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        lobby_in = getArguments().getString("lobby");
        return rootView;
    }

    public static String getTimeDate(long timestamp) {
        try {
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        while (i<=30) {

        playerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "players/player" + i);
        playerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                player = dataSnapshot.getValue(Player.class);
                name.setText(player.getName());
                base_price.setText(player.getPrice());
                bid_price.setText(player.getBidprice());
                owner.setText(player.getOwnerName());
                points = player.getPoints();
                if (dataSnapshot.child("timestamp").exists() && player.getSold() != 1) {
//                    time =dataSnapshot.child("timestamp").getValue(Long.class) ;
                    if (flag != 0) {
                        timer.cancel();
                        timer = null;
                        counter = 10;
                    }
                    timer = new CountDownTimer(11000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            try {
                                timer_text_view.setText("" + counter--);
                            } catch (Exception e) {
                            }
                            flag = 1;
                        }
                        public void onFinish() {
                            try {
                                timer_text_view.setText("SOLD!!");
                            } catch (Exception e) {
                            }
                            if (player.getSold() != 1) {
                                player.setSold(1);
                                playerReference.child("sold").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        timer.cancel();
                                        if (player.getOwnedBy().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) == 0) {
                                            change(Double.parseDouble(player.getBidprice()), Double.parseDouble(player.getPoints()));
                                        }
                                        i++;
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference = FirebaseDatabase.getInstance().getReference(lobby_in + FirebaseAuth.getInstance().getCurrentUser().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.setDisplayName(dataSnapshot.child("displayName").getValue(String.class));
                user.setCash(dataSnapshot.child("cash").getValue(String.class));
                user.setPoints(dataSnapshot.child("points").getValue(String.class));
                user.setNumberOfPlayers(dataSnapshot.child("numberOfPlayers").getValue(Integer.class));
                cash = user.getCash();
                setMoney(cash);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 100 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 100);
                    player.setBidprice(newPrice);
                    player.setOwnerName(user.getDisplayName());
                    player.setOwnedBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    playerReference.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    playerReference.child("ownedBy").setValue(player.getOwnedBy());
                    playerReference.child("ownerName").setValue(player.getOwnerName());
                    playerReference.child("bidprice").setValue(player.getBidprice());
                }
            }

        });
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void change(double deduct, double add) {
        double totalCash = Double.parseDouble(user.getCash());
        double cashRemain = totalCash - deduct;
        user.setCash("" + cashRemain);
        double basePoints = Double.parseDouble(user.getPoints());
        double newPoints = basePoints + add;
        user.setPoints("" + newPoints);
        user.setNumberOfPlayers(user.getNumberOfPlayers() + 1);
        userReference.child("cash").setValue(user.getCash());
        userReference.child("points").setValue(user.getPoints());
        userReference.child("numberOfPlayers").setValue(user.getNumberOfPlayers());
        userReference.child("players").child("player" + j++).setValue(player);
        userReference.child("players").child("numberOfPlayers").setValue(user.getNumberOfPlayers());
    }

}
