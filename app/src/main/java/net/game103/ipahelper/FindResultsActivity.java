package net.game103.ipahelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FindResultsActivity extends AppCompatActivity {

    @Override
    /**
     * Create the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_find_results);
        Intent intent = getIntent();
        List<String> results = intent.getStringArrayListExtra("data");
        java.util.Collections.sort(results, String.CASE_INSENSITIVE_ORDER);
        final TextView description = (TextView) findViewById(R.id.finder_results_description);
        description.setText(intent.getStringExtra("description") + "\n");

        // Manage Matches
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.find_results_layout);
        LinearLayout row = null;
        for(int i = 0; i < results.size(); i ++) {
            if(i % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(row);
            }
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            // height is 12 sp more than text
            int desiredSp = 36;
            int height = (int) (desiredSp * getResources().getDisplayMetrics().scaledDensity);
            textView.setHeight(height);
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            textViewParam.setMargins(0, 20, 0, 0);
            textView.setLayoutParams(textViewParam);
            textView.setText( results.get(i) );
            LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(textLayout);
            row.addView(textView);
        }
    }

    @Override
    /**
     * Inflate the menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    /**
     * Manage action bar clicks (Back, Home, and Settings).
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Create a brand new find.
     * @param view  the view
     */
    public void newFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
    }

}
