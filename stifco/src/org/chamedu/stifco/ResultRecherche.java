package org.chamedu.stifco;

import java.util.ArrayList;

import org.chamedu.stifco.classe.Proposition;
import org.chamedu.stifco.classe.PropositionAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class ResultRecherche extends Activity {
	
	private String json;
	JSONObject jsonResponse;
	JSONArray arrayJson;
	ArrayList<Proposition> items = new ArrayList<Proposition>();
	
	ListView list;
	PropositionAdapter adapter = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_recherche);
		
		list = (ListView)findViewById(R.id.lvPropositions);
		json = (String) getIntent().getExtras().get("liste");

		try {
			jsonResponse = new JSONObject(json);
			// Cr�ation du tableau g�n�ral �partir d'un JSONObject
			JSONArray jsonArray = jsonResponse.getJSONArray("propositions");
			Proposition currentPropo = null;
			
			// Pour chaque �l�ment du tableau
			for (int i = 0; i < jsonArray.length(); i++) {
				currentPropo = new Proposition();
				// Cr�ation d'un tableau �l�ment � partir d'un JSONObject
				JSONObject jsonObj = jsonArray.getJSONObject(i);

				// R�cup�ration �partir d'un JSONObject nomm�
				//JSONObject fields  = jsonObj.getJSONObject("fields");

				// R�cup�ration de l'item qui nous int�resse
				//String nom = fields.getString("nom_de_la_gare");
				
				currentPropo.setHoraire(jsonObj.getString("id"));
				currentPropo.setVille(jsonObj.getString("ville"));
				currentPropo.setLieu(jsonObj.getString("lieu"));

				// Ajout dans l'ArrayList
				items.add(currentPropo);	
				//Log.i("item "+i, ""+horaire);
			}
			adapter = new PropositionAdapter(this, items);
			//list.setAdapter(new ArrayAdapter<Proposition>(this,android.R.layout.simple_list_item_1,items));
			list.setAdapter(adapter);
		}catch (JSONException e) {
			e.printStackTrace();
		}

		//}

		
	}	

	
	
	//M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du t�l�phone
    public boolean onCreateOptionsMenu(Menu menu) {
 
        //Cr�ation d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
        MenuInflater inflater = getMenuInflater();
        //Instanciation du menu XML sp�cifier en un objet Menu
        inflater.inflate(R.layout.menu, menu);
 
        //Il n'est pas possible de modifier l'ic�ne d'en-t�te du sous menu via le fichier XML on le fait donc en JAVA
    	//menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);
 
        return true;
     }
 
       //M�thode qui se d�clenchera au clic sur un item
      public boolean onOptionsItemSelected(MenuItem item) {
         //On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une action
         switch (item.getItemId()) {
            case R.id.retour:
               //Toast.makeText(Tutoriel10_Android.this, "Option", Toast.LENGTH_SHORT).show();
            	Intent iMain = new Intent(getBaseContext(), MainActivity.class);
				startActivityForResult( iMain,10 );
               break;
         }
         return false;}
	
}
