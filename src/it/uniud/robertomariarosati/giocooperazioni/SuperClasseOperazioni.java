package it.uniud.robertomariarosati.giocooperazioni;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author roberto
 *
 */
public class SuperClasseOperazioni extends Activity 
{
	
	private final int NUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY=8;
	//intero difficolta
	private int difficolta_scelta;
	//carattere con cui giocare (opreazioni casuali = '.');
	private char operatore_scelto;
	//Riferimento al layout
	private RelativeLayout layout_operazioni;
	private LinearLayout LinearLayout_interno;
	private LinearLayout LinearLayout_molto_interno;
	//creo un'oggetto testo
	private TextView testo_op;
	//dichiaro un vettore di Button
	private Vector<Button> vettore_pulsanti_ris = new Vector<Button>();

	
	//sfondi
	private Drawable sfondo1;
	private Drawable sfondo2;
	private boolean sfondo_1_impostato;

	
	//disabilito il tasto back
	@Override
	public void onBackPressed() 
	{
	    // Do Here what ever you want do on back press
	}
	
	
	//NOTA: somma_quadrati_tempi serve unicamente al calcolo della varianza
	//che si ricorda essere σ²(X) = E[X²] - E²[X]
	
	//tempo medio verrà ottenuto semplicemente come E[x]=tempo_totale/risposte_corrette
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operazioni, menu);
		return true;
	}
	
	//Funzione che inserisca i quattro risultati e l'operazione nei campi indicati, e restituisce 
	//l'indice del risultato corretto
	int InserisciOperazione(Operazione op, TextView testo_op, Vector<Button> vettore_pulsanti)
	{
		System.out.println("Sono entrato in InserisciOperazione nella SuperClasseOperazioni");
		//imposto il testo dell'operazione
		ImpostaTestoSuTextView(testo_op, op);
		System.out.println("Ho impostato il testo sulla TextView in InserisciOperazione nella SuperClasseOperazioni");
		//println di debug
		//System.out.println("Ho impostato il testo dell'operazione");
		//scrivo i risultati nei quattro pulsanti dei risultati
		for(int i=0; i<vettore_pulsanti.size(); i++)
		{
			//println di debug
			//System.out.println("Sono nel ciclo for. i ="+i);
					
			vettore_pulsanti.get(i).setText(Integer.toString(op.Risultati(i)));
		}
		
		System.out.println("Ho impostato i risultati sui pulsanti in InserisciOperazione nella SuperClasseOperazioni");
		//println di debug
		//System.out.println("Ho eseguito il ciclo for");
		return op.IndiceRisultatoCorretto();
		
		
	}
	

	//classe per le EQUAZIONI
	int InserisciEquazione(Equazione eq, TextView testo_op, Vector<Button> vettore_pulsanti)
	{
		System.out.println("Sono entrato in InserisciOperazione nella SuperClasseEquazioni");

//		//quando inserisco una nuova equazione cambio anche lo sfondo
//		//runOnUiThread(new Runnable() {
//		     @Override
//			 @SuppressLint("NewApi")
//			 @SuppressWarnings("deprecation")
//		     public void run() 
//		     {
//
//					if(!isSfondo_1_impostato())
//					{
//						int sdk = android.os.Build.VERSION.SDK_INT;
//						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) 
//						{
//						    getLayout_operazioni().setBackgroundDrawable(getSfondo1());
//						} 
//						else 
//						{
//							getLayout_operazioni().setBackground(getSfondo1());
//						}
//						System.out.println("Ho cambiato da sfondo2 a sfondo1");
//						setSfondo_1_impostato(true);
//					}
//					else
//					{
//						int sdk = android.os.Build.VERSION.SDK_INT;
//						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) 
//						{
//						    getLayout_operazioni().setBackgroundDrawable(getSfondo2());
//						} 
//						else 
//						{
//							getLayout_operazioni().setBackground(getSfondo2());
//						}
//						setSfondo_1_impostato(false);
//						System.out.println("Ho cambiato da sfondo1 a sfondo2");
//					}
//
//		     }
//		});//ho finito di cambiare lo sfondo

		System.out.println("Sono in InserisciOperazione ed ho chiamato AvviaTimers");

		
		//println di debug
		System.out.println("E' stata chiamata la funzione InserisciOperazione");
		//imposto il testo dell'operazione
		ImpostaTestoSuTextView(testo_op, eq);
		
		//println di debug
		//System.out.println("Ho impostato il testo dell'operazione");
		//scrivo i risultati nei quattro pulsanti dei risultati
		for(int i=0; i<vettore_pulsanti.size(); i++)
		{
			//println di debug
			//System.out.println("Sono nel ciclo for. i ="+i);
					
			vettore_pulsanti.get(i).setText(Integer.toString(eq.Risultati(i)));
		}
		//println di debug
		//System.out.println("Ho eseguito il ciclo for");
		return eq.IndiceRisultatoCorretto();
	}
	
	
	//funzione che, data una view e un'operazione, la imposta come testo della view
	void ImpostaTestoSuTextView(TextView t_v, Operazione op)
	{
		char operat;
		if(op.Operatore()=='/')
		{
			operat='÷';
		}
		else 
		{
			operat=op.Operatore();
		}
		getTesto_op().setText(op.First()+" "+operat+" "+op.Second()+" =");
	}
	
	//funzione che, data una view e un'equazione, la imposta come testo della view
	void ImpostaTestoSuTextView(TextView t_v, Equazione eq)
	{
		String s;
		s=eq.StampaEquazione();
		getTesto_op().setText(s);
	}
	

	
	
	//SETTERS e GETTERS
	
	public int getDifficolta_scelta() {
		return difficolta_scelta;
	}

	public void setDifficolta_scelta(int difficolta_scelta) {
		this.difficolta_scelta = difficolta_scelta;
	}

	public char getOperatore_scelto() {
		return operatore_scelto;
	}

	public void setOperatore_scelto(char operatore_scelto) {
		this.operatore_scelto = operatore_scelto;
	}

	public TextView getTesto_op() {
		return testo_op;
	}

	public void setTesto_op(TextView testo_op) {
		this.testo_op = testo_op;
	}

	public Vector<Button> getVettore_pulsanti_ris() {
		return vettore_pulsanti_ris;
	}

	public void setVettore_pulsanti_ris(Vector<Button> vettore_pulsanti_ris) {
		this.vettore_pulsanti_ris = vettore_pulsanti_ris;
	}


	public RelativeLayout getLayout_operazioni() {
		return layout_operazioni;
	}


	public void setLayout_operazioni(RelativeLayout layout_operazioni) {
		this.layout_operazioni = layout_operazioni;
	}


	public LinearLayout getLinearLayout_interno() {
		return LinearLayout_interno;
	}


	public void setLinearLayout_interno(LinearLayout linearLayout_interno) {
		LinearLayout_interno = linearLayout_interno;
	}


	public LinearLayout getLinearLayout_molto_interno() {
		return LinearLayout_molto_interno;
	}


	public void setLinearLayout_molto_interno(LinearLayout linearLayout_molto_interno) {
		LinearLayout_molto_interno = linearLayout_molto_interno;
	}


	public Drawable getSfondo1() {
		return sfondo1;
	}


	public void setSfondo1(Drawable sfondo1) {
		this.sfondo1 = sfondo1;
	}


	public Drawable getSfondo2() {
		return sfondo2;
	}


	public void setSfondo2(Drawable sfondo2) {
		this.sfondo2 = sfondo2;
	}


	public boolean isSfondo_1_impostato() {
		return sfondo_1_impostato;
	}


	public void setSfondo_1_impostato(boolean sfondo_1_impostato) {
		this.sfondo_1_impostato = sfondo_1_impostato;
	}


	public int getNUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY() {
		return NUMERO_MASSIMO_PUNTEGGI_NEL_DISPLAY;
	}

}

	
	


