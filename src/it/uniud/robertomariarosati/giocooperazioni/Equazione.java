package it.uniud.robertomariarosati.giocooperazioni;

//classe che implementa un'equazione e la sua soluzione per il gioco delle operazioni

//classe che implementa un'equazione e la sua soluzione per il gioco delle operazioni

import java.util.Vector;

class Equazione
{

 //NOTA:
 //Hp: posizione_x=0
 //L'equazione è definita nella forma a*x + b + c + d = 0
 //Poi dovrò ricordarmi di stampare a*x + b = -c - d


 //definisco un costruttore
 Equazione()
 {
    int numero_random;
    
    //stabilisco il valore dell'incognita (tra -10 e 10)
    valore_x=-10+(int)(20*Math.random());
    //stabilisco la sua posizione
    posizione_x=(int)(4*Math.random());
    
    System.out.println("Ho definito valore_x = "+valore_x);
    System.out.println("Ho definito posizione_x = "+posizione_x);
    
    //Adesso stabilisco i valori dei quattro parametri 
    GeneraQuattroParametriRandom();
    
    //Genero i quattro risultati casuali
    GeneraRisultatiCasuali();
 }

 //funzione che genera i quattro parametri in base a valore e posizione della x
 void GeneraQuattroParametriRandom()
 {
    int parametri_contati=0;
    int somma_valori_parametri_contati=0;
    setVettore_parametri(new Vector<Integer>());
    getVettore_parametri().setSize(4);
    //genero il valore del parametro random
    do
    {
       getVettore_parametri().set(posizione_x,(Integer)(-10+(int)(20*Math.random())));
    }while(getVettore_parametri().get(posizione_x)==0);
    //adesso genero gli altri 3 parametri
    
    System.out.println("Ho definito il parametro in v[posizione_x] = "+getVettore_parametri().get(posizione_x));
   
    //fino alla lunghezza del vettore_parametri
    for(int i=0; i<getVettore_parametri().size(); i++)
    {
   
       //se i è diverso da posizione_x
       if(i!=posizione_x)
       {
          //se non ho ancora contato il terzo parametro
          if(parametri_contati!=2)
          {
             getVettore_parametri().set(i,(Integer)(-(getVettore_parametri().get(posizione_x)*valore_x)+(int)(2*getVettore_parametri().get(posizione_x)*valore_x*Math.random())));
             
             //aggiorno la somma dei parametri contati finora
             somma_valori_parametri_contati+=getVettore_parametri().get(i);
             
             parametri_contati++; //incremento il numero dei parametri che ho contato 
             //definisco i parametri random
          }
          else if(parametri_contati==2) //se ho già contato 2 parametri
          {
             System.out.println("Sono entrato in if(parametri_contati==2), i="+i);
             //il terzo dovrà essere definito così
             //poiché a*x+b+c+d=0
             //d = -a*x-b-c
          
             getVettore_parametri().set(i,(Integer)(-valore_x*getVettore_parametri().get(posizione_x)-somma_valori_parametri_contati));
          }   
          
          //stampo cosa ho definito
          System.out.println("Ho definito il parametro in v["+i+"] = "+getVettore_parametri().get(i));
       }
    }   
 }
 
 
 //funzione che STAMPA un'equazione
 String StampaEquazione()
 {
     //salvo tutto in una stringa
     String s;
     
     
     //inserisco il primo valore
     s=Integer.toString(getVettore_parametri().get(0));
     
     //se la x ha indice zero, stampo la x
     if(posizione_x==0)
     {
        s+="*x";
     }
     
     //inserisco il secondo valore, facendo attenzione al segno
     if(getVettore_parametri().get(1)<0)
     {
        //aggiungo il segno meno
        s+=" - ";
        //aggiungo il valore cambiato di segno
        s+=Integer.toString(-getVettore_parametri().get(1));
     }
     else
     {
        //aggiungo il segno più
        s+=" + ";
        //aggiungo il valore
        s+=Integer.toString(getVettore_parametri().get(1));
     }
     
     if(posizione_x==1)
     {
        s+="*x";
     }
     
     //stampo l'uguale
     s+=" = ";
     
     //DA QUI IN POI DEVO STAMPARE GLI ELEMENTI DEL VETTORE CAMBIANDOGLI SEGNO
     // a + b = - c - d
     
     //aggiungo il terzo valore
     s+=Integer.toString(-getVettore_parametri().get(2));
     
     if(posizione_x==2)
     {
        s+="*x";
     }
     
     //aggiungo il quarto valore, con le stesse accortezze del secondo
     if(-getVettore_parametri().get(3)<0)
     {
        //aggiungo il segno meno
        s+=" - ";
        //aggiungo il valore cambiato di segno
        s+=Integer.toString(getVettore_parametri().get(3));
     }
     else
     {
        //aggiungo il segno più
        s+=" + ";
        //aggiungo il valore
        s+=Integer.toString(-getVettore_parametri().get(3));
     }
     
     if(posizione_x==3)
     {
        s+="*x";
     }
     
     
     //adesso posso stampare la stringa su schermo
     System.out.println(s);
     System.out.println("La soluzione è: "+valore_x);
     System.out.print("I quattro parametri sono: (");
     for(int i=0; i<getVettore_parametri().size()-1; i++)
     {
        System.out.print(getVettore_parametri().get(i)+", ");
     }
     System.out.print(getVettore_parametri().get(getVettore_parametri().size()-1));
     System.out.println(")");
     
     
     System.out.println();
     return s;
 }
 
 //funzione che genera quattro risultati casuali (identica a Operazione)
 private int GeneraRisultatiCasuali()
 {
       boolean diverso_dagli_altri_risultati=true;
       int temp_risultato;
	      //imposto la capacità del vettore a quattro
	      setRisultati(new Vector<Integer>(4));
	      
	      //imposto casualmente l'indice al quale si troverà il risultato
	      indice_risultato_corretto=(int)(4*Math.random());
	      
	      //adesso faccio il ciclo che imposta tutti i risultati
	      //per ogni elemento del vettore
	      for(int i=0; i<getRisultati().capacity(); i++)
	      {
	         //se l'indice corrisponde all'indice del risultato ci metto dentro il risultato
	         if(i==indice_risultato_corretto)
	         {
	            getRisultati().add(valore_x);
	         }   
	         else  //altrimenti
	         {
	            //genero un risultato casuale e controllo che diverso dai precedenti tramite
	            //un ciclo do while
	            do
	            {  
	               //reimposto all'inizio di ogni ciclo il flag a true
	               diverso_dagli_altri_risultati=true;  
	               
	               //applico la funzione definita per generare un random
	               //se -30<=valore_x<=30
	               if(Math.abs(valore_x)<=30)
	               { 
	                  temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(-30,30);
	                  //System.out.println("Sono in Math.abs(valore_x)<=30. temp_risultato = "+temp_risultato);
	               }
	               else if(Math.abs(valore_x)>30 && Math.abs(valore_x)<=100)
	               {
	                  temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(valore_x-(int)(3*Math.abs((double)valore_x)/7+50/7),valore_x+(int)(3*Math.abs((double)valore_x)/7+50/7));
	                  //System.out.println("Sono in Math.abs(valore_x)>30 && Math.abs(valore_x)<=100. temp_risultato = "+temp_risultato);
	               }
	               else
	               {
	                  temp_risultato=LibreriaAleatoria.GeneraRandomTraEstremi(valore_x-1*Math.abs(valore_x)/2,valore_x+1*Math.abs(valore_x)/2);
	                  //System.out.println("Sono nell'else. temp_risultato = "+temp_risultato);
	               }
	               //se il temp risultato è uguale al risultato corretto, 
	               //imposto già il flag a false
	               if(temp_risultato==valore_x)
	                  diverso_dagli_altri_risultati=false;
	               //per ogni risultato già stabilito
	               for(int j=0; j<getRisultati().size(); j++)
	               {
	                  //se è uguale a uno degli altri risultati, imposto
	                  //la variabile booleana di stato come falsa
	                  if(temp_risultato==getRisultati().get(j))
	                  {
	                     //in questo caso imposto il flag a false
	                     diverso_dagli_altri_risultati=false;
	                  }
	               }
	            }while(diverso_dagli_altri_risultati==false);
	            //dopo il do-while, posso aggiungere il risultato al vettore
	            getRisultati().add(temp_risultato);
	         }
	      }
	      return indice_risultato_corretto;
 }


 //che parametri deve avere un'equazione? i vettori dei coefficienti, la posizione   
 //dell'icognita, gli operatori

 //SELETTORI
 public int ValoreX() {return valore_x;}
 public int Risultati(int i) {assert(i<getRisultati().size()); return getRisultati().get(i);}
 public int QuantiRisultati() {return getRisultati().size();}
 public int IndiceRisultatoCorretto() {return indice_risultato_corretto;}
 
 
 public Vector<Integer> getVettore_parametri() {
	return vettore_parametri;
}

public void setVettore_parametri(Vector<Integer> vettore_parametri) {
	this.vettore_parametri = vettore_parametri;
}


public Vector<Integer> getRisultati() {
	return risultati;
}

public void setRisultati(Vector<Integer> risultati) {
	this.risultati = risultati;
}


//vettore dei 4 parametri
 private Vector<Integer> vettore_parametri;
 //vettore dei 4 risultati
 private Vector<Integer> risultati;
 private int indice_risultato_corretto;
 //posizione incognita
 private int posizione_x;
 //valore incognita
 private int valore_x;
 //operatori
 private char operatore_1;
 private char operatore_2;
 

};