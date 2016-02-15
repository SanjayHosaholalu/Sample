package com.application.companies.views;

import com.application.companies.R;
import com.application.companies.model.Company;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyDetailsViewActivity extends AppCompatActivity{

	private TextView companyNameTV;
	private TextView companyOwnerTV;
	private TextView companyStartDateTV;
	private TextView companyDepartmentsTV;
	private TextView companyDescriptionTV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		// The activity is being created.
		setContentView(R.layout.company_details_view_activity);

		companyNameTV = (TextView) findViewById(R.id.company_name);
		companyOwnerTV = (TextView) findViewById(R.id.company_owner);
		companyStartDateTV = (TextView) findViewById(R.id.company_start_date);
		companyDepartmentsTV = (TextView) findViewById(R.id.company_departments);
		companyDescriptionTV = (TextView) findViewById(R.id.company_descriptions);
		
		Bundle bundle = getIntent().getExtras();
		Company company = bundle.getParcelable("company");

		setUpDetailsView(company);

		// Set up the action bar to show a back button.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// set the title for action bar button. 
		getSupportActionBar().setTitle(" Details View");
		// remove the application icon in action bar.
		getSupportActionBar().setDisplayShowHomeEnabled(false);
	}

	// set up details view
	private void setUpDetailsView(Company company) {
		// TODO Auto-generated method stub
		if(company != null){
			companyNameTV.setText(company.getCompanyName().trim());
			companyOwnerTV.setText(company.getCompanyOwner().trim());
			companyStartDateTV.setText(company.getCompanyStartDate().trim());
			companyDepartmentsTV.setText(company.getCompanyDepartments().trim());
			companyDescriptionTV.setText(company.getCompanyDescription().trim());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_no_setting, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		//menu.findItem(R.id.action_settings);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// Handle action buttons
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
