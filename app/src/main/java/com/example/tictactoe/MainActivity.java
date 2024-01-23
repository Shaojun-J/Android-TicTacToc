package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;
import com.example.tictactoe.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private int[] boxState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int activePlayer = 1;
    private int stepCounter = 1;
    private final List<int[]> winnerList = new ArrayList<>();
    private String playerOne = "X";
    private String playerTwo = "O";

    private static MainActivity context = null;
    ImageView[] imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        context = this;

        winnerList.add(new int[]{0, 1, 2});
        winnerList.add(new int[]{3, 4, 5});
        winnerList.add(new int[]{6, 7, 8});
        winnerList.add(new int[]{0, 3, 6});
        winnerList.add(new int[]{1, 4, 7});
        winnerList.add(new int[]{2, 5, 8});
        winnerList.add(new int[]{2, 4, 6});
        winnerList.add(new int[]{0, 4, 8});

        imageView = new ImageView[]{binding.image1, binding.image2, binding.image3,
                binding.image4, binding.image5, binding.image6,
                binding.image7, binding.image8, binding.image9
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //横竖屏切换前调用，保存用户想要保存的数据
        outState.putInt("boxPositions_size", boxState.length);
        for (int i = 0; i < boxState.length; i++) {
            outState.putInt("boxPositions_" + i, boxState[i]);
        }
        outState.putInt("stepCount",stepCounter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 屏幕切换完毕后调用用户存储的数据
        if (savedInstanceState != null) {
            int size = savedInstanceState.getInt("boxPositions_size", 0);
            for (int i = 0; i < size; i++) {
                boxState[i] = savedInstanceState.getInt("boxPositions_" + i, 0);
                if (boxState[i] == 1) {
                    imageView[i].setImageResource(R.drawable.x);
                } else if (boxState[i] == 2) {
                    imageView[i].setImageResource(R.drawable.o);
                }
            }
            stepCounter = savedInstanceState.getInt("stepCount",0);
        }
    }

    public static MainActivity getActivity() {
        return context;
    }

    @Override
    protected void onResume() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        playerOne = pref.getString("playerOne", "X");
        playerTwo = pref.getString("playerTwo", "O");
        changeActivePlayer(activePlayer);

        //restore status of game
        int size = pref.getInt("boxPositions_size", 0);
        for (int i = 0; i < size; i++) {
            boxState[i] = pref.getInt("boxPositions_" + i, 0);
            if (boxState[i] == 1) {
                imageView[i].setImageResource(R.drawable.x);
            } else if (boxState[i] == 2) {
                imageView[i].setImageResource(R.drawable.o);
            }
        }
        stepCounter = pref.getInt("stepCount",0);

        super.onResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("playerOne", playerOne);
        editor.putString("playerTwo", playerTwo);

        //save status of game
        editor.putInt("boxPositions_size", boxState.length);
        for (int i = 0; i < boxState.length; i++) {
            editor.putInt("boxPositions_" + i, boxState[i]);
        }
        editor.putInt("stepCount",stepCounter);

        editor.apply();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public String getPlayerOneName() {
        return playerOne;
    }

    public String getPlayerTwoName() {
        return playerTwo;
    }

    public void setPlayerOneName(String name) {
        playerOne = name;
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("playerOne", playerOne);
        editor.apply();
    }

    public void setPlayerTwoName(String name) {
        playerTwo = name;
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("playerTwo", playerTwo);
        editor.apply();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true; // consume the menu event
        }
        return super.onOptionsItemSelected(item);
    }

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int position = Integer.parseInt(img.getTag().toString());
        execute((ImageView) view, position);
    }

    public void restartGame(View view) {
        restartGame();
        changeActivePlayer(1);
    }

    private void enableTap(boolean flag) {
        for (ImageView view : imageView) {
            view.setEnabled(flag);
        }
    }

    private boolean checkWiner() {
        for (int[] position : winnerList) {
            if (boxState[position[0]] == activePlayer
                    && boxState[position[1]] == activePlayer
                    && boxState[position[2]] == activePlayer) {
                return true;
            }
        }
        return false;
    }

    private void execute(ImageView imageView, int index) {
        boxState[index] = activePlayer;

        if (activePlayer == 1) {
            imageView.setImageResource(R.drawable.x);
        } else {
            imageView.setImageResource(R.drawable.o);
        }

        if (checkWiner()) {
            String player = activePlayer == 1 ? playerOne : playerTwo;
            ResultDialog resultDialog = new ResultDialog(MainActivity.this,
                    player + " is a Winner!", MainActivity.this);
            resultDialog.setCancelable(false);
            resultDialog.show();
            enableTap(false);
        } else if (stepCounter == 9) {
            ResultDialog resultDialog = new ResultDialog(MainActivity.this,
                    "Match Draw", MainActivity.this);
            resultDialog.setCancelable(false);
            resultDialog.show();
        } else {
            changeActivePlayer(activePlayer == 1 ? 2 : 1);
            stepCounter++;
        }
    }


    public void restartGame() {
        for (int i = 0; i < boxState.length; i++) {
            boxState[i] = 0;
            imageView[i].setImageResource(R.drawable.white_box);
        }
        activePlayer = 1;
        stepCounter = 1;
        enableTap(true);
    }

    private void changeActivePlayer(int currentPlayerTurn) {
        activePlayer = currentPlayerTurn;
        if (activePlayer == 1) {
            binding.showArea.setText("Player " + playerOne + "'s Turn");
        } else {
            binding.showArea.setText("Player " + playerTwo + "'s Turn");
        }
    }

}