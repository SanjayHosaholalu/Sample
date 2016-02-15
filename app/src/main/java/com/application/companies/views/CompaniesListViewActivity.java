package com.application.companies.views;

import java.util.ArrayList;

import com.application.companies.R;
import com.application.companies.DataMgrs.CompaniesDataMgr;
import com.application.companies.Utilis.AsyncDataGet;
import com.application.companies.Utilis.Consts;
import com.application.companies.Utilis.GetDataInterface;
import com.application.companies.Utilis.Utils;
import com.application.companies.model.Company;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompaniesListViewActivity extends AppCompatActivity{

	private ListView listView = null;
	private CompanyAdapter mCompanyAdapter = null;

	private ImageButton closeSearch = null;
	private EditText searchText = null;
	private RelativeLayout searchComponentLayout = null;
	private TextView filterText = null;

	private String enteredString = "";

	private ArrayList<Company> companiesDataList = null;

	private String[] filterItems = { Consts.ALL, Consts.ACCOUNTING, Consts.ADVERTISING, Consts.ASSET_MANAGEMENT, Consts.CUSTOMER_RELATIONS, Consts.CUSTOMER_SERVICES,
			Consts.FINANCES, Consts.HUMAN_RESOURCES, Consts.LEGAL_DEPARTMENT, Consts.MEDIA_RELATIONS, Consts.PAYROLL, Consts.QUALITY_ASSURANCE,
			Consts.SALES, Consts.RESEARCH, Consts.TECH_SUPPORT};

	private static int selectedTrack = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		// The activity is being created.
		setContentView(R.layout.company_list_layout);

		searchComponentLayout = (RelativeLayout) findViewById(R.id.search_component);
		
		filterText = (TextView) findViewById(R.id.filter_text_view);

		closeSearch = (ImageButton) findViewById(R.id.search_close);
		searchText = (EditText) findViewById(R.id.search_text);

		listView = (ListView) findViewById(android.R.id.list);

		setUpListView();
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
		// Inflate the menu; this adds items to the action bar if it is present.  
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.companies_menu, menu);
		return true; 
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
		int id = item.getItemId();

		if (id == R.id.action_search) {
			searchItemFromList();
		}else if(id == R.id.action_filter){
			searchItemByFilter();
		}
		return true;
	}

	// search items by filter
	private void searchItemByFilter() {
		// TODO Auto-generated method stub
		if(filterItems != null && filterItems.length>0){
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
			builderSingle.setTitle(" Departments ");
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, filterItems);

			builderSingle.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setSingleChoiceItems(arrayAdapter, selectedTrack, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					selectedTrack = which;
					String strName = arrayAdapter.getItem(which);
					searchCompanyByFilterItem(strName);
					dialog.cancel();
				}
			});

			builderSingle.show();
		}		
	}

	// search 
	private void searchCompanyByFilterItem(String strName) {
		// TODO Auto-generated method stub
		if(strName.trim().equalsIgnoreCase(Consts.ALL)){
			filterText.setVisibility(View.GONE);
			setDefaultDataToList();
		}else{
			getResultsForFilterItem(strName);
		}

	}


	// get fiter items from the list
	private void getResultsForFilterItem(String str) {
		filterText.setVisibility(View.VISIBLE);
		filterText.setText(" Filter By: " + str);
		// TODO Auto-generated method stub
		if(listView != null && !mCompanyAdapter.isEmpty()){
			CompaniesDataMgr companiesDataMgr = new CompaniesDataMgr();
			ArrayList<Company> companiesList = companiesDataMgr.getCompanyListByFilter(this, str);
			if(companiesList != null && companiesList.size() > 0){
				setArrayAdapter(companiesList);
			}
		}
	}

	// search items
	private void searchItemFromList(){
		filterText.setVisibility(View.GONE);

		// Override the function in the derived class
		//searchText.setText("");
		searchComponentLayout.setVisibility(RelativeLayout.VISIBLE);

		searchLayoutBtnsListner();
	}

	// listeners for search 
	private void searchLayoutBtnsListner() {
		// TODO Auto-generated method stub

		// close button click listener 
		closeSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				searchText.setText("");
				searchComponentLayout.setVisibility(RelativeLayout.GONE);
				setDefaultDataToList();
			}
		});

		// search button click listener
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
				// When user changed the Text
				enteredString = charSequence.toString().trim();
				if (enteredString != "" && enteredString != null && !enteredString.isEmpty() && enteredString.length()>0) {
					if (mCompanyAdapter != null) {
						mCompanyAdapter.getFilter().filter(enteredString.toString());
					}
				} else {
					setDefaultDataToList();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence charSequence, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	// reload the data list to view
	private void setDefaultDataToList(){
		//check whether adapter is empty
		CompaniesDataMgr companiesDataMgr = new CompaniesDataMgr();
		this.companiesDataList = companiesDataMgr.getCompanyDataList(this);
		if(this.companiesDataList != null && this.companiesDataList.size() > 0){
			setArrayAdapter(this.companiesDataList);
		}
	}

	// create list view with data list
	private void setUpListView() {
		// TODO Auto-generated method stub

		// Get data from webservice
		GenericDataLoader dataLoader = new GenericDataLoader(CompaniesListViewActivity.this);
		AsyncDataGet registrationInfoData = new AsyncDataGet(CompaniesListViewActivity.this, dataLoader, "Retrieving data ...");
		registrationInfoData.execute();	
	}

	// async call to get the data from web service
	public class GenericDataLoader implements GetDataInterface {

		Context context = null;
		ArrayList<Company> companiesList = null;

		GenericDataLoader(Context context) {
			this.context = context;
		}

		@Override
		public boolean execute() {
			boolean success = false;
			CompaniesDataMgr companiesDataMgr = new CompaniesDataMgr();
			companiesList = companiesDataMgr.getCompanyDataList(context);
			if(companiesList != null && companiesList.size() > 0){
				success = true;	
			}
			return success;
		}

		public void postExecute(boolean executionSuccess) {
			if (executionSuccess == true)
			{
				if(companiesList != null && companiesList.size() > 0){
					setArrayAdapter(companiesList);
				}

			} 
			else 
			{
				// check whether internet is available
				if(Utils.isNetworkAvailable(context.getApplicationContext())){
					Toast.makeText(CompaniesListViewActivity.this.getApplicationContext(), " No data to display.", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context.getApplicationContext(), "Internet connection unavailable", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	// create the array adapter for the company list
	private void setArrayAdapter(final ArrayList<Company> companiesList) {
		// TODO Auto-generated method stub
		mCompanyAdapter = new CompanyAdapter(this, R.layout.company_row, companiesList);
		if(this.listView != null && mCompanyAdapter != null){
			this.listView.setAdapter(mCompanyAdapter);
		}

		if (this.listView.getVisibility() == View.VISIBLE) {
			this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					onListViewItemSelected(position, companiesList);
				}
			});
		}
	}

	// on item click listener for the grid items
	private void onListViewItemSelected(int position, ArrayList<Company> companiesList) {

		if (companiesList != null && companiesList.size() > 0) {
			Company company = companiesList.get(position);
			if (company != null) {
				Intent intent = new Intent(this, CompanyDetailsViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("company", company);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}

	// array adapter for company
	public class CompanyAdapter extends ArrayAdapter<Company>{

		Context context;
		int resoure;
		ArrayList<Company> dataList;

		CompaniesFilter filter;

		public CompanyAdapter(Context context, int resource, ArrayList<Company> dataList) {
			super(context, resource, dataList);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.resoure = resoure;
			this.dataList = dataList;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new CompaniesFilter();

			}
			return filter;
		}

		private class CompaniesFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<Company> filteredItems = new ArrayList<Company>();

					for (int i = 0, l = dataList.size(); i < l; i++) {
						Company company = dataList.get(i);
						if (company.getCompanyName().toString().toLowerCase()
								.contains(constraint))
							filteredItems.add(company);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						result.values = dataList;
						result.count = dataList.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				dataList = (ArrayList<Company>) results.values;
				notifyDataSetChanged();
				clear();
				for (int i = 0, l = dataList.size(); i < l; i++) {
					add(dataList.get(i));
					notifyDataSetInvalidated();
				}
			}
		}

		public class ViewHolder {
			private TextView companyName;
			private TextView companyOwner;
			private TextView companyStartDate;
			private TextView companyDepartments;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			ViewHolder holder = null;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				// get the view for assign data to list view
				v = vi.inflate(R.layout.company_row, null);

				holder = new ViewHolder();

				holder.companyName = (TextView) v.findViewById(R.id.company_name);
				holder.companyOwner = (TextView) v.findViewById(R.id.company_owner);
				holder.companyStartDate = (TextView) v.findViewById(R.id.company_start_date);
				holder.companyDepartments = (TextView) v.findViewById(R.id.company_departments);

				v.setTag(holder);
			} else
				holder = (ViewHolder) v.getTag();

			// get company details based on position
			Company company = this.dataList.get(position);

			if (company != null) {
				holder.companyName.setText(company.getCompanyName().toString().trim());
				holder.companyOwner.setText(company.getCompanyOwner().toString().trim());
				holder.companyStartDate.setText(company.getCompanyStartDate().toString().trim());
				holder.companyDepartments.setText(company.getCompanyDepartments().toString().trim());
			} 			
			return v;
		}
	}		
}
