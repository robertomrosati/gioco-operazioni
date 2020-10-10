package it.uniud.robertomariarosati.giocooperazioni;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

//GOP: this class permits to manage the scores (punteggio means score)

public class Punteggio  //GOP: Punteggio=Score
{
   private String nome_giocatore;    //GOP: nome giocatore=name_player
   private Integer valore_punteggio;    //GOP: valore_punteggio=value_score
   
   //costruttori
   Punteggio() {}
   Punteggio(String nome, Integer valore)
   {
      nome_giocatore=nome;
      valore_punteggio=valore;
   }
   
   public String getNomeGiocatore() 
   {
	   return nome_giocatore;
   }
   public void setNomeGiocatore(String nome_giocatore) 
   {
	   this.nome_giocatore = nome_giocatore;
   }
   public Integer getValorePunteggio()
   {
	   return valore_punteggio;
   }
   public void setValorePunteggio(Integer valore_punteggio) 
   {
	   this.valore_punteggio = valore_punteggio;
   }
   
   public void StampaPunteggio()              //GOP: StampaPunteggio=PrintScore
   {
      System.out.println('<'+nome_giocatore+"> "+valore_punteggio);
      return;
   }
   
   public void StampaPunteggio(PrintWriter stream_out)
   {
      stream_out.println('<'+nome_giocatore+"> "+valore_punteggio);
      return;
   }
   
   public void StampaPunteggio(String s)              
   {
      s='<'+nome_giocatore+"> "+valore_punteggio;
      return;
   }
   
   
   //GOP: the next method orders the scores to be shown by INCREASING order, 
   //using an "insertion sort" algorithm
   
   //nota:dovrei aver usato l'algoritmo insertion sort
   static void OrdinaVettoreDiPunteggiCrescenti(Vector<Punteggio> vettore_da_ordinare)
   {
      
      //fino alla fine del vettore
      for(int i=0; i<vettore_da_ordinare.size(); i++)
      {
         //fino a quando non torno a zero
         for(int j=i; j>0; j--)
         {
            //se il punteggio dell'i-esimo elemento è minore del valore del li scambio
            if(vettore_da_ordinare.get(j).getValorePunteggio()<vettore_da_ordinare.get(j-1).getValorePunteggio())
            {
               SwapPunteggio(vettore_da_ordinare.get(j),vettore_da_ordinare.get(j-1));
               System.out.println("i="+i+", j="+j+"Sto scambiando:");
               vettore_da_ordinare.get(j).StampaPunteggio();
               System.out.println("con:");
               vettore_da_ordinare.get(j-1).StampaPunteggio();
            }
         }
         ////////////////DEBUG/
         /////////STAMPO TUTTO IL VETTORE
         System.out.println("i="+i+" Ho ordinato il vettore:");
         for(int f=0; f<vettore_da_ordinare.size(); f++)
         {
           System.out.print("elemento n° "+f+" = ");
           vettore_da_ordinare.get(f).StampaPunteggio();
         }
      }
   }
    //GOP: the next method orders the scores to be shown by DECREASING order, 
   //using an "insertion sort" algorithm
   static void OrdinaVettoreDiPunteggiDecrescenti(Vector<Punteggio> vettore_da_ordinare)
   {
      //li ordino in ordine crescente
      OrdinaVettoreDiPunteggiCrescenti(vettore_da_ordinare);
      //li scambio a due a due, fino ad arrivare a metà vettore
      for(int i=0; i<vettore_da_ordinare.size()/2; i++)
      {
         SwapPunteggio(vettore_da_ordinare.get(i),vettore_da_ordinare.get(vettore_da_ordinare.size()-1-i));
      }
   }
   
    //GOP: the next method swaps two scores
   static void SwapPunteggio(Punteggio p1, Punteggio p2)
   {
      String nome_temp;
      Integer punteggio_temp;
      
      //Java non supporta l'overloading degli operatori. Devo swappare il punteggio in maniera un po' complicata:
      nome_temp=p1.getNomeGiocatore();
      punteggio_temp=p1.getValorePunteggio();
      p1.setNomeGiocatore(p2.getNomeGiocatore());
      p1.setValorePunteggio(p2.getValorePunteggio());
      p2.setNomeGiocatore(nome_temp);
      p2.setValorePunteggio(punteggio_temp);
      
   }
   
   //GOP: method that reads the scores from a FILE--> totally to be changed, in order to use databases
   static Vector<Punteggio> LeggiStreamdiPunteggi(Scanner stream_in)
   {
      //ho bisogno di un vettore di stringhe
      Vector<String> vettore_di_stringhe = new Vector<String>();
      //e ho altresì bisogno di un vettore di punteggi
      Vector<Punteggio> vettore_di_punteggi = new Vector<Punteggio>();
      Integer k=0; //contatore 
      
      
      while(stream_in.hasNextLine())
      {
         vettore_di_stringhe.addElement(stream_in.nextLine());
         k++;
      }
      
      vettore_di_punteggi=LeggiVettoreDiStringhe(vettore_di_stringhe);
      
      return vettore_di_punteggi;
   }
   
   //GOP: method that writes the scores on a FILE--> totally to be changed, in order to use databases
   static void StampaVettorediPunteggiSuFile(PrintWriter stream_out, Vector<Punteggio> vettore_di_punteggi)
   {
      //ora li stampo nel file di output
      for(int i=0; i<vettore_di_punteggi.size(); i++)
      {
         //uso la funzione che riceve come argomento uno stream e stampa su quello stream
         vettore_di_punteggi.get(i).StampaPunteggio(stream_out);
         System.out.print("i = "+i+" Sto stampando: ");
         vettore_di_punteggi.get(i).StampaPunteggio();
      }
   }
   
   //GOP: method that writes the scores on a STRING VECTOR--> totally to be changed, in order to use databases
   static String StampaVettorediPunteggiSuStringa(Vector<Punteggio> vettore_di_punteggi)
   {
	  String s="";
      //ora li stampo nel file di output
      for(int i=0; i<vettore_di_punteggi.size(); i++)
      {
         //uso la funzione che riceve come argomento uno stream e stampa su quello stream
         s+=((i+1)+". "+vettore_di_punteggi.get(i).getNomeGiocatore()+"    "+vettore_di_punteggi.get(i).getValorePunteggio()+"\n");
      }
      System.out.println("Ho stampato la stringa: "+s);
      return s;
   }
   
   
   //GOP: method that reads the scores from a STRING VECTOR--> totally to be changed, in order to use databases
   //Funzione che salva dei punteggi desumendoli da un vettore di stringhe
   static Vector<Punteggio> LeggiVettoreDiStringhe(Vector<String> vettore_di_stringhe)
   {
      int k=0;
      Punteggio punteggio_temporaneo=new Punteggio();  //lo userò per memorizzare i punteggi nella funzione
      Vector<Punteggio> vettore_di_punteggi = new Vector<Punteggio>();
      //ora che ho letto tutte le stringhe, posso dividere stringhe e punteggi
      for(int i=0; i<vettore_di_stringhe.size(); i++)
      {
         String s=""; //stringa in cui metto l'intero
         String nome=""; //stringa in cui metto il nome del giocatore
         for(int j=0; vettore_di_stringhe.get(i).charAt(j)!='>'; j++)
         {
            if(vettore_di_stringhe.get(i).charAt(j)!='<')
            {
               nome+= vettore_di_stringhe.get(i).charAt(j);
            }
            k=j; //memorizzo il valore di j 
         }
         
         
         
         //adesso, fino alla fine della stringa
         
         for(int j=k; j!=vettore_di_stringhe.get(i).length(); j++)
         {
            if(Character.isDigit(vettore_di_stringhe.get(i).charAt(j)))
            {
               s+=vettore_di_stringhe.get(i).charAt(j);
            }
         }
         
         //devo ogni volta dichiarare un nuovo punteggio se voglio poterlo aggiungere al vettore
         punteggio_temporaneo=new Punteggio();
         //NOTA: PENSO CHE SIA UN MODO MOLTO POCO EFFICIENTE DI MEMORIZZARE
         //I DATI NEL VETTORE, PIÙ CHE ALTRO PERCHÉ MI VIENE IL SOSPETTO CHE IN
         //QUESTO MODO TUTTI I DATI NEL VETTORE SIANO "SPARSI" UN PO' IN GIRO
         //NELLA MEMORIA: CHIEDERE DELUCIDAZIONI AL PROF
         
         //imposto nome e valore di punteggio_temporaneo
         punteggio_temporaneo.setNomeGiocatore(nome);
         punteggio_temporaneo.setValorePunteggio(Integer.parseInt(s));
         System.out.print("Sto memorizzando ");
         punteggio_temporaneo.StampaPunteggio();
         
         //ora lo aggiongo al vettore
         vettore_di_punteggi.addElement(punteggio_temporaneo);
         System.out.print("i = "+i+" Ho memorizzato: ");
         vettore_di_punteggi.get(i).StampaPunteggio();
      }
      
      ////////////////DEBUG/
      /////////STAMPO TUTTO IL VETTORE
      for(int i=0; i<vettore_di_punteggi.size(); i++)
      {
         System.out.print("elemento n° "+i+" = ");
         vettore_di_punteggi.get(i).StampaPunteggio();
      }
      
      return vettore_di_punteggi;
   }
   
}