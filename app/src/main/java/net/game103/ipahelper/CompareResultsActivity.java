package net.game103.ipahelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/**
 * Activity displaying the results of a compare
 */
public class CompareResultsActivity extends AppCompatActivity {

    @Override
    /**
     * Create the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_compare_results);
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        List<String> results = intent.getStringArrayListExtra("data");
        List<String> differentFeatures = intent.getStringArrayListExtra("diffFeatures");
        List<String> differentSigns = intent.getStringArrayListExtra("diffSigns");
        final TextView description = (TextView) findViewById(R.id.compare_results_description);
        final TextView diffDescription = (TextView) findViewById(R.id.compare_results_diff_description);
        description.setText(intent.getStringExtra("description") + "\n");
        diffDescription.setText(intent.getStringExtra("diffDescription") + "\n");

        // Manage Matches
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.compare_results_layout);
        LinearLayout row = null;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("ipaidsettings",0);
        Boolean includeNull = settings.getBoolean("includeNull", false);
        Boolean nullHidden = false;
        for(int i = 0; i < results.size(); i ++) {
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(row);
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            textViewParam.setMargins(0, 20, 0, 0);
            textView.setLayoutParams(textViewParam);
            String result = results.get(i);
            textView.setText( result );
            LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(textLayout);
            row.addView(textView);
            // Don't include null features if that is not on
            // However, if there is a null feature, set the flag
            // that displays null characters are hidden are true
            if(!includeNull && result.charAt(0) == "0".charAt(0)) {
                row.setVisibility(View.GONE);
                nullHidden = true;
            }
        }

        // Check whether or not to add a picture of Cocoa :)
        String cocoaCheckString = intent.getStringExtra("cocoaCheckString");
        if(cocoaCheckString.equals("kʰoko")) {
            ImageView cocoa = new ImageView(this);
            cocoa.setImageResource(R.drawable.cocoa);
            cocoa.setAdjustViewBounds(true);
            LinearLayout.LayoutParams cocoaParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cocoaParams.setMargins(0, 20, 0, 0);
            cocoa.setLayoutParams(cocoaParams);
            linearLayout.addView(cocoa);
        }

        // Add the description stating there are null characters hidden
        row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(row);
        TextView lastView = new TextView(this);
        lastView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        LinearLayout.LayoutParams lastViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        lastViewParam.setMargins(0, 20, 0, 0);
        lastView.setLayoutParams(lastViewParam);
        lastView.setText( "To see matching null features, change the settings." );
        row.addView(lastView);
        if(!nullHidden) {
            row.setVisibility(View.GONE);
        }

        // Populate the differences layout
        LinearLayout linearDiffLayout = (LinearLayout) findViewById(R.id.compare_results_diff_layout);
        row = null;
        for(int i = 0; i < differentFeatures.size(); i ++) {
            // Add the feature names
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            linearDiffLayout.addView(row);
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            textViewParam.setMargins(0, 20, 0, 0);
            textView.setLayoutParams(textViewParam);
            String feature = differentFeatures.get(i);
            textView.setText( feature );
            LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(textLayout);
            row.addView(textView);

            // Add the feature signs
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            linearDiffLayout.addView(row);
            textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            // Set the height to a little more than wrap content would so that the bottom of
            // some characters don't get cut off
            // height is 12 sp more than text
            // There will always be at least two symbols for these
            int desiredSp = (int) (18 + 18 * 0.2) * differentSigns.get(i).split("\n").length + 12;
            int height = (int) (desiredSp * getResources().getDisplayMetrics().scaledDensity);
            textView.setHeight(height);
            textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            textViewParam.setMargins(0, 20, 0, 0);
            textView.setLayoutParams(textViewParam);
            String signs = differentSigns.get(i);
            textView.setText( signs );
            textLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(textLayout);
            row.addView(textView);
        }

        // Determine which view to start on
        if(action.equals("compare")) {
            showCompare(null);
        }
        else {
            showContrast(null);
        }
    }

    @Override
    /**
     * On resume, check to display null or not.
     * This is important if the user changes the null display setting
     * and then taps back
     */
    public void onResume() {
        super.onResume();
        displayNull();
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
     * Go through all the displayed results and hide them if they are null and includeNull is false.
     * otherwise, show them
     */
    public void displayNull() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("ipaidsettings",0);
        Boolean includeNull = settings.getBoolean("includeNull", false);
        LinearLayout results = (LinearLayout) findViewById(R.id.compare_results_layout);
        Boolean nullHidden = false;
        // The last child is the message stating that null features can be shown if turned on in
        // settings
        for( int i = 0; i < results.getChildCount() - 1; i++ ) {
            if (results.getChildAt(i) instanceof LinearLayout) {
                LinearLayout current = (LinearLayout) results.getChildAt(i);
                TextView textView = (TextView) current.getChildAt(0);
                String text = textView.getText().toString();
                if(!includeNull && text.charAt(0) == "0".charAt(0)) {
                    current.setVisibility(View.GONE);
                    nullHidden = true;
                }
                else {
                    current.setVisibility(View.VISIBLE);
                }
            }
        }
        if(nullHidden) {
            results.getChildAt(results.getChildCount() - 1).setVisibility(View.VISIBLE);
        }
        else {
            results.getChildAt(results.getChildCount() - 1).setVisibility(View.GONE);
        }
    }

    /**
     * Create a brand new compare.
     * @param view  the view
     */
    public void newCompare(View view) {
        Intent intent = new Intent(this, CompareActivity.class);
        startActivity(intent);
    }

    /**
     * Show the compare view.
     * @param view  the view
     */
    public void showCompare(View view) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.compare_results_scroll);
        scrollView.scrollTo(0,0);

        final TextView description = (TextView) findViewById(R.id.compare_results_description);
        final TextView diffDescription = (TextView) findViewById(R.id.compare_results_diff_description);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.compare_results_layout);
        LinearLayout linearDiffLayout = (LinearLayout) findViewById(R.id.compare_results_diff_layout);
        Button compareButton = (Button) findViewById(R.id.compare_results_compare_button);
        Button contrastButton = (Button) findViewById(R.id.compare_results_contrast_button);

        diffDescription.setVisibility(View.GONE);
        linearDiffLayout.setVisibility(View.GONE);
        description.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        compareButton.setEnabled(false);
        contrastButton.setEnabled(true);
    }

    /**
     * Show the contrast view.
     * @param view  the view
     */
    public void showContrast(View view) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.compare_results_scroll);
        scrollView.scrollTo(0,0);

        final TextView description = (TextView) findViewById(R.id.compare_results_description);
        final TextView diffDescription = (TextView) findViewById(R.id.compare_results_diff_description);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.compare_results_layout);
        LinearLayout linearDiffLayout = (LinearLayout) findViewById(R.id.compare_results_diff_layout);
        Button compareButton = (Button) findViewById(R.id.compare_results_compare_button);
        Button contrastButton = (Button) findViewById(R.id.compare_results_contrast_button);

        description.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        diffDescription.setVisibility(View.VISIBLE);
        linearDiffLayout.setVisibility(View.VISIBLE);
        contrastButton.setEnabled(false);
        compareButton.setEnabled(true);
    }

}
