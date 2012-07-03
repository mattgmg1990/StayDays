package com.weigarnes.staydays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Result extends Activity{
	
	  public static String STAYLENGTH_PERCENT_ONE = "StayLength1";
	  public static String STAYLENGTH_PERCENT_TWO = "StayLength2";
	  public static String STAYLENGTH_PERCENT_THREE = "StayLength3";
	  public static String STAYLENGTH_PERCENT_FOUR = "StayLength4";
	  
	  public static String DIAGNOSIS_EXTRA = "Diagnosis";
	  public static String SEX_EXTRA = "Sex";
	  public static String AGE_EXTRA = "AgeGroup";
	  
	  private String percentOne = "";
	  private String percentTwo = "";
	  private String percentThree = "";
	  private String percentFour = "";
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_result);
	        
	        percentOne = getIntent().getExtras().getString(STAYLENGTH_PERCENT_ONE);
	        percentTwo = getIntent().getExtras().getString(STAYLENGTH_PERCENT_TWO);
	        percentThree = getIntent().getExtras().getString(STAYLENGTH_PERCENT_THREE);
	        percentFour = getIntent().getExtras().getString(STAYLENGTH_PERCENT_FOUR);
	        
	        String diagnosis = getIntent().getExtras().getString(DIAGNOSIS_EXTRA);
	        String sex = getIntent().getExtras().getString(SEX_EXTRA);
	        String age = getIntent().getExtras().getString(AGE_EXTRA);
        
	        ((TextView) findViewById(R.id.result_diagnosis_value)).setText(diagnosis);
	        ((TextView) findViewById(R.id.result_sex_value)).setText(sex);
	        ((TextView) findViewById(R.id.result_age_value)).setText(age);
	        	        
	        ((TextView) findViewById(R.id.stay_one_percent)).setText(percentOne + "% of Patients");
	        ((TextView) findViewById(R.id.stay_two_percent)).setText(percentTwo + "% of Patients");
	        ((TextView) findViewById(R.id.stay_three_percent)).setText(percentThree + "% of Patients");
	        ((TextView) findViewById(R.id.stay_four_percent)).setText(percentFour + "% of Patients");
	    }
	  
	   public void onAboutClicked(View v){
		   Intent aboutIntent = new Intent(Result.this, About.class);
		   
		   startActivity(aboutIntent);
	   }
	
	  	
}
