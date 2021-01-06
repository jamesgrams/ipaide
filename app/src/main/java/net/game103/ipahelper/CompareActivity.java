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
 * Activity for choosing what symbols that you want to compare
 */
public class CompareActivity extends AppCompatActivity {
    // A list of the current filters
    private List<LinearLayout> filters;

    @Override
    /**
     * Create the compare activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_compare);
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
     * @param   action  The action which has been selected (compare or contrast)
     */
    public void findMatches(String action) {
        List<String> symbols = new ArrayList<>();
        String description = "";
        String cocoaCheckString = "";
        // First, get the symbols that we are trying to get the filters for
        outerLoop:
        for(int i = 0; i < this.filters.size(); i++) {
            LinearLayout current = this.filters.get(i);
            Spinner symbolSpinner = (Spinner) current.getChildAt(0);
            String symbolValue = symbolSpinner.getSelectedItem().toString();
            cocoaCheckString += symbolValue;

            // Look at what has been currently added
            // Don't include something in the lookup
            // if it is duplicated
            for(int j = 0; j < symbols.size(); j++) {
                String curSymbol = symbols.get(j);
                if(curSymbol.equals(symbolValue)) {
                    continue outerLoop;
                }
            }

            // Manage adding to the list
            symbols.add(symbolValue);
        }
        // Add the description
        for(int i = 0; i < symbols.size(); i ++) {
            if(i > 0) {
                if(symbols.size() > 2) {
                    description += ",";
                }
                description += " ";
                if(i == symbols.size() - 1) {
                    description += "and ";
                }
            }
            description += symbols.get(i);
        }

        // Now, we consult the controller
        Controller controller = Controller.getInstance();

        // Find the matches !!
        List<FeatureSign> featureSigns = controller.getFeatureSignsFromSymbols(symbols);

        Intent intent = new Intent(this, CompareResultsActivity.class);
        ArrayList<String> featureSignNames = new ArrayList<>();
        ArrayList<String> namesOnly = new ArrayList<>();
        // Convert the list of feature sign to a list of strings that will be displayed in the
        // result
        for(int i = 0; i < featureSigns.size(); i++) {
            Boolean sign = featureSigns.get(i).getSign();
            String signValue = "0";
            if (sign != null) {
                if (sign) signValue = "+";
                else signValue = "-";
            }
            featureSignNames.add(signValue + " " + featureSigns.get(i).getFeature().getName());
            // Add the names so we know which features are matching
            // This helps for when looking up the contrasting features
            namesOnly.add(featureSigns.get(i).getFeature().getName());
        }
        intent.putStringArrayListExtra("data", featureSignNames);

        // The of different features and different signs
        ArrayList<String> differentFeatures = new ArrayList<>();
        ArrayList<String> differentSigns = new ArrayList<>();
        // For all the possible features
        for(int i = 0; i < controller.getFeatures().size(); i++) {
            Feature current = controller.getFeatures().get(i);
            // if the names of matches does not contain this feature, there is a difference
            if(!namesOnly.contains(current.getName())) {
                // This is a different feature
                differentFeatures.add(current.getName());
                String forDifferentSign = "";
                // Create a string (one symbol per line) explaining the signs each selected symbol has
                // for the current feature
                for(int j = 0; j < symbols.size(); j++) {
                    if(j > 0) {
                        forDifferentSign += "\n";
                    }
                    String currentSymbolName = symbols.get(j);
                    Boolean sign = controller.getSignByFeatureSymbol(currentSymbolName, current.getName());
                    String signValue = "0";
                    if (sign != null) {
                        if (sign) signValue = "+";
                        else signValue = "-";
                    }
                    forDifferentSign += currentSymbolName + " is " + signValue;
                }
                differentSigns.add(forDifferentSign);
            }
        }
        intent.putStringArrayListExtra("diffFeatures", differentFeatures);
        intent.putStringArrayListExtra("diffSigns", differentSigns);

        // The diffDescription and description are the same at this point
        String diffDescription = description;

        // Finish up the description
        if(featureSigns.size() > 0) {
            if(symbols.size() == 1) {
                description += " has";
            }
            else {
                description += " have";
            }
            description += " the following feature";
            if(featureSigns.size() > 1) {
                description += "s";
            }
        }
        else {
            if(symbols.size() == 1) {
                description += " has no features";
            }
            else {
                description += " have no common features";
            }
        }
        description += ".";
        // Finish up diff description
        if(differentFeatures.size() > 0) {
            if(symbols.size() == 1) {
                diffDescription += " has";
            }
            else {
                diffDescription += " have";
            }
            diffDescription += " the following different feature";
            if(differentFeatures.size() > 1) {
                diffDescription += "s";
            }
        }
        else {
            if(symbols.size() == 1) {
                diffDescription += " is not being compared to anything";
            }
            else {
                diffDescription += " have no differences";
            }
        }
        diffDescription += ".";

        intent.putExtra("description", description);
        intent.putExtra("diffDescription", diffDescription);
        intent.putExtra("cocoaCheckString", cocoaCheckString);
        intent.putExtra("action", action);
        startActivity(intent);
    }

    /**
     * Get results and go to the compare view by default.
     * @param view the view
     */
    public void compareFind(View view) {
        findMatches("compare");
    }

    /**
     * Get results and go to the contrast view by default.
     * @param view the view
     */
    public void contrastFind(View view) {
        findMatches("contrast");
    }

    /**
     * Add a new filter to the screen.
     * @param view  the view
     */
    public void addNewFilter(View view) {
        LinearLayout findDropdownLayout = (LinearLayout) findViewById(R.id.compare_dropdown_layout);
        LinearLayout filter = createSymbolFilter();
        if(filters.size() > 0) {
            this.filters.get(0).getChildAt(1).setVisibility(View.VISIBLE);
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
        ((LinearLayout) findViewById(R.id.compare_dropdown_layout)).removeAllViews();
        addNewFilter(null);
    }

    /**
     * Create a filter.
     * @return the new filter
     */
    private LinearLayout createSymbolFilter() {
        final LinearLayout filter = new LinearLayout(this);
        LinearLayout.LayoutParams filterParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        filterParam.setMargins(0, 20, 0, 0);
        filter.setLayoutParams(filterParam);
        filter.setOrientation(LinearLayout.HORIZONTAL);
        Spinner symbols = new Spinner(this);
        populateSpinnerFromList(symbols, Controller.getInstance().getSymbolsNames());
        symbols.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        filter.addView(symbols);
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
                        filters.get(0).getChildAt(1).setVisibility(View.GONE);
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
        java.util.Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        String [] array = list.toArray(new String[list.size()]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, array);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

}
