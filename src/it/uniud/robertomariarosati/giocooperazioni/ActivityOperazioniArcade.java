package it.uniud.robertomariarosati.giocooperazioni;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityOperazioniArcade extends SuperClasseOperazioni 
{
	
	//dichiaro la progress bar
	private ProgressBar BarraProgresso; 
	private int millisecondi_a_disposizione;
	private final int fondoscala_progress_bar = 500;
	private int indice_risposta_corretta;
	//variabile del timer
	private Timer timer;
	private Timer timer_da_mostrare; //timer solo per il println
	int counter;  //variabile che conta il tempo
	//variabili per la raccolta punti e per le statistiche
	private int punti_totali;
	private int risposte_corrette;
	private int tempo_totale;
	private int tempo_medio;
	private int varianza;
	private int somma_quadrati_tempi;  
	private Operazione op1; //operazione che riciclo nella classe
	private Equazione eq1;
	private Button pulsante_avanzamento, pulsante_avanzamento2, pulsante_conclusione;
	private EditText inserimento_testo;
	private TextView statistiche_view; 
	private String nome_giocatore_temp;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//println di debug
		System.out.println("Sono entrato nell'activity OperazioniArcade");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operazioni_arcade);
		//println di debug
		System.out.println("Ho fatto il setContentView nell'activity OperazioniArcade" );
		
		//ricevo l'intent
		if (getIntent().getExtras() != null) {
		    //String value = extras.getString("difficolta");
			setDifficolta_scelta(getIntent().getIntExtra("difficolta_scelta", 1));
			setOperatore_scelto(getIntent().getCharExtra("operatore_scelto",'.'));
			System.out.println("Sono in OperazioniArcade e ho ricevuto i seguenti dati dal Bundle:");
			System.out.println("difficolta_scelta = "+getDifficolta_scelta());
			System.out.println("operatore_scelto = "+getOperatore_scelto());
		}
		
		//Riferimento al layout
		setLayout_operazioni((RelativeLayout) findViewById(R.id.activity_operazioni_arcade));
		setLinearLayout_interno((LinearLayout) findViewById(R.id.LinearLayout_interno_a_activity_operazioni));
		setLinearLayout_molto_interno((LinearLayout) findViewById(R.id.LinearLayout_molto_interno_a_activity_operazioni));
		//imposto il TextView uguale all'id del widget testo_operazione
		setTesto_op((TextView) findViewById(R.id.testo_operazione));
		
		//aggiungo i widget Button che devono contenere i risultati al vettore
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato1));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato2));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato3));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato4));
		
		//riferimento alla progress bar
		BarraProgresso = (ProgressBar) findViewById(R.id.ProgressBarTimer);
		//imposto il massimo di 5 secondi (conto fondoscala_progress_bar volte 10 ms con timer_da_mostrare)
		BarraProgresso.setMax(fondoscala_progress_bar);
		
		//setto i ClickListener per i tasti
		getVettore_pulsanti_ris().get(0).setOnClickListener(MioOnClickListener);
		getVettore_pulsanti_ris().get(1).setOnClickListener(MioOnClickListener);
		getVettore_pulsanti_ris().get(2).setOnClickListener(MioOnClickListener);
		getVettore_pulsanti_ris().get(3).setOnClickListener(MioOnClickListener);
		
		setSfondo1(getResources().getDrawable(R.drawable.sfondo_gioco_operazioni1));
		setSfondo2(getResources().getDrawable(R.drawable.sfondo_gioco_operazioni2));
		setSfondo_1_impostato(false);
		setSfondo1(getResources().getDrawable(R.drawable.sfondo_gioco_operazioni1));
		setSfondo2(getResources().getDrawable(R.drawable.sfondo_gioco_operazioni2));
		
		
		//inziializzo a zero le variabili per le statistiche
		punti_totali=0;
		risposte_corrette=0;
		tempo_totale=0;
		tempo_medio=0;
		varianza=0;
		somma_quadrati_tempi=0;
		
		//creo una op/eq casuale
		//Devo vedere che operatore ho ricevuto
		//se ho scelto un'equazione
		
		if(getOperatore_scelto()!='=')
		{
			millisecondi_a_disposizione=5000;
			op1 = new Operazione(getOperatore_scelto(), getDifficolta_scelta());
			indice_risposta_corretta=InserisciOperazioneArcade(op1, getTesto_op(), getVettore_pulsanti_ris());
		}
		else if (getOperatore_scelto()=='=')
		{
			millisecondi_a_disposizione=30000;
			eq1 = new Equazione();
			indice_risposta_corretta=InserisciEquazioneArcade(eq1, getTesto_op(), getVettore_pulsanti_ris());
		}
		
		System.out.println("Tutto l'onCreate di ActivityOperazioniArcade è andato a buon fine");
		
		
		
	}
	
	//METODO PER PERMETTERE LA ROTAZIONE DELLO SCHERMO SENZA PERDITA DI DATI: WORK IN PROGRESS: RISOLTO CON android:ConfigChanges
	
	/*
	protected void onSaveInstanceState(Bundle icicle) 
	{	
		icicle.putInt("difficolta_scelta", getDifficolta_scelta());
		icicle.putInt("operatore_scelto", getOperatore_scelto());
		icicle.putInt("indice_risposta_corretta",indice_risposta_corretta);
		//variabile del timer
		icicle.putInt("counter",counter);  //variabile che conta il tempo
		//variabili per la raccolta punti e per le statistiche
		icicle.putInt("punti_totali",punti_totali);
		icicle.putInt("risposte_corrette",risposte_corrette);
		icicle.putInt("tempo_totale",tempo_totale);
		icicle.putDouble("tempo_medio",tempo_medio);
		icicle.putDouble("varianza",varianza);
		icicle.putDouble("somma_quadrati_tempi",somma_quadrati_tempi);  
		
		//vedo se memorizzare l'equazione o l'operazione
		if(getOperatore_scelto()=='=')
		{
			icicle.putInt("risultato_corretto", eq1.ValoreX());
			icicle.putIntArray("vettore risultati", eq1.getRisultati());
			icicle.putIntArray("vettore parametri", eq1.getVettore_parametri());
		}
		else
		{
		}
		
		op1; //operazione che riciclo nella classe
		eq1;
	    icicle.put
		icicle.putLong("param", value);
		super.onSaveInstanceState(icicle);
		  
	}
	*/
	
	//Creo una variabile di tipo View.OnClickListener da cui ricevo i click per i quattro pulsanti
	private View.OnClickListener MioOnClickListener = new View.OnClickListener() 
	{
	    @Override
	    public void onClick(final View v) 
	    {
		   	

		    for(int i=0; i<getVettore_pulsanti_ris().size(); i++)
		    {
		    	//uso quattro if anziché la switch perché non mi lascia usare v (è una View) nella switch.
			   	//posso mettere solo tipi interi nella switch
		    	//SE HO CLICCATO IL TASTO CON LA RISPSOTA CORRETTA
		    	if(v==getVettore_pulsanti_ris().get(indice_risposta_corretta))
		    	{
		    		//prima di interrompere il timer, aggiorno le statistiche			    		
			    	int tempo_questa_risposta=(int)((double)(counter)*millisecondi_a_disposizione/fondoscala_progress_bar);
			    		
			    	System.out.println("counter = "+counter);
			    	System.out.println("tempo_questa_risposta = "+tempo_questa_risposta);
			   		
			   		//ho ricalcolato tutte le statistiche
			   		//nei punti totali includo il molitplicatore ogni 5 risposte
			   		punti_totali+=Math.pow(2,(risposte_corrette/6));
		    		risposte_corrette++;
		    		tempo_totale+=tempo_questa_risposta;
		    		tempo_medio=tempo_totale/risposte_corrette;
			    	somma_quadrati_tempi+=Math.pow(tempo_questa_risposta,2);
			    	varianza=somma_quadrati_tempi/risposte_corrette-(int)Math.pow(tempo_medio,2);
			   		 
			   		
			   		//se ho cliccato sulla risposta corretta devo:
			   		//interrompere il timer
		    		//interrompere il timer_da_mostrare
		    		//riazzerare la barra (dovrebbe farlo da solo)
		    		//NOTA: È importante farlo prima di invocare InserisciOperazioneArcade	    		
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							//DEBUG: stampo lo scheduled execution time prima del purge								timer.cancel();
							timer.cancel();
							timer.purge();
							timer_da_mostrare.cancel();
							timer_da_mostrare.purge();
							System.out.println("Ho eseguito timer.purge() e timer.cancel() poiché è stata cliccata la risposta esatta");
						}
					});
		    		
					
						
					if(getOperatore_scelto()!='=')
					{
						op1 = new Operazione(getOperatore_scelto(), getDifficolta_scelta());
						indice_risposta_corretta=InserisciOperazioneArcade(op1, getTesto_op(), getVettore_pulsanti_ris());
					}
					else if (getOperatore_scelto()=='=')
					{
						eq1 = new Equazione();
						indice_risposta_corretta=InserisciEquazioneArcade(eq1, getTesto_op(), getVettore_pulsanti_ris());
					}
					System.out.println("L'utente ha cliccato sulla risposta corretta: aggiorno la risposta");
					
					break; //se ho cliccato sulla risposta esatta devo interrmpere il ciclo
		    	}  //ALTRIMENTI
		    	else if (v==getVettore_pulsanti_ris().get(i))
		    	{
		    		System.out.println("Ho cliccato sulla risposta sbagliata!");
			    	ChiamaSconfitta();
			   		break;
			   	}
		    } 	
		}
	};

	void AvviaTimers()
	{
		System.out.println("E' stata chiamata la funzione AvviaTimers nella classe ActivityOperazioni");
		//faccio partire il Timer, e do tot secondi all'utente per rispondere
		timer = new Timer();
		timer_da_mostrare = new Timer();
		timer.schedule(new SconfittaTask(), millisecondi_a_disposizione);
		counter=0;
		//NOTA: timer da mostrare mi serve anche a regolare la barra (ogni 2000/500 ms la incremento)
		timer_da_mostrare.scheduleAtFixedRate(new TimerTask()
		{
				
			public void run()
			{
				System.out.println("Sono trascorsi "+((double)(counter++)*millisecondi_a_disposizione/fondoscala_progress_bar)/1000+"secondi");
				BarraProgresso.setProgress(counter);
			}
		}
		,0,millisecondi_a_disposizione/fondoscala_progress_bar);
		System.out.println("Sto per uscire da AvviaTimers");
	}
	
	void RiavviaTimers()    //Riavvio il timers dal valore di counter
	{
		System.out.println("E' stata chiamata la funzione RiavviaTimers nella classe ActivityOperazioni");
		//faccio partire il Timer, e do tot secondi all'utente per rispondere
		timer = new Timer();
		timer_da_mostrare = new Timer();
		timer.schedule(new SconfittaTask(), millisecondi_a_disposizione-(int)((counter++)*millisecondi_a_disposizione/fondoscala_progress_bar));
		counter=0;
		//NOTA: timer da mostrare mi serve anche a regolare la barra (ogni 2000/500 ms la incremento)
		timer_da_mostrare.scheduleAtFixedRate(new TimerTask()
		{
				
			public void run()
			{
				System.out.println("Sono trascorsi "+((double)(counter++)*millisecondi_a_disposizione/fondoscala_progress_bar)/1000+"secondi");
				BarraProgresso.setProgress(counter);
			}
		}
		,0,millisecondi_a_disposizione/fondoscala_progress_bar);
		System.out.println("Sto per uscire da RiavviaTimers");
	}
	
	//METODO PER LA SCONFITTA
	void ChiamaSconfitta()
	{
		System.out.println("ATTENZIONE:E' stata chiamata ChiamaSconfitta()");
		//faccio scomparire il layout interno a activity_operazioni
		BarraProgresso.post(new Runnable() 
		{
			public void run() 
			{
				getLinearLayout_interno().removeView(BarraProgresso);
			}
		});
			
		getLinearLayout_molto_interno().post(new Runnable() 
		{
			public void run() 
			{
				getLinearLayout_interno().removeView(getLinearLayout_molto_interno());
			}
		});
		//svuoto i pulsanti
		for(int j=0; j<getVettore_pulsanti_ris().size(); j++)
		{	
			//println di debug
			System.out.println("Sto rimuovendo i pulsanti, j = "+j);
			//elimino il pulsante dal layout
			
			final int k=j;
				
			getVettore_pulsanti_ris().get(k).post(new Runnable() 
			{
				public void run() 
				{
					getLinearLayout_interno().removeView(getVettore_pulsanti_ris().get(k));
					}
			});
	
		}
			
		System.out.println("Ho rimosso le Views");
			
			//new Handler().post(new Runnable() {
		    //    public void run() {
		    //     	layout_operazioni.removeView(vettore_pulsanti_ris.get(j));
		    //     }
		    // });
			//println di debug
			//System.out.println("Sto per settare il LinearLayout layout_operazioni");
			//setContentView(layout_operazioni);
			//println di debug
			//System.out.println("Ho settato il LinearLayout layout_operazioni");
			//pongo "indice_rispsta_corretta" a un valore impossibile
		indice_risposta_corretta=-1;
			
			//creo la stringa in cui ficco tutte le statistiche
		String stringa_statistiche="";
			
		stringa_statistiche+=getString(R.string.stringa_statisticaPt1)+' '+risposte_corrette+' '+getString(R.string.stringa_statisticaPt2)+' '+((double)tempo_totale/1000)+"s\n";
		stringa_statistiche+=getString(R.string.stringa_statisticaPt3)+' '+((double)tempo_medio/1000)+"s\n";
		stringa_statistiche+=getString(R.string.stringa_statisticaPt4)+' '+((double)varianza/1000000)+"s²\n";
		stringa_statistiche+=getString(R.string.stringa_statisticaPt5)+' '+Math.sqrt((double)varianza)/1000+"s\n";
			
			//aggiorno la views per i punti e ne creo una per le statistiche
		getTesto_op().setText(punti_totali+' '+getString(R.string.punti));
			
		statistiche_view = new TextView(this);
			
		statistiche_view.setText(stringa_statistiche);
		statistiche_view.setTextSize(20);
			
		pulsante_avanzamento = new Button(this);
			
		pulsante_avanzamento.setText(R.string.continua);
		pulsante_avanzamento.setTextSize(35);
		pulsante_avanzamento.setBackgroundResource(R.drawable.mio_button_selector);
			
		getLinearLayout_interno().addView(statistiche_view);
		getLinearLayout_interno().addView(pulsante_avanzamento);
			
		inserimento_testo = new EditText(this);
		inserimento_testo.setInputType(InputType.TYPE_CLASS_TEXT);
		inserimento_testo.setBackgroundResource(R.drawable.mio_edit_text);
		inserimento_testo.setImeOptions(EditorInfo.IME_ACTION_DONE);
		//VOGLIO RICORDARE L'ULTIMO NOME INSERITO, SE PRESENTE
		//Leggo i dati dal file e li metto nel vettore di stringhe
		try
		{
		   InputStream in = openFileInput("Nome_ultimo_giocatore.txt");
		       if(in!= null)
		       {
		          InputStreamReader tmp = new InputStreamReader(in);
		          BufferedReader reader = new BufferedReader(tmp);
		          //leggo la stringa
		          while((nome_giocatore_temp=reader.readLine())!=null)
		          {
		        	  //lo imposto come campo dell'EditText
		        	  inserimento_testo.setText(nome_giocatore_temp);
		        	  System.out.println("Ho letto il nome dell'ultimo giocatore, che e' "+nome_giocatore_temp);
		          }
		          in.close();
		       }
		    }
		    catch (java.io.FileNotFoundException e)
		    {
		       //ok, non l'abbiamo ancora creato
		    }
		    catch (Throwable t)
		    {
		       Toast.makeText(this, "Eccezione: " + t.toString(), 2000);
		    }
			
			
			pulsante_avanzamento2 = new Button(this);
			pulsante_avanzamento2.setText(R.string.salva);
			pulsante_avanzamento2.setTextSize(35);
			pulsante_avanzamento2.setBackgroundResource(R.drawable.mio_button_selector);
			
			pulsante_conclusione = new Button(this);
			pulsante_conclusione.setText(R.string.torna_menu_principale);
			pulsante_conclusione.setTextSize(35);
			pulsante_conclusione.setBackgroundResource(R.drawable.mio_button_selector);
			
			
			pulsante_avanzamento.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//ripulisco le views
					statistiche_view.setText(getString(R.string.complimenti_punteggioPt1)+' '+punti_totali+' '+getString(R.string.complimenti_punteggioPt2));
					getLinearLayout_interno().removeView(pulsante_avanzamento);
					//visualizzo un campo per l'inserimento di un nome, e un pulsante per schiacciare ok
					getLinearLayout_interno().addView(inserimento_testo);
					getLinearLayout_interno().addView(pulsante_avanzamento2);
				}
				
			});
			
			pulsante_avanzamento2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//quando il pulsante_avanzamento2 viene cliccato, devo salvare il punteggio, mostrare la lista dei punteggi e permettere di uscire
					
					//faccio scomparire innanzitutto la tastiera
					InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(inserimento_testo.getWindowToken(), 0);
					
					//creo una stringa temporanea
					String nome_giocatore = new String();
					//Salvo i punteggi sul file e sulla stringa!
					nome_giocatore = SalvaPunteggiSuFile(inserimento_testo.getText().toString(), punti_totali);
					//rimuovo tutte le views, e riutilizzo statistiche view per stampare la lista dei punti
					nome_giocatore_temp=inserimento_testo.getText().toString();
					getLinearLayout_interno().removeView(pulsante_avanzamento2);
					getLinearLayout_interno().removeView(inserimento_testo);
					
					//creo una stringa che mi servira per stampare la modalita di gioco
					String stringa_operatore = "";
					switch(getOperatore_scelto())
				      {
				      	case '+':
				      		stringa_operatore=getString(R.string.stringa_operatore_ADD);
				      		break;
				      	case '-':
				      		stringa_operatore=getString(R.string.stringa_operatore_SOT);
				      		break;
				      	case '*':
				      		stringa_operatore=getString(R.string.stringa_operatore_MOL);
				      		break;
				      	case '/':
				      		stringa_operatore=getString(R.string.stringa_operatore_DIV);
				      		break;
				      	case '.':
				      		stringa_operatore=getString(R.string.stringa_operatore_MIX);
				      		break;
				      	case '=':
				      		stringa_operatore=getString(R.string.stringa_operatore_EQU);
				      		break;
				      }
				      
					
					((TextView) findViewById(R.id.testo_operazione)).setText(stringa_operatore+" LIV "+getDifficolta_scelta());
					((TextView) findViewById(R.id.testo_operazione)).setTextSize(25);
					statistiche_view.setText(nome_giocatore);
					
					
					//Mi salvo sul file il nome dell'ultimo giocatore
					//In modo da ricordarlo per la prossima volta
					try
				    {
						PrintWriter file_out = new PrintWriter(openFileOutput("Nome_ultimo_giocatore.txt",0));
						file_out.println(nome_giocatore_temp);
						System.out.println("Ho salvato il nome dell'ultimo giocatore, che e' "+nome_giocatore_temp);
						file_out.close();
				    }
				    catch (Throwable t)
				    {
				    	System.out.println("Non sono riuscito a stampare i punteggi nel file di uscita, sono nel blocco catch");
				        //Toast.makeText(this, "Eccezione: " + t.toString(), 2000).show();
				    }
					
					getLinearLayout_interno().addView(pulsante_conclusione);
					
					
				}
				
			});
			
			
			pulsante_conclusione.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//quando il pulsante_conclusione viene cliccato, chiamo un intent al menu principale
					Intent mio_intent = new Intent(ActivityOperazioniArcade.this, ActivityMain.class);
					startActivity(mio_intent);

				}
				
			});
			
			
			//faccio cancellare i timer!
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					timer.cancel();
					timer_da_mostrare.cancel();
					System.out.println("Ho eseguito timer.cancel() in ChiamaSconfitta");
				}
			});
			

		}
		
		//metodo inserisci operazioni interno (è uguale al "padre", in più fa partire anche i timer
		int InserisciOperazioneArcade(Operazione op, TextView testo_op, Vector<Button> vettore_pulsanti)
		{
			int indice_ris_corretto_da_restituire;
			indice_ris_corretto_da_restituire = InserisciOperazione(op, testo_op, vettore_pulsanti);
			//avvio i timer
			System.out.println("Sono in InserisciOperazioneArcade e sto per chiamare AvviaTimers");
			AvviaTimers();
			System.out.println("Sono in InserisciOperazioneArcade ed ho chiamato AvviaTimers correttamente");
			return indice_ris_corretto_da_restituire;
		}
		
		//metodo inserisci operazioni interno (è uguale al "padre", in più fa partire anche i timer
		int InserisciEquazioneArcade(Equazione eq, TextView testo_op, Vector<Button> vettore_pulsanti)
		{
			int indice_ris_corretto_da_restituire;
			indice_ris_corretto_da_restituire = InserisciEquazione(eq, testo_op, vettore_pulsanti);
			//avvio i timer
			System.out.println("Sono in InserisciEquazioneArcade e sto per chiamare AvviaTimers");
			AvviaTimers();
			
//			//quando inserisco una nuova opreazione cambio anche lo sfondo
//			runOnUiThread(new Runnable() {
//			     @Override
//				 @SuppressLint("NewApi")
//				 @SuppressWarnings("deprecation")
//			     public void run() 
//			     {
//			 			System.out.println("Sono nel metodo che imposta gli sfondi nella ClasseOperazioniArcade");
//						if(!isSfondo_1_impostato())
//						{
//							int sdk = android.os.Build.VERSION.SDK_INT;
//							if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) 
//							{
//							    getLayout_operazioni().setBackgroundDrawable(getSfondo1());
//							} 
//							else 
//							{
//								getLayout_operazioni().setBackground(getSfondo1());
//							}
//							System.out.println("Ho cambiato da sfondo2 a sfondo1");
//							setSfondo_1_impostato(true);
//						}
//						else
//						{
//							int sdk = android.os.Build.VERSION.SDK_INT;
//							if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) 
//							{
//							    getLayout_operazioni().setBackgroundDrawable(getSfondo2());
//							} 
//							else 
//							{
//								getLayout_operazioni().setBackground(getSfondo2());
//							}
//							setSfondo_1_impostato(false);
//							System.out.println("Ho cambiato da sfondo1 a sfondo2");
//						}
//
//			     }
//			});
			
			return indice_ris_corretto_da_restituire;
			
			
		}
		

		//CLASSE INTERNA ALLA CLASSE OPERAZIONI CHE SERVE SOLO A CREARE IL TIMER TASK
		//DA ESEGUIRE QUANDO VIENE PERSA LA PARTITA
		class SconfittaTask extends TimerTask
		{
			public void run()
			{
				//quando viene chiamato questo timer task assegno la sconfitta!
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						ChiamaSconfitta();
						System.out.println("Sono passati "+millisecondi_a_disposizione/1000+" secondi, sono entrato in SconfittaTask.run() e ho chiamato ChiamaSconfitta()");
						//inoltre faccio fermare il timer!
						timer.cancel();
						timer_da_mostrare.cancel();
						System.out.println("Ho eseguito timer.cancel() in SconfittaTask");
					}
				});
			}
		}
		
		
		// METODO PER SCRIVERE I PUNTEGGI SU FILE
		String SalvaPunteggiSuFile(String nome_giocatore, Integer punteggio_ottenuto) 
		{
			  String stringa_output = new String();
			  Vector<Punteggio> vettore_di_punteggi = new Vector<Punteggio>();
		      Vector<String> vettore_di_stringhe = new Vector<String>();
		      Punteggio questo_punteggio;
		      char carattere_operatore='0';
		      
		      //il char operazione deve rappresentare l'operazione, 
		      //D = diviso, A = addizione, S= sottrazione, M=Moltiplicazione, T=Tutti insieme, E=Equazioni
		      
		      switch(getOperatore_scelto())
		      {
		      	case '+':
		      		carattere_operatore='A';
		      		break;
		      	case '-':
		      		carattere_operatore='S';
		      		break;
		      	case '*':
		      		carattere_operatore=';';
		      		break;
		      	case '/':
		      		carattere_operatore='D';
		      		break;
		      	case '.':
		      		carattere_operatore='T';
		      		break;
		      	case '=':
		      		carattere_operatore='E';
		      		break;
		      }
		      
		      //Devo scrivere/salvare il file relativo a quel livello
		      String nome_file = "PunteggiModalitaArcade-Liv"+getDifficolta_scelta()+"-Mod:"+carattere_operatore+".txt";
		      
		      //Leggo i dati dal file e li metto nel vettore di stringhe
		      try
		      {
		         InputStream in = openFileInput(nome_file);
		         if(in!= null)
		         {
		            InputStreamReader tmp = new InputStreamReader(in);
		            BufferedReader reader = new BufferedReader(tmp);
		            String str;
		            while((str=reader.readLine())!=null)
		            {
		               vettore_di_stringhe.add(str);
		            }
		            in.close();
		         }
		      }
		      catch (java.io.FileNotFoundException e)
		      {
		         //ok, non l'abbiamo ancora creato
		      }
		      catch (Throwable t)
		      {
		         Toast.makeText(this, "Eccezione: " + t.toString(), 2000);
		      }
		      
		      
		      vettore_di_punteggi=Punteggio.LeggiVettoreDiStringhe(vettore_di_stringhe);
		      
		      ////////////////DEBUG/
		      /////////STAMPO TUTTO IL VETTORE
		      //System.out.println("Ho ordinato il vettore:");
		      //for(int i=0; i<vettore_di_punteggi.size(); i++)
		      //{
		      //   System.out.print("stampo su file l'elemento n° "+i+" = ");
		      //   vettore_di_punteggi.get(i).StampaPunteggio();
		      //}
		      
		      //Stampo i punteggi su file
		      
		
			// aggiungo il mio punteggio al vettore!
			questo_punteggio = new Punteggio(nome_giocatore, punteggio_ottenuto);
			vettore_di_punteggi.add(questo_punteggio);
		    Punteggio.OrdinaVettoreDiPunteggiDecrescenti(vettore_di_punteggi);
	
			// adesso che ho il vettore di punteggi li debbo stampare in un file di
			// output ordinati
	
			Punteggio.OrdinaVettoreDiPunteggiDecrescenti(vettore_di_punteggi);
	
			//SE I PUNTEGGI SONO PIÙ DI DIECI, ELIMINO GLI ECCEDENTI
			System.out.println("Ho ordinato il vettore:");
		
			while(vettore_di_punteggi.size()>getNUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY())
			{
				System.out.print("ho eliminato l'elemento n°"+(getNUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY()+1)+" = ");
				vettore_di_punteggi.get(getNUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY()).StampaPunteggio();
				vettore_di_punteggi.remove(getNUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY());
			}
			//apro il file di uscita e stampo i punteggi
		      try
		      {
		    	 System.out.println("Sto stampando i punteggi nel file di uscita");
		         PrintWriter out = new PrintWriter(openFileOutput(nome_file,0));
		         Punteggio.StampaVettorediPunteggiSuFile(out, vettore_di_punteggi);
		         System.out.println("Ho stampato i punteggi sul file di uscita!");
		         stringa_output = Punteggio.StampaVettorediPunteggiSuStringa(vettore_di_punteggi);
		         System.out.println("Ho stampato la stringa: "+stringa_output);
		         out.close();
		      }
		      catch (Throwable t)
		      {
		    	 System.out.println("Non sono riuscito a stampare i punteggi nel file di uscita, sono nel blocco catch");
		         //Toast.makeText(this, "Eccezione: " + t.toString(), 2000).show();
		      }
		     return stringa_output;
	
		}
		

	
}

	
	


