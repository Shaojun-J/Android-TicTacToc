package com.example.tictactoe;

import androidx.annotation.NonNull;
import android.view.View;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

public class ResultDialog extends Dialog {

    private final MainActivity parentActivity;
    private final String msg;


    public ResultDialog(@NonNull Context context, String msg, MainActivity parentActivity) {
        super(context);
        this.msg = msg;
        this.parentActivity = parentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        TextView messageText = findViewById(R.id.messageText);
        messageText.setText(msg);

        Button startAgainButton = findViewById(R.id.startButton);
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.restartGame();
                dismiss();
            }
        });
    }
}