package it.uniud.robertomariarosati.giocooperazioni;

class LibreriaAleatoria
{

   //FUNZIONE CHE GENERA UN RISULTATO RANDOM INTERO
   static int GeneraRandomTraEstremi(int min, int max)
   {
      return min+(int)((max-min)*Math.random());
   }
   
   //devo implementare l'algoritmo di box-muller, per generare un v.c. normale
   static double VariabileNormaleStandard()
   {
      //siano u1 e u2 due variabili uniformi tra zero e uno
      //allora R*sen(theta) e R*cos(theta) sono due v.c. normali 
      //di media 0 e varianza 1 
      //dove R^2=-2ln(u1) e theta=2*pi*u2
      
      //a noi basta una delle due (usiamo il seno)
      
      double u1, u2, z;
      u1=Math.random();
      u2=Math.random();
      
      z=Math.sqrt(-2*Math.log(u1))*Math.sin(2*Math.PI*u2);
      return z;
   } 
   
   static double VariabileNormale(double media, double varianza)
   {
      double z=VariabileNormaleStandard();
      return z*Math.sqrt(varianza)+media;
   } 

}