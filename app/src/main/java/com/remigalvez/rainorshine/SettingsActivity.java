package com.remigalvez.rainorshine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = "SettingsActivity";

    RadioGroup unitsBtns;
    RadioButton celsiusBtn;
    RadioButton fahrenheitBtn;

    EditText zipcode;

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

        zipcode = (EditText) findViewById(R.id.zipcode);

        daySlider = (SeekBar) findViewById(R.id.daySlider);
        daySlider.setProgress(numDays);
        numDaysTxt.setText("" + numDays);

        daySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int startingProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    progress = 1;
                    seekBar.setProgress(1);
                }
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

        zipcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String zipcode = v.getText().toString();
                    if (zipcode.length() < 5) {
                        invalidZipcodeAlert();
                    } else {
                        Settings.MAIN_ACTIVITY.refreshData(zipcode);
                        finish();
                        handled = true;
                    }
                }
                return handled;
            }
        });
    }

    public void invalidZipcodeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                .setMessage(R.string.invalid_zip_code)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
