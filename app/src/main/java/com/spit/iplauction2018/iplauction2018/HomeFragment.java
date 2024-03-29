package com.spit.iplauction2018.iplauction2018;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.bid_price)
    TextView bid_price;
    @BindView(R.id.name_text_view)
    TextView name;
    @BindView(R.id.price_text_view)
    TextView base_price;
    @BindView(R.id.points_text_view)
    TextView point;
    @BindView(R.id.timer_text)
    TextView timer_text_view;
    @BindView(R.id.owner)
    TextView owner;
    @BindView(R.id.button_100)
    Button button_100;
    @BindView(R.id.button_200)
    Button button_200;
    @BindView(R.id.button_500)
    Button button_500;
    @BindView(R.id.button_1000)
    Button button_1000;
    @BindView(R.id.button_2000)
    Button button_2000;
    @BindView(R.id.button_5000)
    Button button_5000;
    @BindView(R.id.button_10000)
    Button button_10000;
    @BindView(R.id.button_20000)
    Button button_20000;
    @BindView(R.id.button_50000)
    Button button_50000;
    @BindView(R.id.skip)
    Button button_skip;
    Player player;
    User user = new User();
    View rootView;
    private Unbinder unbinder;
    Menu globalMenu;
    MenuItem myItem;
    CountDownTimer timer;
    int counter = 10, flag = 0, j = 1, i = 1;
    boolean skipFlag=true;
    String lobby_in, cash, winnerPoints, winnerKey, winnerName, type1, points;
    private DatabaseReference playerReference, nextPlayerReference, userReference, winnerReference;

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
        if(skipFlag){
            button_skip.setText("Start");
            button_100.setEnabled(false);
            button_200.setEnabled(false);
            button_500.setEnabled(false);
            button_1000.setEnabled(false);
            button_2000.setEnabled(false);
            button_5000.setEnabled(false);
            button_10000.setEnabled(false);
            button_20000.setEnabled(false);
            button_50000.setEnabled(false);
        }
        playerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "players/player" + i);
        winnerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "winner");
        winnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                winnerName = dataSnapshot.child("name").getValue(String.class);
                Toast.makeText(getContext(), winnerName, Toast.LENGTH_SHORT).show();
                winnerPoints = dataSnapshot.child("points").getValue(String.class);
                winnerKey = dataSnapshot.child("key").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nextPlayerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "players/player" + (i+1));
        valueEventListener(playerReference);

        userReference = FirebaseDatabase.getInstance().getReference(lobby_in + FirebaseAuth.getInstance().getCurrentUser().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.setDisplayName(dataSnapshot.child("displayName").getValue(String.class));
                user.setCash(dataSnapshot.child("cash").getValue(String.class));
                user.setPoints(dataSnapshot.child("points").getValue(String.class));
                user.setNumberOfPlayers(dataSnapshot.child("numberOfPlayers").getValue(Integer.class));
                user.setNumberOfwk(dataSnapshot.child("numberOfwk").getValue(Integer.class));
                user.setNumberOfallrounder(dataSnapshot.child("numberOfallrounder").getValue(Integer.class));
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
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
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

        button_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 1000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 1000);
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

        button_10000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 10000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 10000);
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

        button_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 200 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 200);
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

        button_2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 2000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 2000);
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

        button_20000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 20000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 20000);
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

        button_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 500 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 500);
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

        button_5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 5000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 5000);
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

        button_50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(player.getBidprice()) + 50000 > Double.parseDouble(user.getCash())) {
                    Toast.makeText(getContext(), "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else if (player.getSold() == 1) {
                    Toast.makeText(getContext(), "Player already sold", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 11;
                    button_skip.setEnabled(false);
                    button_skip.setBackgroundColor(0xFF263238);
                    final String newPrice = "" + (Double.parseDouble(player.getBidprice()) + 50000);
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

        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerReference.child("timestamp").setValue(ServerValue.TIMESTAMP);
                if(!skipFlag){
                    playerReference.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    button_skip.setEnabled(false);
                    button_100.setEnabled(false);
                    button_200.setEnabled(false);
                    button_500.setEnabled(false);
                    button_1000.setEnabled(false);
                    button_2000.setEnabled(false);
                    button_5000.setEnabled(false);
                    button_10000.setEnabled(false);
                    button_20000.setEnabled(false);
                    button_50000.setEnabled(false);
                }else {
                    skipFlag=false;
                    button_skip.setText("Skip");
                    button_100.setEnabled(true);
                    button_200.setEnabled(true);
                    button_500.setEnabled(true);
                    button_1000.setEnabled(true);
                    button_2000.setEnabled(true);
                    button_5000.setEnabled(true);
                    button_10000.setEnabled(true);
                    button_20000.setEnabled(true);
                    button_50000.setEnabled(true);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void change(double deduct, double add, String types) {
        double totalCash = Double.parseDouble(user.getCash());
        double cashRemain = totalCash - deduct;
        user.setCash("" + cashRemain);
        double basePoints = Double.parseDouble(user.getPoints());
        double newPoints = basePoints + add;
        user.setPoints("" + newPoints);
        user.setNumberOfPlayers(user.getNumberOfPlayers() + 1);
        if (types.compareTo("wicketkeeper") == 0) {
            user.setNumberOfwk(user.getNumberOfwk() + 1);
        }
        if (types.compareTo("allrounder") == 0) {
            user.setNumberOfallrounder(user.getNumberOfallrounder() + 1);
        }
        userReference.child("numberOfallrounder").setValue(user.getNumberOfallrounder());
        userReference.child("numberOfwk").setValue(user.getNumberOfwk());
        userReference.child("cash").setValue(user.getCash());
        userReference.child("points").setValue(user.getPoints());
        userReference.child("numberOfPlayers").setValue(user.getNumberOfPlayers());
        userReference.child("players").child("player" + j++).setValue(player);
        userReference.child("players").child("numberOfPlayers").setValue(user.getNumberOfPlayers());
        if (user.getNumberOfPlayers() >= 5 && user.getNumberOfwk() >= 1 && user.getNumberOfallrounder() >= 2 && Double.parseDouble(winnerPoints) < Double.parseDouble(user.getPoints())) {
            winnerReference.child("name").setValue(user.getDisplayName());
            winnerReference.child("key").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            winnerReference.child("points").setValue(user.getPoints());
        }
    }

    public void valueEventListener(DatabaseReference playerReference) {
        final DatabaseReference currentPlayer = playerReference;
        currentPlayer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                player = dataSnapshot.getValue(Player.class);
                try {
                    name.setText(player.getName());
                    base_price.setText(player.getPrice());
                    bid_price.setText(player.getBidprice());
                    point.setText(player.getPoints());
                    owner.setText(player.getOwnerName());
                } catch (Exception e) {

                }
                points = player.getPoints();
                type1 = player.getType();
                if (dataSnapshot.child("timestamp").exists() && player.getSold() != 1) {
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
                                currentPlayer.child("sold").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        timer.cancel();
                                        i++;
                                        changePlayer();
                                        if (player.getOwnedBy().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) == 0) {
                                            change(Double.parseDouble(player.getBidprice()), Double.parseDouble(player.getPoints()), player.getType());
                                        }
                                        button_skip.setEnabled(true);
                                        button_skip.setBackgroundColor(0xFFB0BEC5);
                                        button_100.setEnabled(true);
                                        button_200.setEnabled(true);
                                        button_500.setEnabled(true);
                                        button_1000.setEnabled(true);
                                        button_2000.setEnabled(true);
                                        button_5000.setEnabled(true);
                                        button_10000.setEnabled(true);
                                        button_20000.setEnabled(true);
                                        button_50000.setEnabled(true);
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
    }

    public void changePlayer(){
        if (i < 31) {
            playerReference = nextPlayerReference;
            valueEventListener(playerReference);
            nextPlayerReference = FirebaseDatabase.getInstance().getReference(lobby_in + "players/player" + (i + 1));
            timer_text_view.setText("No Bids Yet");
        } else {
            Intent intent = new Intent(getActivity(), WinnerActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


}