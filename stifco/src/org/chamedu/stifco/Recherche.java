package org.chamedu.stifco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.chamedu.stifco.network.OnResultListener;
import org.chamedu.stifco.network.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Recherche extends Activity implements OnClickListener, OnItemSelectedListener  {

	Button search;
	Spinner spinner;
	private String spinnerSelected = "Janvier";
	// Varaibles pour la lecture du flux Json
	private String jsonString;
	JSONObject jsonResponse;
	JSONArray arrayJson;
	AutoCompleteTextView tvGareAuto;
	ArrayList<String> items = new ArrayList<String>();
	
	// Array of choices
	String Month[] = {"Janvier","Fevrier","Mars","Avril","Mai", "Juin","Juillet","Aout","Septembre","Octobre", "Novembre","Decembre"};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rechercher);

		search = (Button)findViewById(R.id.search);
		search.setOnClickListener(this);
		// Traitement du textView en autocompl�tion � partir de la source Json
		jsonString = lireJSON();

		try {
			jsonResponse = new JSONObject(jsonString);
			// Cr�ation du tableau g�n�ral �partir d'un JSONObject
			JSONArray jsonArray = jsonResponse.getJSONArray("gares");

			// Pour chaque �l�ment du tableau
			for (int i = 0; i < jsonArray.length(); i++) {

				// Cr�ation d'un tableau �l�ment � partir d'un JSONObject
				JSONObject jsonObj = jsonArray.getJSONObject(i);

				// R�cup�ration �partir d'un JSONObject nomm�
				JSONObject fields  = jsonObj.getJSONObject("fields");

				// R�cup�ration de l'item qui nous int�resse
				String nom = fields.getString("nom_de_la_gare");

				// Ajout dans l'ArrayList
				items.add(nom);		
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, items);
			tvGareAuto = (AutoCompleteTextView)findViewById(R.id.gare);
			tvGareAuto.setAdapter(adapter);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Selection of the spinner
		spinner = (Spinner) findViewById(R.id.dateSpinner);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Month);
		//spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setOnItemSelectedListener(this);

	}	

	
	public void onClick(View v) {
		if ( v == search ) {
			Toast.makeText(Recherche.this, "Recherche en cours", Toast.LENGTH_LONG).show();
			if(!tvGareAuto.getText().equals("")){
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
				nameValuePairs.add(new BasicNameValuePair("gare",""+tvGareAuto.getText()));
				nameValuePairs.add(new BasicNameValuePair("mois",spinnerSelected.toLowerCase()));
	
//				Log.i("mois", ""+tvGareAuto.getText());
//				Log.i("gare", ""+spinnerSelected.toLowerCase());
	
				

				try {				
					RestClient.doPost("/recherche.php", nameValuePairs, new OnResultListener() {					
						@Override
						public void onResult(String json) {
							Log.i("resultat", ""+json);
							if ( !json.equals("recherche_vide")) {
								Intent iResultatRecherche = new Intent(getBaseContext(), ResultRecherche.class);
								iResultatRecherche.putExtra("liste", json);
								startActivityForResult( iResultatRecherche,10 );
							} else {
								Toast.makeText(Recherche.this, "Aucune proposition n\'a �t� faite.", Toast.LENGTH_LONG).show();
							}					
						}
					});
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (HttpException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

	public String lireJSON() {
		InputStream is = getResources().openRawResource(R.raw.gares);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writer.toString();
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		spinnerSelected = Month[arg2];
		//Toast.makeText(Recherche.this, ""+spinnerSelected, Toast.LENGTH_LONG).show();
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
