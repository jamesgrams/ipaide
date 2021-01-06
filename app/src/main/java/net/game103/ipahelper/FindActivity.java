package net.game103.ipahelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for choosing features and signs to get symbols.
 */
public class FindActivity extends AppCompatActivity {
    // A list of the current filters
    private List<LinearLayout> filters;

    @Override
    /**
     * Create the compare activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_find);
        filters = new ArrayList<>();
        addNewFilter(null);
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
            // This is the button to the left of the title
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
     * Look up matches.
     * @param   view    the view
     */
    public void findMatches(View view) {
        List<String> featureNames = new ArrayList<>();
        List<Boolean> signs = new ArrayList<>();
        String description = "";
        outerLoop:
        // First, get the featureSigns that we are trying to get the filters for
        for(int i = 0; i < this.filters.size(); i++) {
            LinearLayout current = this.filters.get(i);
            Spinner signSpinner = (Spinner) current.getChildAt(0);
            String signValue = signSpinner.getSelectedItem().toString();
            Spinner featureSpinner = (Spinner) current.getChildAt(1);
            String featureName = featureSpinner.getSelectedItem().toString();

            Boolean signBoolValue = null;
            if(signValue.equals("+")) signBoolValue = true;
            else if(signValue.equals("-")) signBoolValue = false;

            // Look at what has been currently added
            // Don't include something in the lookup
            // if it is duplicated
            for(int j = 0; j < featureNames.size(); j++) {
                String curFeatureName = featureNames.get(j);
                Boolean curSign = signs.get(j);
                if(curFeatureName.equals(featureName) && signBoolValue == curSign) {
                    continue outerLoop;
                }
            }

            // Manage populating lists
            signs.add(signBoolValue);
            featureNames.add(featureName);
        }
        // Add the description
        for(int i = 0; i < featureNames.size(); i ++) {
            if(i > 0) {
                if(featureNames.size() > 2) {
                    description += ",";
                }
                description += " ";
                if(i == featureNames.size() - 1) {
                    description += "and ";
                }
            }
            Boolean sign = signs.get(i);
            String signValue = "0";
            if(sign != null) {
                if (sign) signValue = "+";
                else signValue = "-";
            }
            description += signValue + " ";
            description += featureNames.get(i);
        }
        description += ".";

        // Now, we consult the controller
        Controller controller = Controller.getInstance();

        // Find the matches !!
        List<Symbol> symbols = controller.getSymbolsFromFeatureSigns(featureNames, signs);

        Intent intent = new Intent(this, FindResultsActivity.class);
        ArrayList<String> symbolNames = new ArrayList<>();
        // Convert the list of symbols into a list of strings that can be displayed
        for(int i = 0; i < symbols.size(); i++) {
            symbolNames.add(symbols.get(i).getName());
        }
        intent.putStringArrayListExtra("data", symbolNames);
        if(symbols.size() > 0) {
            String symbolString = "symbols are";
            if(symbols.size() == 1) {
                symbolString = "symbol is";
            }
            description = "The following " + symbolString + " " + description;
        }
        else {
            description = "The are no symbols that are " + description;
        }
        intent.putExtra("description", description);
        startActivity(intent);
    }

    /**
     * Add a new filter to the screen.
     * @param view  the view
     */
    public void addNewFilter(View view) {
        LinearLayout findDropdownLayout = (LinearLayout) findViewById(R.id.find_dropdown_layout);
        LinearLayout filter = createFeatureFilter();
        if(filters.size() > 0) {
            this.filters.get(0).getChildAt(2).setVisibility(View.VISIBLE);
        }
        this.filters.add(filter);
        findDropdownLayout.addView(filter);
    }

    /**
     * Clear all the added filters.
     * @param view  the view
     */
    public void refresh(View view) {
        this.filters.clear();
        ((LinearLayout) findViewById(R.id.find_dropdown_layout)).removeAllViews();
        addNewFilter(null);
    }

    /**
     * Create a filter.
     * @return the new filter
     */
    private LinearLayout createFeatureFilter() {
        final LinearLayout filter = new LinearLayout(this);
        LinearLayout.LayoutParams filterParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        filterParam.setMargins(0, 20, 0, 0);
        filter.setLayoutParams(filterParam);
        filter.setOrientation(LinearLayout.HORIZONTAL);
        Spinner signs = new Spinner(this);
        List<String> signsList = new ArrayList<>();
        signsList.add("+");
        signsList.add("-");
        signsList.add("0");
        populateSpinnerFromList(signs, signsList);
        filter.addView(signs);
        Spinner features = new Spinner(this);
        populateSpinnerFromList(features, Controller.getInstance().getFeatureNames());
        features.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        filter.addView(features);
        Button deleteFilterButton = new Button(this);
        deleteFilterButton.setText("-");
        deleteFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.size() > 0) {
                    LinearLayout parent = (LinearLayout) v.getParent();
                    ((LinearLayout) parent.getParent()).removeView(parent);
                    filters.remove(parent);
                    // If there are only two filters left, shouldn't be able to deleted
                    if (filters.size() == 1) {
                        filters.get(0).getChildAt(2).setVisibility(View.GONE);
                    }
                }
            }
        });
        if(this.filters.size() == 0) {
            deleteFilterButton.setVisibility(View.GONE);
        }
        filter.addView(deleteFilterButton);
        return filter;
    }

    /**
     * Populate a spinner's values from a given list.
     * @param spinner   the spinner to add values to
     * @param list  the list to put in the spinner
     */
    private void populateSpinnerFromList(Spinner spinner, List<String> list) {
        String [] array = list.toArray(new String[list.size()]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, array);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

}
