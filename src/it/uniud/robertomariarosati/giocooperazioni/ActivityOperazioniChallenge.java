package it.uniud.robertomariarosati.giocooperazioni;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityOperazioniChallenge extends SuperClasseOperazioni {

	//timer: ho bisogno di un timer
	private Timer timer;
	private Timer timer_da_mostrare;
	private int millisecondi_totali_a_disposizione;
	private int millisecondi_rimanenti;
	private TempoOrario tempo_rimanente;
	//indice risposta corretta per la generazione di opzioni casuali
	private int indice_risposta_corretta;
	
	private TextView testo_timer_challenge;
	private TextView testo_punteggio_challenge;
	
	private int punti_totali;
	private int risposte_corrette_consecutive;
	private int max_risposte_corrette_consecutive;
	private int risposte_corrette;
	private int risposte_sbagliate;
	private Button pulsante_avanzamento, pulsante_avanzamento2, pulsante_conclusione;
	private EditText inserimento_testo;
	private TextView statistiche_view; 
	private String nome_giocatore_temp;
	
	//equazione/operazione che uso
	private Equazione eq1;
	private Operazione op1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operazioni_challenge);
		//println di debug
		System.out.println("Ho fatto il setContentView nell'activity OperazioniChallenge" );
				
		//ricevo l'intent
		if (getIntent().getExtras() != null) {
		    //String value = extras.getString("difficolta");
			setDifficolta_scelta(getIntent().getIntExtra("difficolta_scelta", 1));
			setOperatore_scelto(getIntent().getCharExtra("operatore_scelto",'.'));
			System.out.println("Sono in OperazioniChallenge e ho ricevuto i seguenti dati dal Bundle:");
			System.out.println("difficolta_scelta = "+getDifficolta_scelta());
			System.out.println("operatore_scelto = "+getOperatore_scelto());
		}
			
		
		//Riferimento al layout
		setLayout_operazioni((RelativeLayout) findViewById(R.id.activity_operazioni_challenge));
		setLinearLayout_interno((LinearLayout) findViewById(R.id.LinearLayout_interno_a_activity_operazioni_challenge));
		setLinearLayout_molto_interno((LinearLayout) findViewById(R.id.LinearLayout_molto_interno_a_activity_operazioni_challenge));
		//imposto il TextView uguale all'id del widget testo_operazione
		setTesto_op((TextView) findViewById(R.id.testo_operazione_challenge));
		
		//aggiungo i widget Button che devono contenere i risultati al vettore
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato1_challenge));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato2_challenge));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato3_challenge));
		getVettore_pulsanti_ris().add((Button) findViewById(R.id.ButtonRisultato4_challenge));
				
				
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
		
		//setto le View dei testi di putneggio e timer
		testo_timer_challenge = (TextView) findViewById(R.id.timer_challenge);
		testo_punteggio_challenge = (TextView) findViewById(R.id.punteggio_challenge);
		tempo_rimanente = new TempoOrario();

		//creo l'equazione/operazione iniziale
		if(getOperatore_scelto()!='=')
		{
			millisecondi_totali_a_disposizione=60*1000;  //60 secondi
			op1 = new Operazione(getOperatore_scelto(), getDifficolta_scelta());
			indice_risposta_corretta=InserisciOperazione(op1, getTesto_op(), getVettore_pulsanti_ris());
		}
		else if (getOperatore_scelto()=='=')
		{
			millisecondi_totali_a_disposizione=120*1000; //120 secondi
			eq1 = new Equazione();
			indice_risposta_corretta=InserisciEquazione(eq1, getTesto_op(), getVettore_pulsanti_ris());
		}
		
		System.out.println("Ho creato la prima equazione /operazione nella classe OperazioniChallenge");
		
		punti_totali=0;
		risposte_corrette_consecutive=0;
		risposte_corrette=0;
		risposte_sbagliate=0;
		max_risposte_corrette_consecutive=0;
		
		
		//devo far partire i timer
		millisecondi_rimanenti=millisecondi_totali_a_disposizione;
		tempo_rimanente.ConvertiDaMillisecondi(millisecondi_rimanenti);
		testo_timer_challenge.setText(tempo_rimanente.StampaTempoMinutieSecondiSuStringa());
		
		System.out.println("Sto per far partire i timer nella classe Operazioni Challenge");
		
		AvviaTimersChallenge();
	
		System.out.println("Tutto l'onCreate di ActivityOperazioniChallenge è andato a buon fine");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_operazioni_challenge, menu);
		return true;
	}
	
	
	//funzione AvviaTimer
	void AvviaTimersChallenge()
	{
		timer = new Timer();
		timer.schedule(new TempoEsauritoTask(), millisecondi_totali_a_disposizione);
		timer_da_mostrare = new Timer();
		//NOTA: timer da mostrare mi serve anche a regolare la barra (ogni 2000/500 ms la incremento)
		timer_da_mostrare.scheduleAtFixedRate(new TimerTask()
			{
			
				public void run()
				{
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							millisecondi_rimanenti-=1000;
							tempo_rimanente.ConvertiDaMillisecondi(millisecondi_rimanenti);
							testo_timer_challenge.setText(tempo_rimanente.StampaTempoMinutieSecondiSuStringa());
					    }
					});
				}
			}
		,1000,1000);
		
		System.out.println("Sto per uscire da AvviaTimers");
	}
	
	
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
				    	//int tempo_questa_risposta=(int)((double)(counter)*millisecondi_a_disposizione/fondoscala_progress_bar);
				    		
				    	//System.out.println("counter = "+counter);
				    	//System.out.println("tempo_questa_risposta = "+tempo_questa_risposta);
				   		
				   		//ho ricalcolato tutte le statistiche
				   		//nei punti totali includo il molitplicatore ogni 5 risposte
				   		//punti_totali+=Math.pow(2,(risposte_corrette/5));
			    		//risposte_corrette++;
			    		//tempo_totale+=tempo_questa_risposta;
			    		//tempo_medio=tempo_totale/risposte_corrette;
				    	//somma_quadrati_tempi+=Math.pow(tempo_questa_risposta,2);
				    	//varianza=somma_quadrati_tempi/risposte_corrette-(int)Math.pow(tempo_medio,2);
				   		
			    		//se ho cliccato sulla risposta corretta
			    		risposte_corrette_consecutive++;
			    		risposte_corrette++;
			    		punti_totali+=Math.pow(2,(risposte_corrette_consecutive/6));
			    		
			    		//aggiorno il puntegggio nella textview
			    		String s="+"+String.valueOf((int)(Math.pow(2,(risposte_corrette_consecutive/6))));
			    		s+="! score= ";
			    		s+=String.valueOf(punti_totali);
			    		s+=" pt";
			    		testo_punteggio_challenge.setText(s);
				   		
			    		
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
							//	timer.cancel();
							//	timer.purge();
							//	timer_da_mostrare.cancel();
							//	timer_da_mostrare.purge();
								System.out.println("Ho eseguito timer.purge() e timer.cancel() poiché è stata cliccata la risposta esatta");
							}
						});
			    		
						
							
						if(getOperatore_scelto()!='=')
						{
							op1 = new Operazione(getOperatore_scelto(), getDifficolta_scelta());
							indice_risposta_corretta=InserisciOperazione(op1, getTesto_op(), getVettore_pulsanti_ris());
						}
						else if (getOperatore_scelto()=='=')
						{
							eq1 = new Equazione();
							indice_risposta_corretta=InserisciEquazione(eq1, getTesto_op(), getVettore_pulsanti_ris());
						}
						System.out.println("L'utente ha cliccato sulla risposta corretta: aggiorno l'operazione");
						
						break; //se ho cliccato sulla risposta esatta devo interrmpere il ciclo
			    	}  //ALTRIMENTI, SE HO CLICCATO SULLA RISPOSTA SBAGLIATA
			    	else if (v==getVettore_pulsanti_ris().get(i))
			    	{
			    		System.out.println("Ho cliccato sulla risposta sbagliata!");
			    		
			    		//memorizzo l'eventuale nuovo record di risposte corrette consecutive
			    		if(max_risposte_corrette_consecutive<risposte_corrette_consecutive)
			    		{
			    			max_risposte_corrette_consecutive=risposte_corrette_consecutive;
			    		}
			    		
			    		risposte_corrette_consecutive=0;
			    		risposte_sbagliate++;
				        //ChiamaSconfitta();
			    		
			    		//riscrivo il punteggio
			    		String s="score = ";
			    		s+=String.valueOf(punti_totali);
			    		s+=" pt";
			    		testo_punteggio_challenge.setText(s);
			    		
						if(getOperatore_scelto()!='=')
						{
							op1 = new Operazione(getOperatore_scelto(), getDifficolta_scelta());
							indice_risposta_corretta=InserisciOperazione(op1, getTesto_op(), getVettore_pulsanti_ris());
						}
						else if (getOperatore_scelto()=='=')
						{
							eq1 = new Equazione();
							indice_risposta_corretta=InserisciEquazione(eq1, getTesto_op(), getVettore_pulsanti_ris());
						}
						System.out.println("L'utente ha cliccato sulla risposta corretta: aggiorno l'operazione");
			    		
				   		break;
				   	}
			    } 	
			}
		};
				
		
		//CLASSE INTERNA ALLA CLASSE OPERAZIONI CHE SERVE SOLO A CREARE IL TIMER TASK
		//DA ESEGUIRE QUANDO VIENE PERSA LA PARTITA
		class TempoEsauritoTask extends TimerTask
		{
			public void run()
			{
				//quando viene chiamato questo timer task assegno la sconfitta!
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						ChiamaConclusione();
					//	System.out.println("Sono passati "+millisecondi_a_disposizione/1000+" secondi, sono entrato in SconfittaTask.run() e ho chiamato ChiamaSconfitta()");
						//inoltre faccio fermare il timer!
						timer.cancel();
						timer_da_mostrare.cancel();
						System.out.println("Ho eseguito timer.cancel() in SconfittaTask");
				    }
				});
			}
		}
		
		//METODO PER LA SCONFITTA
		void ChiamaConclusione()
		{
			System.out.println("ATTENZIONE:E' stata chiamata ChiamaConclusione()");
			//faccio scomparire il layout interno a activity_operazioni
			
			//controllo se devo modificare il valore di max_risposte_corrette_consecutive
			//memorizzo l'eventuale nuovo record di risposte corrette consecutive
    		if(max_risposte_corrette_consecutive<risposte_corrette_consecutive)
    		{
    			max_risposte_corrette_consecutive=risposte_corrette_consecutive;
    		}
			
			
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
					
			//stringa_statistiche+="Hai risposto a "+risposte_corrette+" quesiti in "+ ((double)tempo_totale/1000)+"s\n";
			//stringa_statistiche+="Tempo medio di risposta: μ(T) = "+((double)tempo_medio/1000)+"s\n";
			//stringa_statistiche+="Varianza: σ²(T) = "+((double)varianza/1000000)+"s²\n";
			//stringa_statistiche+="S.q.m. = σ(T) = "+Math.sqrt((double)varianza)/1000+"s\n";
					
			stringa_statistiche+=getString(R.string.stringa_challenge_statisticaPt1)+' '+risposte_corrette+"\n";
			stringa_statistiche+=getString(R.string.stringa_challenge_statisticaPt2)+' '+max_risposte_corrette_consecutive+"\n";
			stringa_statistiche+=getString(R.string.stringa_challenge_statisticaPt3)+' '+risposte_sbagliate+"\n";
			stringa_statistiche+=getString(R.string.stringa_challenge_statisticaPt4)+' '+(risposte_sbagliate+risposte_corrette)+"\n";
			stringa_statistiche+=getString(R.string.stringa_challenge_statisticaPt5)+' '+((int)((((double)risposte_corrette)/(risposte_sbagliate+risposte_corrette))*100))+" %\n";
			
			
			//aggiorno la views per i punti e ne creo una per le statistiche
			getTesto_op().setText(punti_totali+' '+R.string.punti);
				
			statistiche_view = new TextView(this);
					
			statistiche_view.setText(stringa_statistiche);
			statistiche_view.setTextSize(20);
			getLinearLayout_interno().addView(statistiche_view);
			
			pulsante_avanzamento = new Button(this);
			
			pulsante_avanzamento.setText(R.string.continua);
			pulsante_avanzamento.setTextSize(35);
			pulsante_avanzamento.setBackgroundResource(R.drawable.mio_button_selector);
			
			getLinearLayout_interno().addView(pulsante_avanzamento);
			
			inserimento_testo = new EditText(this);
			inserimento_testo.setBackgroundResource(R.drawable.mio_edit_text);
			inserimento_testo.setInputType(InputType.TYPE_CLASS_TEXT);
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
			
			//definisco cosa succede quando viene chiamato pulsante_avanzamento
			
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
				      
					
					((TextView) findViewById(R.id.testo_operazione_challenge)).setText(stringa_operatore+" LIV "+getDifficolta_scelta());
					((TextView) findViewById(R.id.testo_operazione_challenge)).setTextSize(25);
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
					Intent mio_intent = new Intent(ActivityOperazioniChallenge.this, ActivityMain.class);
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
			
			//salvo i punteggi su file!
			SalvaPunteggiSuFile();

			
		}
		
		// METODO PER SCRIVERE I PUNTEGGI SU FILE
		void SalvaPunteggiSuFile() 
		{
			  Vector<Punteggio> vettore_di_punteggi = new Vector<Punteggio>();
		      Vector<String> vettore_di_stringhe = new Vector<String>();
		      Punteggio questo_punteggio;
		      
		      //Leggo i dati dal file e li metto nel vettore di stringhe
		      try
		      {
		         InputStream in = openFileInput("PunteggiModalitaChallenge.txt");
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
		       //  Toast.makeText(this, "Eccezione: " + t.toString(), 2000);
		      }
		      
		      
		      //FileReader reader;
		      //Scanner in = new Scanner(System.in);
		      //PrintWriter out = new PrintWriter(System.out);
		      //Vector<Punteggio> vettore_di_punteggi = new Vector<Punteggio>();
		      //boolean salvataggi_gia_presenti=true;
				//try
				//{
				//   System.out.println("Sono entrato nel primo blocco try");
				//   reader = new FileReader("input.txt");
				//      in = new Scanner(reader);
				//}
				//catch(IOException e)
				//{
				//   System.out.println("Sono entrato nel primo blocco catch(IOException e)");
		      //   System.out.println("File non trovato!");
			   //   //Se sono qui, vuol dire che non c'erano già dei salvataggi
			   //   salvataggi_gia_presenti=false;
				//}
		      //chiudo il file in lettura
		      //in.close();

		      //ora che li ho letti, apro il file in scrittura
		      //try
		      //{
		      //   System.out.println("Sono entrato nel secondo blocco try");
		      //   out = new PrintWriter("input.txt");
		      //}
		      //catch(IOException e)
		      //{
		      //   System.out.println("Sono entrato nel secondo blocco catch(IOException e)");
		      //   System.out.println("File non trovato!");
		      //}


		      //adesso che ho il vettore di punteggi li debbo stampare in un file di output ordinati
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
			questo_punteggio = new Punteggio("Roberto", punti_totali);
			vettore_di_punteggi.add(questo_punteggio);
		    Punteggio.OrdinaVettoreDiPunteggiDecrescenti(vettore_di_punteggi);
	
			// adesso che ho il vettore di punteggi li debbo stampare in un file di
			// output ordinati
	
			Punteggio.OrdinaVettoreDiPunteggiDecrescenti(vettore_di_punteggi);
	
			// //////////////DEBUG/
			// ///////STAMPO TUTTO IL VETTORE
			System.out.println("Ho ordinato il vettore:");
			for (int i = 0; i < vettore_di_punteggi.size(); i++) 
			{
				System.out.print("stampo su file l'elemento n° " + i + " = ");
				vettore_di_punteggi.get(i).StampaPunteggio();
			}
	
	
			//apro il file di uscita e stampo i punteggi
		      try
		      {
		         PrintWriter out = new PrintWriter(openFileOutput("PunteggiModalitaChallenge.txt",0));
		         Punteggio.StampaVettorediPunteggiSuFile(out, vettore_di_punteggi);
		         out.close();
		      }
		      catch (Throwable t)
		      {
		         //Toast.makeText(this, "Eccezione: " + t.toString(), 2000).show();
		      }
		     return;
	
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
				      String nome_file = "PunteggiModalitaChallenge-Liv"+getDifficolta_scelta()+"-Mod:"+carattere_operatore+".txt";
				      
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




