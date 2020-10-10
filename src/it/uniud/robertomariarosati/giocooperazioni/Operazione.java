/**
 * 
 */
package it.uniud.robertomariarosati.giocooperazioni;

/**
 * @author roberto
 *
 */
/*Classe operazione:
Un'operazione contiene i due operandi, l'operatore, un vettore di quattro risultati (uno dei quali corretto), e l'indice del risultato corretto
*/

//GOP: this class defines an operation.
//An operation contains: 
//two operands (Ex: 4,7)
//an operator (Ex: +)
//a vector with four result, just one right (Ex: [11,8,9,10])
//the index of the correct result of the vector (in this example: 0)

import java.util.Vector;

public class Operazione 
{

//GOP: constructor that defines 
//COSTRUTTORE CON DIFFICOLTA'
Operazione(int diff)
{
	int op;
	op=(int)(4*Math.random());
	//vettore dei risultati
	
	GeneraOperandiCasuali(op, diff);
	   
	//Adesso che ho determinato gli operandi e gli operatori posso generare dei risultati
	GeneraRisultatiCasuali(diff);
	
	
}

//
//GOP: Constructor for a simple operation
Operazione(char op, int diff)
{
   //vettore dei risultati
   if(op!='+' && op!='-' && op!='*' && op!= '/')
   {
      op=(char)(4*Math.random());
      //Devo fare una switch qui per convertirlo prima
      switch(op)
      {
         case 0:
            op='+';
            break;
         case 1:
            op='-';
            break;
         case 2:
            op='*';
            break;
         case 3:
            op='/';
            break;
      }
   }
   
	GeneraOperandiCasuali(op, diff);
	  
	//Adesso che ho determinato gli operandi e gli operatori posso generare dei risultati
	GeneraRisultatiCasuali(diff);
	
	
}

//GOP: This method is a constructor with arguments
Operazione(int f, int s, char o, Vector<Integer> ris, int indice_ris_cor)
{
	assert(o=='/' || o=='*' || o=='+' || o== '-');
	if(o=='/')
		assert(second!=0);
	first=f;
	second=s;
	operatore=o;
	//vettore con argomenti
	risultati=ris;
	//determino comunque il risultato
	switch(o)
	{
		case '+':
			operatore='+';
			risultato=first+second;
			break;
		case '-':
			operatore='-';
			risultato=first-second;
			break;
		case '*':
			operatore='*';
			risultato=first*second;
			break;
		case '/':
			operatore='/';
			//inoltre, nel caso mi sia uscito un diviso, devo anche controllare che second
			//non sia pari a zero (non posso chiedere quanto fa un numero diviso zero!)
			//e sia un divisore di first
			while(second==0 || first%second!=0)
			{
				second=(int)(20*Math.random());
			}
			risultato=first/second;
			break;
	   }
	indice_risultato_corretto=indice_ris_cor;
}
	
//GOP: the following method generates 4 random results and put them in a vector. Furthermore, it puts the variable "indice_risultato_corretto" 
//METODO DELLA CLASSE CHE RANDOMIZZA I RISULTATI (mettendoli nel vettore) E IMPOSTA LA VARIABILE indice_risultato_corretto, RESTITUENDOLA ANCHE COME INTERO
private int GeneraRisultatiCasuali(int diff)
{
boolean diverso_dagli_altri_risultati=true;
int temp_risultato=0;
      //"dado" (vedi dopo)
      int dado;
      boolean dado_lanciato_non0e1;  
      int contatore_loops;

	   //imposto la capacità del vettore a quattro
	   risultati = new Vector<Integer>(4);
	   
	   //imposto casualmente l'indice al quale si troverà il risultato
	   indice_risultato_corretto=(int)(4*Math.random());
	   
	   //PER MOLTIPLICAZIONI e SOTTRAZIONI e ADDIZIONI
	   //numero casuale che mi dice la formula con la quale generò i numeri casuali (in base alla difficoltà
	   //cioè:
	   //dado=0o1 => generazione puramente casuale
	   //dado=2o4 => genero un risultato pari a ris +- (6-diff)
	   //dado=3o5 => genero un risultato pari a ris +- (6-diff)*10
	   
	   //PER DIVISIONI
	   //dado=0o1 => generazione puramente casuale
	   //dado=2o4 => genero un risultato pari a ris +- (6-diff)
	   //dado=3o5 => genero un risultato pari ad un divisore di first
	   //adesso faccio il ciclo che imposta tutti i risultati
	   
	   //per ogni elemento del vettore
	   for(int i=0; i<risultati.capacity(); i++)
	   {
	      //se l'indice corrisponde all'indice del risultato ci metto dentro il risultato
	      if(i==indice_risultato_corretto)
	      {
	         risultati.add(risultato);
	      }   
	      else  //altrimenti
	      {
	         //genero un risultato casuale e controllo che diverso dai precedenti tramite
	         //un ciclo do while
	         do
	         {  
	            //reimposto all'inizio di ogni ciclo il flag a true
	            diverso_dagli_altri_risultati=true;  
	            
	            //lancio il dado
	            dado = LibreriaAleatoria.GeneraRandomTraEstremi(0,diff);
	            System.out.println("Ho lanciato il dado, dado="+dado);
	   
	            switch(dado)
	            {
	               case 0:
	               case 1:
	                  dado_lanciato_non0e1=false;
	                  break;
	               
	               case 2:
	               case 4:
	                  temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(risultato-(10-diff),risultato+(10+diff));
	                  dado_lanciato_non0e1=true;
	                  break;
	               case 3:
	               case 5:
	                  //se l'operatore non è una divisione
	                  if(operatore!='/')
	                  {
	                     //genero un "moltiplicatore" casuale, riutilizzando dado, fin quando non è diverso da zero
	                     do
	                     {  dado=LibreriaAleatoria.GeneraRandomTraEstremi(-(9-diff),(9+diff));
	                     }while(dado==0);
	                     dado_lanciato_non0e1=true;
                        temp_risultato=risultato+10*dado;
	                  }
	                  else
	                  {
	                	 contatore_loops=0;
	                     do   //può trovare al massimo tre valori che siano divisori di first, se non va bene nessuno dei tre, è out
	                     {
	                    	System.out.println("Sono entrato nel ciclo do - while temp_risultato==0 || temp_risultato==risultato || first%temp_risultato!=0)");
	                    	System.out.println("first = "+first+", second = "+second);
	                    	//genero un risultato casuale che sia minore del dividendo
		                    temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(0,first);
		                    //se ho beccato un divisore, aumento contatore_loops
		                    if(temp_risultato!=0 && first%temp_risultato==0)
		                    {
		                    	contatore_loops++;
		                    	System.out.println("contatore_loops= "+contatore_loops);
		                    }
		                    if(temp_risultato==0)
		                    {
		                    	contatore_loops++;
		                    }
	                     }while(contatore_loops<3 && (temp_risultato==0 || temp_risultato==risultato || first%temp_risultato!=0)); //attenti alla divisione per zero
	                	 dado_lanciato_non0e1=false;
	                  }
	                  break;
	               default:
	                  dado_lanciato_non0e1=false;
	                  break;
	            }
	            
	            //applico la funzione definita per generare un random
	            //se -30<=risultato<=30
	            if(Math.abs(risultato)<=30 && dado_lanciato_non0e1==false)
	            { 
	               temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(-30,30);
	               //System.out.println("Sono in Math.abs(risultato)<=30. temp_risultato = "+temp_risultato);
	            }
	            else if(Math.abs(risultato)>30 && Math.abs(risultato)<=100 && dado_lanciato_non0e1==false)
	            {
	               temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(risultato-(int)(3*Math.abs((double)risultato)/7+50/7),risultato+(int)(3*Math.abs((double)risultato)/7+50/7));
	               //System.out.println("Sono in Math.abs(risultato)>30 && Math.abs(risultato)<=100. temp_risultato = "+temp_risultato);
	            }
	            else if(dado_lanciato_non0e1==false)
	            {
	               temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(risultato-1*Math.abs(risultato)/2,risultato+1*Math.abs(risultato)/2);
	               //System.out.println("Sono nell'else. temp_risultato = "+temp_risultato);
	            }
	            //se il temp risultato è uguale al risultato corretto, 
	            //imposto già il flag a false
	            if(temp_risultato==risultato)
	               diverso_dagli_altri_risultati=false;
	            //per ogni risultato già stabilito
	            for(int j=0; j<risultati.size(); j++)
	            {
	               //se è uguale a uno degli altri risultati, imposto
	               //la variabile booleana di stato come falsa
	               if(temp_risultato==risultati.get(j))
	               {
	                  //in questo caso imposto il flag a false
	                  diverso_dagli_altri_risultati=false;
	               }
	            }
	         }while(diverso_dagli_altri_risultati==false);
	         //dopo il do-while, posso aggiungere il risultato al vettore
	         risultati.add(temp_risultato);
	      }
	   }
	   return indice_risultato_corretto;
}
	
	//METODO CHE STAMPA L'OPERAZIONE SU STDOUT
	public void StampaOperazione()
	{
	   System.out.println(First()+" "+Operatore()+" "+Second()+" = "+Risultato());
	   char ch='a';
	   for(int i=0; i<4; i++)
	   {
	      ch=(char)(ch+i);
	      System.out.println(ch+") "+risultati.get(i));
	   }
	   System.out.println();
	}

//GOP: Method that returns the maximum value of a random resul, depending on the difficulty
//Funzione che dà il range per la generazione di un risultato casuale in funzione della difficoltà per le addizioni e sottrazioni
private int MaxRange(int x)
{
   return 9*x+(int)Math.pow(2,x);
   
}
//Method that returns the maximum value of a random result in a moltiplication, depending on the difficulty
private int MaxRangeMolt(int x)
{
   return 4+3*x;
}
	
//GOP: method that creates random operands 
private void GeneraOperandiCasuali(int op, int diff)
{
   int moltiplicatore_temporaneo_per_dividendi;
   switch(op)
	{
		case '+':
			operatore='+';
			//genero gli operandi
			first=LibreriaAleatoria.GeneraRandomTraEstremi((int)(MaxRange(diff)/4),MaxRange(diff));
	      second=LibreriaAleatoria.GeneraRandomTraEstremi((int)(MaxRange(diff)/4),MaxRange(diff));;
	      risultato=first+second;
			break;
		case '-':
			operatore='-';
			first=LibreriaAleatoria.GeneraRandomTraEstremi((int)(MaxRange(diff)/2),MaxRange(diff));
	      second=LibreriaAleatoria.GeneraRandomTraEstremi(0,MaxRange(diff));
			risultato=first-second;
			break;
      case '*':
			operatore='*';
			first=LibreriaAleatoria.GeneraRandomTraEstremi(0,MaxRangeMolt(diff));
			second=LibreriaAleatoria.GeneraRandomTraEstremi(0,MaxRangeMolt(diff));
			risultato=first*second;
			break;
		case '/':
			operatore='/';
			
			//aggiungo una modifica affinché il numero abbia con buona probabilità divisori
			
			//stabilisco un numero casuale per il quale dovrà essere moltiplicato il numero casuale generato, per far sì che abbia più divisori.
			moltiplicatore_temporaneo_per_dividendi=LibreriaAleatoria.GeneraRandomTraEstremi(0,10);
			
			
			first=LibreriaAleatoria.GeneraRandomTraEstremi(0,4*diff+6);
	      second=LibreriaAleatoria.GeneraRandomTraEstremi(0,first); //in nessun caso il denom. sarà maggiore del numeratore
	      
			//inoltre, nel caso mi sia uscito un diviso, devo anche controllare che second
			//non sia pari a zero (non posso chiedere quanto fa un numero diviso zero!)
			//e sia un divisore di first
			while(second==0 || first%second!=0)
			{
				second=LibreriaAleatoria.GeneraRandomTraEstremi(0,MaxRange(diff));
			}
			risultato=first/second;
			break;
	  }
	  return;
}
	
   //GOP: selectors	
   //SELETTORI
	public int First(){return first;}
	public int Second(){return second;}
	public char Operatore(){return operatore;}
	public int Risultato() {return risultato;}
	public int Risultati(int i) {assert(i<risultati.size()); return risultati.get(i);}
	public int QuantiRisultati() {return risultati.size();}
	public int IndiceRisultatoCorretto() {return indice_risultato_corretto;}
	
	//GOP: private fields	
	//CAMPI PRIVATI
	private int first;
	private int second;
	private char operatore;
	private int risultato;
	private Vector<Integer> risultati;     //nota:uso la classe involucro Integer perché
	                                        //Java non consente di caricare tipi primitivi
	                                        //nei vettori
	private int indice_risultato_corretto;

};
