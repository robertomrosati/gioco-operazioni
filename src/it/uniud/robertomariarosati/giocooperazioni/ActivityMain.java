package it.uniud.robertomariarosati.giocooperazioni;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.widget.*;
import android.content.*;  //classe che importo per gli Intent

public class ActivityMain extends Activity {

	private Button pulsante_challenge;
	private Button pulsante_arcade;
	
	//definisco un listener per il click ricevuto
	private View.OnClickListener MioClickListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==pulsante_challenge)
			{
				//se ho chiamato pulsante challenge, mando l'intent in modo da chiamare poi la modalità challenge
				Intent mio_intent= new Intent(ActivityMain.this, ActivitySceltaOperatore.class);
				String stringa_informazione_pulsante="challenge";
				mio_intent.putExtra("stringa_informazione_pulsante", stringa_informazione_pulsante);
				startActivity(mio_intent);
			}
			else if (v==pulsante_arcade)
			{
				//se ho chiamato pulsante arcade, mando l'intent in modo da chiamare poi la modalità arcade
				Intent mio_intent= new Intent(ActivityMain.this, ActivitySceltaOperatore.class);
				String stringa_informazione_pulsante="arcade";
				mio_intent.putExtra("stringa_informazione_pulsante", stringa_informazione_pulsante);
				startActivity(mio_intent);
			}
			
		}
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		System.out.println("Sono nel metodo OnCreate di ActivityMain");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//associo i due pulsanti
		pulsante_challenge = (Button) findViewById(R.id.buttonChallenge);
		pulsante_arcade = (Button) findViewById(R.id.buttonArcade);
		
		//definisco cosa succede cliccando i vari pulsanti
		
		pulsante_challenge.setOnClickListener(MioClickListener);
		pulsante_arcade.setOnClickListener(MioClickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
