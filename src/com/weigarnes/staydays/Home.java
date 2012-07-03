package com.weigarnes.staydays;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;

public class Home extends Activity {
	
    private DataHelper mDataHelper;
    
    public AlertDialog mNoDataDialog;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initializeAgeSpinner();
        
        InputStream losData = getResources().openRawResource(R.raw.los_data);
		InputStream codeData = getResources().openRawResource(R.raw.diagnosis_desc);
		
		mDataHelper = new DataHelper(losData, codeData);
		
		initializeAutoComplete();

    }
    
    private void initializeAutoComplete(){
    	AutoCompleteTextView diagnosisInput = (AutoCompleteTextView) findViewById(R.id.autocomplete_diagnosis);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, mDataHelper.getDiagnoses());
        diagnosisInput.setAdapter(adapter);
    }
    
    // Initialize the spinner with the resource for age groups
    public void initializeAgeSpinner(){
    	Spinner spinner = (Spinner) findViewById(R.id.age_group);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.age_groups_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    
   public void onGenderClicked(View v){
	   ImageButton thisButton = (ImageButton) v;
	   
	   // This is the male button
	   if((thisButton.getId() == R.id.male_button) && !thisButton.isSelected()){
		   // Deselect the femaleButton
		   ImageButton femaleButton = (ImageButton) findViewById(R.id.female_button);
		   femaleButton.setSelected(false);
		   femaleButton.setBackgroundColor(Color.TRANSPARENT);
		   
		   // Select the male button
		   thisButton.setSelected(true);
		   thisButton.setBackgroundResource(R.drawable.gender_button_bg);
	   }
	   
	   // this is the female button
	   if((thisButton.getId() == R.id.female_button) && !thisButton.isSelected()){
		   // Deselect the maleButton
		   ImageButton maleButton = (ImageButton) findViewById(R.id.male_button);
		   maleButton.setSelected(false);
		   maleButton.setBackgroundColor(Color.TRANSPARENT);
		   
		   // Select the female button
		   thisButton.setSelected(true);
		   thisButton.setBackgroundResource(R.drawable.gender_button_bg);
	   }
   }
   

   public void showDialog(String title, String message) {
           if( mNoDataDialog != null && mNoDataDialog.isShowing() ) return;

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle(title);
           builder.setMessage(message);
           builder.setPositiveButton("OK", new OnClickListener() {
                   public void onClick(DialogInterface dialog, int arg1) {
                       dialog.dismiss();
                   }});
           builder.setCancelable(false);
           mNoDataDialog = builder.create();
           mNoDataDialog.show();
   }

   
   public void onCalculateClicked(View v){
	   String diagnosis = ((AutoCompleteTextView) findViewById(R.id.autocomplete_diagnosis)).getText().toString();
	   String sex = "2";
	   String ageGroup = Integer.toString(((Spinner) findViewById(R.id.age_group)).getSelectedItemPosition() + 1);
	   
	   if(findViewById(R.id.male_button).isSelected()){
		   sex = "1";
	   }	   
	   
	   String[] result;
	   try {
		   result = mDataHelper.findLosByInput(diagnosis, sex, ageGroup);
		   
		   // Add a string representation of the sex of the patient
		   String readableSex = "Female";
		   if(sex.equals("1")){
			   readableSex = "Male";
		   }
		   
		   // Calculate percentage values for each category
		   float categoryOneVal = Float.parseFloat(result[3]) * 100.0f;
		   float categoryTwoVal = Float.parseFloat(result[4]) * 100.0f;
		   float categoryThreeVal = Float.parseFloat(result[5]) * 100.0f;
		   float categoryFourVal = Float.parseFloat(result[6]) * 100.0f;
		   
		   if((categoryOneVal == 0) && (categoryTwoVal == 0) && (categoryThreeVal == 0) && (categoryFourVal == 0)){
			   showDialog("No Data Stored", "While this diagnosis does exist in the database, there is no length of stay data stored for the selected patient gender and age group.");
		   } else {
			   Intent resultIntent = new Intent(Home.this, Result.class);
			   
			   resultIntent.putExtra(Result.STAYLENGTH_PERCENT_ONE, String.format("%.2f", categoryOneVal));
			   resultIntent.putExtra(Result.STAYLENGTH_PERCENT_TWO, String.format("%.2f", categoryTwoVal));
			   resultIntent.putExtra(Result.STAYLENGTH_PERCENT_THREE, String.format("%.2f", categoryThreeVal));
			   resultIntent.putExtra(Result.STAYLENGTH_PERCENT_FOUR, String.format("%.2f", categoryFourVal));
			   resultIntent.putExtra(Result.DIAGNOSIS_EXTRA, diagnosis);
			   resultIntent.putExtra(Result.SEX_EXTRA, readableSex);
			   resultIntent.putExtra(Result.AGE_EXTRA, ((Spinner) findViewById(R.id.age_group)).getSelectedItem().toString());
				
			   startActivity(resultIntent);
		   }
	   } catch (Exception e) {
		   showDialog("Entered Diagnosis Not Present", "The diagnosis entered was not found in the data stored. Please make sure to choose only a diagnosis from the autocomplete list that appears as you enter text. All other inputs will not be found.");
	   }
	   
	   
   }
}