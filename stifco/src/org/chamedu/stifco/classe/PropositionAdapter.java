package org.chamedu.stifco.classe;

import java.util.List;

import org.chamedu.stifco.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PropositionAdapter extends BaseAdapter{

	// Une liste de proposition
	private List<Proposition> mListP;
	    	
	//Le contexte dans lequel est pr�sent notre adapter
	private Context mContext;
	    	
	//Un m�canisme pour g�rer l'affichage graphique depuis un layout XML
	private LayoutInflater mInflater;
	
	
	public PropositionAdapter(Context mContext, List<Proposition> mListP) {
		super();
		this.mListP = mListP;
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	}

	@Override
	public int getCount() {
		return mListP.size();
	}

	@Override
	public Object getItem(int position) {
		return mListP.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		Log.i("tag1","ok");
		Log.i("position", ""+position);
		Log.i("viewGroup", ""+parent);
		  //(1) : R�utilisation des layouts
		  if (convertView == null) {
			  Log.i("tag2",""+convertView);
		  	//Initialisation de notre item � partir du  layout XML "proposition_row.xml"
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.proposition_row, parent, false);
		    Log.i("tag3","ok");
		  } else {
			  Log.i("tag4","ok");
		  	layoutItem = (LinearLayout) convertView;
		  	Log.i("tag5","ok");
		  }
		  Log.i("tag6","ok");
		  //(2) : R�cup�ration des TextView de notre layout      
		  TextView tvVille = (TextView)layoutItem.findViewById(R.id.tvVille);
		  TextView tvHoraire = (TextView)layoutItem.findViewById(R.id.tvHoraire);
		        
		  //(3) : Renseignement des valeurs       
		  tvVille.setText(mListP.get(position).getVille());
		  tvHoraire.setText(mListP.get(position).getHoraire());
		  
		 
		  //On retourne l'item cr��.
		  return layoutItem;
	}

}
