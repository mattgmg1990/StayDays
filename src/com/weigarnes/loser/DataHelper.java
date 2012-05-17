package com.weigarnes.loser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;

public class DataHelper {
	String TAG = "DataHelper";
	public static int LOS_SEX_COLUMN = 0;
	public static int LOS_AGE_COLUMN = 1;
	public static int LOS_DIAGNOSIS_COLUMN = 2;

	int CODES_CODE_COLUMN = 1;
	int CODES_DIAGNOSIS_COLUMN = 2;
	
	// The main LOS data
	HashMap<String, List<LosLine>> mLosData = new HashMap<String, List<LosLine>>();
	
	// Map from Code to diagnosis
	HashMap<String,String> mDiagnosisToCode = new HashMap<String, String>();
	
	public DataHelper(InputStream losDataInputStream, InputStream diagnosisToCodeInputStream){
		loadLosData(losDataInputStream);
		loadDiagnosisToCodeData(diagnosisToCodeInputStream);
	}
	
	private void loadLosData(InputStream losDataFileName){
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(losDataFileName));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	if(nextLine.length < 6){
		    		continue;
		    	}
		        if(!mLosData.containsKey(nextLine[2])){
		        	ArrayList<LosLine> lineList = new ArrayList<LosLine>();
		        	lineList.add(new LosLine(nextLine));
		        	mLosData.put(nextLine[2], lineList);
		        }else{
		        	mLosData.get(nextLine[2]).add(new LosLine(nextLine));
		        }
		    }
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found: " + losDataFileName);
		} catch (IOException e) {
			Log.e(TAG, "IO Error reading file: " + losDataFileName);
		}
	}
	
	private void loadDiagnosisToCodeData(InputStream diagnosisToCodeFileName){
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(diagnosisToCodeFileName));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	if(nextLine.length < 2){
		    		continue;
		    	}
	        	mDiagnosisToCode.put(nextLine[1], nextLine[0]);
		    }
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found: " + diagnosisToCodeFileName);
		} catch (IOException e) {
			Log.e(TAG, "IO Error reading file: " + diagnosisToCodeFileName);
		}
	}
	
	public String[] getDiagnoses(){
		return mDiagnosisToCode.keySet().toArray(new String[0]);
	}
	
	public String[] findLosByInput(String diagnosis, String sex, String AgeGroup){
		String[] emptyResult = {"0","0","0","0","0","0","0"};
		
		if(!mDiagnosisToCode.containsKey(diagnosis)){
			return emptyResult;
		}
		for(LosLine line : mLosData.get(mDiagnosisToCode.get(diagnosis))){
			if(line.equals(diagnosis, sex, AgeGroup)){
				return line.mLine;
			}
		}
		return emptyResult;
	}
	
	private class LosLine{
		private String[] mLine;
		
		public LosLine(String[] line){
			mLine = line;
		}
		
		public boolean equals(String diagnosis, String sex, String AgeGroup){
			return (mLine[LOS_DIAGNOSIS_COLUMN].trim().equals(mDiagnosisToCode.get(diagnosis.trim())) &&
					mLine[LOS_SEX_COLUMN].trim().equals(sex.trim()) &&
					mLine[LOS_AGE_COLUMN].trim().equals(AgeGroup.trim()));
		}
	}
}
