package it.uniud.robertomariarosati.giocooperazioni;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ActivitySceltaOperatore extends Activity {

	//Creo alcune variabili di classe
 	
	
	private Button pulsante_add;
	private Button pulsante_sott;
	private Button pulsante_molt;
	private Button pulsante_div;
	private Button pulsante_equaz;
	private Button pulsante_generico;
	private char operatore_scelto;
	private boolean ho_cliccato_un_button;
	private String modalita_scelta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scelta_operatore);
		
		System.out.println("Sono entrato nell'activity SceltaOperatore");
		
		//ricevo l'intent, e ricevo la modalità con cui voglio giocare
		if (getIntent().getExtras() != null) 
		{
			modalita_scelta = getIntent().getStringExtra("stringa_informazione_pulsante");
			System.out.println("Sono in SceltaDifficoltaOperazioni, e ho ricevuto modalita_scelta = "+modalita_scelta);
		}
				
		
		//associo pulsanti nella classe java e pulsanti nel file xml
		pulsante_add = (Button) findViewById(R.id.buttonAdd);
		pulsante_sott = (Button) findViewById(R.id.buttonSott);
		pulsante_molt = (Button) findViewById(R.id.buttonMolt);
		pulsante_div = (Button) findViewById(R.id.buttonDiv);
		pulsante_equaz = (Button) findViewById(R.id.buttonEquaz);
		pulsante_generico = (Button) findViewById(R.id.buttonGenerico);
		
		pulsante_add.setOnClickListener(MioOnClickListener);
		pulsante_sott.setOnClickListener(MioOnClickListener);
		pulsante_molt.setOnClickListener(MioOnClickListener);
		pulsante_div.setOnClickListener(MioOnClickListener);
		pulsante_equaz.setOnClickListener(MioOnClickListener);
		pulsante_generico.setOnClickListener(MioOnClickListener);
		
		//impongo la variabile ho_cliccato_un_button a false
		ho_cliccato_un_button=false;
	}
	
	//se clicco il pulsante attivo l'activity per andare a SceltaDifficoltaOperazioni
	private View.OnClickListener MioOnClickListener = new View.OnClickListener() 
	{
		//in base al tasto che ho cliccato passo il parametro dell'operatore all'activity della scelta difficoltà
		@Override
		public void onClick(View v)
		{
			if(v==pulsante_add)
			{
				 operatore_scelto='+';
				 ho_cliccato_un_button=true;
				 System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_add");
			}
			else if(v==pulsante_sott)
			{
				operatore_scelto='-';
				ho_cliccato_un_button=true;
				System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_sott");

			}
			else if(v==pulsante_molt)
			{
				operatore_scelto='*';
				ho_cliccato_un_button=true;
				System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_molt");
			}
			else if(v==pulsante_div)
			{
				operatore_scelto='/';
				ho_cliccato_un_button=true;
				System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_div");
			}
			else if(v==pulsante_equaz)
			{
				operatore_scelto='=';
				ho_cliccato_un_button=true;
				System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_equaz");
			}
			else if(v==pulsante_generico)
			{
				operatore_scelto='.';
				ho_cliccato_un_button=true;
				System.out.println("Sono in Scelta_Operatore e ho cliccato pulsante_generico");
			}
			
			//Solo se il click è stato effettuato su uno dei quattro tasto chiamo l'intent
			if(ho_cliccato_un_button)
			{
				Intent intent = new Intent(ActivitySceltaOperatore.this, ActivitySceltaDifficoltaOperazioni.class);
				Bundle extras_bundle = new Bundle();
				extras_bundle.putString("modalita_scelta", modalita_scelta);
				extras_bundle.putChar("operatore_scelto",operatore_scelto);
				intent.putExtras(extras_bundle);
				startActivity(intent);
			}
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scelta_operatore, menu);
		return true;
	}

}
