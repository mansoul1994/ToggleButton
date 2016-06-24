package com.mansoul.togglebutton;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.tb_button);

//        toggleButton.setSwitchBackground(R.drawable.switch_background);
//        toggleButton.setSlideButton(R.drawable.slide_button);
//        toggleButton.setSwitchState(true);

        toggleButton.setOnSwitchStateListener(new ToggleButton.OnSwitchStateListener() {
            @Override
            public void onSwitchState(boolean state) {
                Toast.makeText(mContext, "switchState:" + state, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
