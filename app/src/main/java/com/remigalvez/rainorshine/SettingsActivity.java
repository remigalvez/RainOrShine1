package com.remigalvez.rainorshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup unitsBtns;
    RadioButton celsiusBtn;
    RadioButton fahrenheitBtn;

    SeekBar daySlider;
    TextView numDaysTxt;
    int numDays;

    String units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        unitsBtns = (RadioGroup) findViewById(R.id.unitsBtns);
        units = Settings.units;

        celsiusBtn = (RadioButton) findViewById(R.id.celsiusBtn);
        fahrenheitBtn = (RadioButton) findViewById(R.id.fahrenheitBtn);

        numDays = Settings.numDays;
        numDaysTxt = (TextView) findViewById(R.id.numDaysTxt);

        daySlider = (SeekBar) findViewById(R.id.daySlider);
        daySlider.setProgress(numDays);

        daySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int startingProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numDaysTxt.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startingProgress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                numDays = seekBar.getProgress();
            }
        });

        switch (Settings.units) {
            case "C":
                unitsBtns.check(R.id.celsiusBtn);
                break;
            case "F":
                unitsBtns.check(R.id.fahrenheitBtn);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Settings.setNumDays(numDays);
        Settings.setUnits(units);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.fahrenheitBtn:
                if (checked)
                    units = "F";
                    break;
            case R.id.celsiusBtn:
                if (checked)
                    units = "C";
                    break;
        }
    }
}
