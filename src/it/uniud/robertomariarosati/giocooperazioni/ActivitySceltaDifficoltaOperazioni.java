package it.uniud.robertomariarosati.giocooperazioni;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.util.Vector;

public class ActivitySceltaDifficoltaOperazioni extends Activity {

	//NOTA:::!!!!!!!!!!!!!!!!
	//CORREGGERE BUG TROVATO DA EUGENIO
	//creo un vettore di button per gestire i 5 pulsanti della scelta della difficoltà
	Vector<Button> vettore_pulsanti_difficolta = new Vector<Button>();
	//variabile che riconosce la difficoltà
	private int difficolta_scelta;
	//variabile che riconosce l'operatore
	private char operatore_scelto;
	private String modalita_scelta;
	private boolean tasti_difficolta_premuti;
	private TextView testo_principale;
	//private TextView testo_diff;
	private Button pulsante;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scelta_difficolta_operazioni);
		//testo_diff = (TextView) findViewById(R.id.testo_difficolta_scelta);
		testo_principale = (TextView) findViewById(R.id.testo_difficolta);
		pulsante= (Button) findViewById(R.id.tasto_scelta_difficolta);
		
		vettore_pulsanti_difficolta.add((Button) findViewById(R.id.ButtonDiff1));
		vettore_pulsanti_difficolta.add((Button) findViewById(R.id.ButtonDiff2));
		vettore_pulsanti_difficolta.add((Button) findViewById(R.id.ButtonDiff3));
		vettore_pulsanti_difficolta.add((Button) findViewById(R.id.ButtonDiff4));
		vettore_pulsanti_difficolta.add((Button) findViewById(R.id.ButtonDiff5));

		//IMPOSTO i listeners per questi Buttons	
		for(int j=0; j<vettore_pulsanti_difficolta.size(); j++)
		{
			vettore_pulsanti_difficolta.get(j).setOnClickListener(MioListenerDifficolta);
		}
		
		//ricevo l'intent, e ricevo l'operatore con cui voglio giocare e la MODALITA con cui voglio giocare
		if (getIntent().getExtras() != null) 
		{
			modalita_scelta = getIntent().getStringExtra("modalita_scelta");
			operatore_scelto = getIntent().getCharExtra("operatore_scelto", '.');
			System.out.println("Sono in SceltaDifficoltaOperazioni, e ho ricevuto operatore_scelto = "+operatore_scelto+" e modalita_scelta = "+modalita_scelta);
		}
		//ricevo l'intent
		
		//pongo tasti_difficolta_premuti a false
		tasti_difficolta_premuti=false;
		
		
		
		//INTENT con passaggio di informazioni
		pulsante.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				System.out.println("Ho cliccato sul pulsante, modalita_scelta = "+modalita_scelta+".");
				System.out.println("Ho cliccato sul pulsante, modalita_scelta2 = "+modalita_scelta+".");
				//se la seek bar è stata toccata
				if(tasti_difficolta_premuti)
				{
					if (modalita_scelta.equals("arcade"))
					{
						System.out.println("sono in SceltaDifficoltaOperazioni e sto cercando di chiamare l'intent verso ActivityOperazioniArcade");
						//devo passare due parametri, quindi devo creare un Bundle da passare
						Intent intent= new Intent(ActivitySceltaDifficoltaOperazioni.this, ActivityOperazioniArcade.class);
						Bundle extras_bundle = new Bundle();
						extras_bundle.putChar("operatore_scelto", operatore_scelto);
						extras_bundle.putInt("difficolta_scelta",difficolta_scelta);
						intent.putExtras(extras_bundle);
						startActivity(intent);
					}
					else if (modalita_scelta.equals("challenge"))
					{
						System.out.println("sono in SceltaDifficoltaOperazioni e sto cercando di chiamare l'intent verso ActivityOperazioniArcade");
						//devo passare due parametri, quindi devo creare un Bundle da passare
						Intent intent= new Intent(ActivitySceltaDifficoltaOperazioni.this, ActivityOperazioniChallenge.class);
						Bundle extras_bundle = new Bundle();
						extras_bundle.putChar("operatore_scelto", operatore_scelto);
						extras_bundle.putInt("difficolta_scelta",difficolta_scelta);
						intent.putExtras(extras_bundle);
						startActivity(intent);
					}
					
				}
				else
				{
					testo_principale.setText(R.string.warning_difficolta);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scelta_difficolta_operazioni, menu);
		return true;
	}
	
	//Creo una variabile di tipo View.OnClickListener da cui ricevo i click per i quattro pulsanti
	private View.OnClickListener MioListenerDifficolta = new View.OnClickListener() 
	{
		@Override
			public void onClick(final View v) 
			{
			 	//per ogni pulsante, controllo se è stato premuto
				for(int i=0; i<vettore_pulsanti_difficolta.size(); i++)
				{
				     //se è stato premuto
				     if(v==vettore_pulsanti_difficolta.get(i))
				     {
				    	
				    	//prima di impostare la difficoltà, modifico i colori dei pulsanti
				    	 
				    	//faccio diventare tutti gli altri verdi
				    	for(int j=0; j<vettore_pulsanti_difficolta.size(); j++)
				    	{
				    		vettore_pulsanti_difficolta.get(j).setBackgroundResource(R.drawable.mio_button_circolare_verde_selector);
				    	}
				    	vettore_pulsanti_difficolta.get(i).setBackgroundResource(R.drawable.mio_button_circolare_azzurro_selector);
				    	difficolta_scelta=i+1;
				    	tasti_difficolta_premuti=true;
				    	String stringa_difficolta_selezionata="";
				    	stringa_difficolta_selezionata=getString(R.string.difficolta_sceltaPt1)+difficolta_scelta+getString(R.string.difficolta_sceltaPt2);
				    	testo_principale.setText(stringa_difficolta_selezionata);
				    	//testo_principale.setText(R.string.difficolta_sceltaPt1+difficolta_scelta+R.string.difficolta_sceltaPt2);
				     }
				}
		    }
	};

}
