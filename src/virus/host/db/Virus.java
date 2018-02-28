/*
 * De getters en Setters van het progamma en de aangepaste compareTo methode.
 */
package virus.host.db;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Damian Bolwerk
 */
public class Virus implements Comparable<Virus> {
    private Integer id;
    private String soort;
    private  HashSet hostSet = new HashSet();
    private String classificatie;
    static String radio_select ="Classificatie";
   
    
    public Virus(int pid,String psoort,  String pclassificatie){
    id = pid;
    soort = psoort;
    classificatie =  pclassificatie;
}
    public Integer getId(){
    return this.id;
}
    
   public String getSoort(){
    return this.soort;
}
   public  HashSet getHost(){
    return this.hostSet;
   }
   
   public String getClassificatie(){
    return this.classificatie;
}
   public void setId(int pid){
   id = pid;
}
   public void setSoort(String psoort){
   soort = psoort;
}
   public void setHost( String hostId){
    hostSet.add( hostId);
}
   
   public void setClassificatie(String pclassificatie){
  classificatie = pclassificatie;
}
   public static void setRadioSelect( String PradioSelect){
    radio_select = PradioSelect;
}
  /**
 @param virussen, de virus objecten die vergeleken moet worden op basis van een bepaalde eigenschap.
 * @return 0 als de virus eigenschap gelijk zijn, -1 als de virus eigenschap waarmee je vergelijkt groter is, en +1 als de virus eigenschap warrmee je vergelijkt kleiner is.
 */
    @Override
       public int compareTo(Virus virussen){
        switch(radio_select){
            
            case "Classificatie":
               return this.classificatie.toLowerCase().compareTo(virussen.classificatie.toLowerCase());
            
             case "id":
               return this.id.compareTo(virussen.id);
               
                case "Aantal_hosts":
               return Integer.valueOf(this.hostSet.size()).compareTo(Integer.valueOf(virussen.hostSet.size()));
            
                default:
                    break;
        }
        return 0;    
    }
}
