package it.uniud.robertomariarosati.giocooperazioni;

//GOP: class to manage the time, in particular to convert a given time 
//in milliseconds to notation in h,m,s

public class TempoOrario 
{

   //nota:
   //millisecondi in un:
   //secondo=1000
   //minuto=60*10^3=60000
   //ora=3.6*10^6=3600000
   //giorno=8.6*10^7=864000000
   
   private final int ms_in_un_giorno=86400000;
   private final int ms_in_un_ora=3600000;
   private final int ms_in_un_minuto=60000;
   private final int ms_in_un_secondo=1000;
   
   private int giorni;
   private int ore;
   private int minuti;
   private int secondi;
   private int millisecondi;
      
   //creo metodo che converte un valore intero in millisecondi in un tempo orario
   
   //costruttori
   TempoOrario()
   {
      giorni=0;
      ore=0;
      minuti=0;
      secondi=0;
      millisecondi=0;
   }
   
   TempoOrario(int g, int h, int m, int s, int ms)
   {
      giorni=g;
      ore=h;
      minuti=m;
      secondi=s;
      millisecondi=ms;
   }
   
   //GOP: this method converts a time in milliseconds in a time of type "TempoOrario", with day,hours, minutes, seconds, milliseconds
   //convertitore da millisecondi
   void ConvertiDaMillisecondi(int tempo_in_millisecondi)
   {
      //ottengo il numero di giorni
      giorni=tempo_in_millisecondi/ms_in_un_giorno;
      //elimino la parte già coteggiata nei giorni
      tempo_in_millisecondi=tempo_in_millisecondi%ms_in_un_giorno;
      
      //ottengo il numero di ore
      ore=tempo_in_millisecondi/ms_in_un_ora;
      //elimino la parte già coteggiata nelle ore
      tempo_in_millisecondi=tempo_in_millisecondi%ms_in_un_ora;
      
      //ottengo il numero di minuti
      minuti=tempo_in_millisecondi/ms_in_un_minuto;
      //elimino la parte già coteggiata nei minuti
      tempo_in_millisecondi=tempo_in_millisecondi%ms_in_un_minuto;
      
      //ottengo il numero di secondi
      secondi=tempo_in_millisecondi/ms_in_un_secondo;
      //elimino la parte già coteggiata nei minuti
      tempo_in_millisecondi=tempo_in_millisecondi%ms_in_un_secondo;
      
      //ora dovrebbero essermi rimasti solo i millisecondi
      millisecondi=tempo_in_millisecondi;
   }
   
   //GOP: this method prints the time
   void StampaTempoOrario()
   {
      System.out.println(giorni+" g, "+ore+":"+minuti+":"+secondi+"."+millisecondi);
   }
   
   //GOP: this method prints the time without showing days and hours and milliseconds (this method is used in the app)
   void StampaTempoMinutieSecondi()
   {
      if(secondi!=0)
         System.out.println(minuti+":"+secondi);
      else
         System.out.println(minuti+":00");
   }
   
   //GOP: this method prints the time into a string
   String StampaTempoMinutieSecondiSuStringa()
   {
      String s=String.valueOf(minuti);
      s+=":";
      if(Math.abs(secondi)<10)
    	  s+="0"+String.valueOf(secondi);
      else
    	  s+=String.valueOf(secondi);
      
      return s;  
   }

}
