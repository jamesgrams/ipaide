package net.game103.ipahelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity that begins on startup
 */
public class MainActivity extends AppCompatActivity {

    @Override
    /**
     * Create the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_main);
        Controller.getInstance();
    }

    @Override
    /**
     * Inflate the menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //MenuItem backButton = menu.findItem(android.R.id.home);
        //backButton.setVisible(false);
        MenuItem homeButton = menu.findItem(R.id.action_home);
        homeButton.setVisible(false);
        return true;
    }

    @Override
    /**
     * Manage action bar clicks (Back, Home, and Settings).
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Start a find
     * @param view  the view
     */
    public void goToFind(View view) {
        Intent find = new Intent(this, FindActivity.class);
        startActivity(find);
    }

    /**
     * Start a compare
     * @param view  the compare
     */
    public void goToCompare(View view) {
        Intent find = new Intent(this, CompareActivity.class);
        startActivity(find);
    }
}
