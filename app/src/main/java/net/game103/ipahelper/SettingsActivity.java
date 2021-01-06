package net.game103.ipahelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Activity containing the settings and credits
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    /**
     * Create the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        Switch nullSwitch = (Switch) findViewById(R.id.include_null_switch);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("ipaidsettings",0);
        Boolean includeNull = settings.getBoolean("includeNull", false);
        nullSwitch.setChecked(includeNull);
    }

    @Override
    /**
     * Inflate the menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem settingsButton = menu.findItem(R.id.action_settings);
        settingsButton.setVisible(false);
        return true;
    }

    @Override
    /**
     * Manage action bar clicks (Back, Home, and Settings).
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the button to the left of the title
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Toggle whether or not null featureSigns are shown in compare results.
     * @param view  the view
     */
    public void toggleNull(View view) {
        Switch nullSwitch = (Switch) findViewById(R.id.include_null_switch);
        Boolean value = nullSwitch.isChecked();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("ipaidsettings",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("includeNull", value);
        editor.apply();
    }
}
