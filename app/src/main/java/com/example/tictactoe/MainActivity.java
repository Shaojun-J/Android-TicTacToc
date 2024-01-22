package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0}; //9 zero
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    private String playerOne = "X";
    private String playerTwo = "O";

    private static MainActivity context = null;

    EditTextPreference oneEditTextPreference;
    EditTextPreference twoEditTextPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        context = this;

        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{3, 4, 5});
        combinationList.add(new int[]{6, 7, 8});
        combinationList.add(new int[]{0, 3, 6});
        combinationList.add(new int[]{1, 4, 7});
        combinationList.add(new int[]{2, 5, 8});
        combinationList.add(new int[]{2, 4, 6});
        combinationList.add(new int[]{0, 4, 8});
    }

    public static MainActivity getActivity() {
        return context;
    }

    @Override
    protected void onResume() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        playerOne = pref.getString("playerOne", "X");
        playerTwo = pref.getString("playerTwo", "O");
        changePlayerTurn(playerTurn);

//        int size = pref.getInt("boxPositions_size",0);
//        for(int i=0; i<size; i++){
//            boxPositions[i] =  pref.getInt("boxPositions_" + i, 0);
//        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("playerOne", playerOne);
        editor.putString("playerTwo", playerTwo);

//        editor.putInt("boxPositions_size",boxPositions.length);
//        for(int i=0; i<boxPositions.length; i++){
//            editor.putInt("boxPositions_" + i, boxPositions[i]);
//        }

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
        performAction((ImageView) view, position);
    }

    public void restartGame(View view) {
        restartMatch();
        changePlayerTurn(1);
    }

    private void enableTap(boolean flag) {
        binding.image1.setEnabled(flag);
        binding.image2.setEnabled(flag);
        binding.image3.setEnabled(flag);
        binding.image4.setEnabled(flag);
        binding.image5.setEnabled(flag);
        binding.image6.setEnabled(flag);
        binding.image7.setEnabled(flag);
        binding.image8.setEnabled(flag);
        binding.image9.setEnabled(flag);
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.x);
            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, playerOne
                        + " is a Winner!", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
//                binding.showArea.setText("Player X wins");
                enableTap(false);
            } else if (totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, "Match Draw", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.o);
            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, playerTwo
                        + " is a Winner!", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
                enableTap(false);
            } else if (totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, "Match Draw", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;

            }
        }
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            binding.showArea.setText("Player " + playerOne + "'s Turn");
        } else {
            binding.showArea.setText("Player " + playerTwo + "'s Turn");
        }
    }

    private boolean checkResults() {
        boolean response = false;
        for (int i = 0; i < combinationList.size(); i++) {
            final int[] combination = combinationList.get(i);
            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                response = true;
            }
        }
        return response;
    }

    private boolean isBoxSelectable(int boxPosition) {
        boolean response = false;
        if (boxPositions[boxPosition] == 0) {
            response = true;
        }
        return response;
    }

    public void restartMatch() {
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0}; //9 zero
        playerTurn = 1;
        totalSelectedBoxes = 1;
        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);
        enableTap(true);
    }
}