package com.blashca.womanshealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button examineButton = (Button) findViewById(R.id.examine_button);
        examineButton.setOnClickListener(this);
        Button weightButton = (Button) findViewById(R.id.weight_button);
        weightButton.setOnClickListener(this);
        Button periodButton = (Button) findViewById(R.id.period_button);
        periodButton.setOnClickListener(this);
        Button pillsButton = (Button) findViewById(R.id.pills_button);
        pillsButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.settins_button);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.examine_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.weight_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.period_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.pills_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.settins_button:
                makeIntent(WeightActivity.class);
                break;

            default:
                break;
        }
    }

    private void makeIntent(Class<?> nextIntentClass) {
        Intent intent = new Intent(getApplicationContext(), nextIntentClass);
        startActivity(intent);
    }
}
