/*
 *  De logica van het programma 
 */
package virus.host.db;

import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 *
 * @author Damian Bolwerk
 * 
 */
public class VirusLogica {

    /**
     * 
     * @param titel , de naam die in de tesktfield is ingevoerd.
     * @return inFile,de bestand
     */
     static HashMap<String,HashMap<Integer,Virus>> virus_identifier = new HashMap<>();
     static   HashMap<String, HashSet> host_identifier = new HashMap<>();
     static  ArrayList<Virus> gastheren = new  ArrayList<Virus> ();
    public static BufferedReader openBestand(String titel){
        try{
      BufferedReader  inFile = new BufferedReader(
            new FileReader(titel));
      return inFile;
        }
        catch (FileNotFoundException o){
                JOptionPane.showMessageDialog(null, "Er is iets misgegaan bij het vinden van de file");
                
                }
        catch (Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
        }
        return null;
            }
    /**
     * @return  path,de locatie van de gekozen bestand.
     * @exception FileNotFoundException de gekozen bestand pad kan niet gevonden wordten
     */
     public static String browseBestand(){
     JFileChooser chooser;
     String choosertitle = "Selecteer bestand om te lezen";
     String path ;
      try{
   chooser = new JFileChooser(); 
    chooser.setCurrentDirectory(new java.io.File("Documenten"));
    chooser.setDialogTitle(choosertitle);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    // disable the "All files" option.
    chooser.setAcceptAllFileFilterUsed(false);
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
      System.out.println("getCurrentDirectory(): " 
         +  chooser.getCurrentDirectory());
      System.out.println("getSelectedFile() : " 
         +  chooser.getSelectedFile());
      BufferedReader reader = new BufferedReader(
           new InputStreamReader(new FileInputStream(chooser.getSelectedFile()), "UTF-8"));
      File selectedFile = chooser.getSelectedFile();
        System.out.println( selectedFile.getName());
         path = selectedFile.getAbsolutePath(); 
         return path;
     } 
    else {
      System.out.println("No Selection ");
             } 
      }
       catch (FileNotFoundException o){
                JOptionPane.showMessageDialog(null, "Er is iets misgegaan bij het vinden van de file");
                
                }
        catch (Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
        }
        return null;
     }
      /**
     * Haalt de data die gebruikt wordt voor de programma uit het bestand en stopt het in hashmaps en Arraylists.
     * @param file , de bestand waaruit de data gehaald moet worden.
     * @param Virus_GUI , De frame waarin de data gezet moet worden.
     */
     public static void  dataExtract( BufferedReader file,Virus_GUI frame){
         ArrayList<String> classificatie = new  ArrayList<String>();
           ArrayList<Virus> Virussen = new  ArrayList<Virus>();
          String virus_string = "";
          String host_string = "";
          HashSet host_virussen = new HashSet();
          String virus_key ="";
          String virus_value="";
          String host_key ="";
          String host_value="";
         String line;
        try{
            line = file.readLine();
          while ( ( line = file.readLine()) != null) {
               String[] virus_eigenschappen = line.split("\t",-1);
                String virus_id = virus_eigenschappen[0];
                int virusID = Integer.parseInt(virus_id);
                String virus_name = virus_eigenschappen[1];
              String virus_classificatie = virus_eigenschappen[2];
               String host_id = virus_eigenschappen[7];
               String host_name = virus_eigenschappen[8];
               virus_string = virus_id +" "+"("+virus_name+")";
              host_string = host_id +" "+"("+host_name+")";
               if(virus_identifier.containsKey(virus_classificatie)  == false){
                   if(virus_classificatie.equals("") == false){
                     virus_identifier.put(virus_classificatie, new HashMap<>());
                   }
                   
               }
                    if(virus_classificatie.equals("") == false){
                        if(virus_identifier.get(virus_classificatie).keySet().contains(virusID)){
                            virus_identifier.get(virus_classificatie).get(virusID).setHost(host_string);
                        }
                         Virus virus_init = new Virus(virusID,virus_name,virus_classificatie);
                  virus_identifier.get(virus_classificatie).put(virusID, virus_init);
                    }
               
               if(host_identifier.containsKey(host_id) == false){
                    if(host_id.equals("") == false){
                     host_identifier.put(host_id, new HashSet());
                    }
               }
               else{
                   if(host_id.equals("") == false){
                       host_identifier.get(host_id).add(virus_id);
                   }
               }
          }
               guiDefault(frame);
        }
         catch(Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
         }
  }    
      /**
     * Maakt de default GUI van het gekozen bestand.
     * @param Virus_GUI , De frame waarin de data gezet moet worden.
     */
         public static void guiDefault(Virus_GUI frame){
             try{
         Set virus_classificatie = virus_identifier.keySet();
          Set host_ids =  host_identifier.keySet();
          JComboBox classificatie_box= frame.getClassification();
          for (int i = 0; i < virus_classificatie.size(); i++){
             classificatie_box.addItem(virus_classificatie.toArray()[i]);
            }
      
           JComboBox host1_box = frame.getHostId_1_box();
          for (int i = 0; i < host_ids.size(); i++){
              host1_box.addItem(host_ids.toArray()[i]);
        }
          JComboBox host2_box = frame.getHostId_2_box();
          for (int i = 0; i < host_ids.size(); i++){
              host2_box.addItem(host_ids.toArray()[i]);
        } 
            String virus_key= (String)frame.getClassification().getSelectedItem();
          Set virus_ids_clas = virus_identifier.get(virus_key).keySet();
            String host1 = (String) frame.getHostId_1_box().getSelectedItem();
            Set host_virus_1 = host_identifier.get(host1);
             Set<String> virus_intersect1 = new HashSet<>(virus_ids_clas);
            virus_intersect1.retainAll(host_virus_1);
               JTextArea lijst_1 = frame.getVirusLijst_1();
                String host2 = (String) frame.getHostId_2_box().getSelectedItem();
            Set host_virus_2 = host_identifier.get(host2);
            Set<String> virus_intersect2 = new HashSet<>(virus_ids_clas);
            virus_intersect2.retainAll(host_virus_2);
            JTextArea lijst_2 = frame.getVirusLijst_2();
            JTextArea host_overlap = frame.getOvereenkomst();
            Set<String> host_intersect = new HashSet<>(virus_intersect1);
             host_intersect.retainAll( virus_intersect2);
             System.out.println(virus_ids_clas+ " virussen met gekozen classificatie");
             System.out.println(host_virus_1+" Virussen van de gekozen host" );
               lijst_1.setText("");
            
            
            for(int i = 0; i< virus_intersect1.size();i++){
                lijst_1.append( (String)virus_intersect1.toArray()[i]);
                lijst_1.append("\n");
                
            }
            lijst_2.setText("");
            
              for(int i = 0; i< virus_intersect2.size();i++){
                lijst_2.append( (String)virus_intersect2.toArray()[i]);
                lijst_2.append("\n");
                
            }
               host_overlap.setText("");
             for(int i = 0; i< host_intersect.size();i++){
                lijst_2.append( (String)virus_intersect2.toArray()[i]);
                lijst_2.append("\n");
                
            }    
             /* Zorgt ervoor dat de gebruiker nadat de GUI gemaakt is
             * de verschilende onderdelen kan raadplegen.
             */
             frame.getClassification().setEnabled(true);
             frame.getHostId_1_box().setEnabled(true);
             frame.getHostId_2_box().setEnabled(true);
             frame.getRadioId().setEnabled(true);
             frame.getRadioClassificatie().setEnabled(true);
             frame.getRadioAantalHost().setEnabled(true);
             }
              catch (Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
        }
}
      /**
     *  Zorgt ervoor dat als de gebruiker een andere classificatie of organisme selecteerd
     * de virussen gezocht worden die overeenkomen met die classificatie of organisme.
     * @param Virus_GUI , De frame waarin de data gezet moet worden.
     * @param e, de Itemevent die opgetreden is.
     */  
     public static void comboBoxChange(Virus_GUI frame,ItemEvent e) {
         try{
         String virus_key= (String)frame.getClassification().getSelectedItem();
          Set virus_ids_clas = virus_identifier.get(virus_key).keySet();
            String host1 = (String) frame.getHostId_1_box().getSelectedItem();
            Set host_virus_1 = host_identifier.get(host1);
             Set<String> virus_intersect1 = new HashSet<>(virus_ids_clas);
            virus_intersect1.retainAll(host_virus_1);
               JTextArea lijst_1 = frame.getVirusLijst_1();
                String host2 = (String) frame.getHostId_2_box().getSelectedItem();
            Set host_virus_2 = host_identifier.get(host2);
            Set<String> virus_intersect2 = new HashSet<>(virus_ids_clas);
            virus_intersect2.retainAll(host_virus_2);
            JTextArea lijst_2 = frame.getVirusLijst_2();
            JTextArea host_overlap = frame.getOvereenkomst();
            Set<String> host_intersect = new HashSet<>(virus_intersect1);
             host_intersect.retainAll( virus_intersect2);
             System.out.println(virus_ids_clas+ " virussen met gekozen classificatie");
             System.out.println(host_virus_1+" Virussen van de gekozen host" );
               lijst_1.setText("");
            
            
            for(int i = 0; i< virus_intersect1.size();i++){
                lijst_1.append( (String)virus_intersect1.toArray()[i]);
                lijst_1.append("\n");
                
            }
            lijst_2.setText("");
            
              for(int i = 0; i< virus_intersect2.size();i++){
                lijst_2.append( (String)virus_intersect2.toArray()[i]);
                lijst_2.append("\n");
                
            }
               host_overlap.setText("");
             for(int i = 0; i< host_intersect.size();i++){
                lijst_2.append( (String)virus_intersect2.toArray()[i]);
                lijst_2.append("\n");
                
            }  
         }
          catch(Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
         }
     } 
      /**
     * Zorgt ervoor dat de gebruiker met behulp van radiobuttons de lijsten kunnen
     * sorteren op bepaalde criteria
     * @param Virus_GUI , De frame waarin de data gezet moet worden.
     * @param e, de Itemevent die opgetreden is.
     * @see Virus.compareTo.
     */  
     public static void JRadioButtonChange(Virus_GUI frame,ItemEvent e) {
         try{
             Enumeration elements = frame.getRadioGroup().getElements();
    while (elements.hasMoreElements()) {
      AbstractButton button = (AbstractButton)elements.nextElement();
      if (button.isSelected()) {
       String keuze = button.getText();
          Virus.setRadioSelect(keuze);
      }
    }
    JComboBox classificatie_box= frame.getClassification(); 
    String clas = (String)classificatie_box.getSelectedItem();
     HashMap <Integer, Virus> clas_virussen = virus_identifier.get(clas);
   
           JTextArea virus_lijst_1 = frame.getVirusLijst_1();
           JTextArea virus_lijst_2 = frame.getVirusLijst_2();
           JTextArea overlap= frame.getOvereenkomst();
           String[] ids1 = virus_lijst_1.getText().split("\n");
           String[] ids2 = virus_lijst_2.getText().split("\n");
           String[] overlap_id =overlap.getText().split("\n");
               gastheren.clear();
               if ( ids1!= null  ){
                   if (virus_lijst_1.getText().length() != 0){
                for (int i = 0; i < ids1.length ; i++ ){
                   Virus virus_1= clas_virussen.get(ids1[i]);
                    gastheren.add(virus_1);
                }
                System.out.println(gastheren.isEmpty());
                 Collections.sort(gastheren);
                 virus_lijst_1.setText("");
         for(int i = 0 ; i < gastheren.size();i++){
             int virus_id_sorted = gastheren.get(i).getId();
             String virus_id_sort= String.valueOf(virus_id_sorted );
             virus_lijst_1.append(virus_id_sort);
             virus_lijst_1.append("\n");
         }
               }
               }
           gastheren.clear();
           if ( ids2!= null  ){
               if(virus_lijst_2.getText().length() != 0 ){
                for (int i = 0; i < ids2.length ; i++ ){
                   Virus virus_2= clas_virussen.get(ids2[i]);
                    gastheren.add(virus_2);
                }
                System.out.println(gastheren.isEmpty());
                 Collections.sort(gastheren);
                 
                 System.out.println("hoi");
                 virus_lijst_2.setText("");
         for(int i = 0 ; i < gastheren.size();i++){
             int virus_id_sorted = gastheren.get(i).getId();
             String virus_id_sort= String.valueOf(virus_id_sorted );
             virus_lijst_2.append(virus_id_sort);
             virus_lijst_2.append("\n");
         }
           }
     }
         gastheren.clear();
          if (    overlap_id!= null ){
              if(overlap.getText().length() != 0){
                for (int i = 0; i <overlap_id.length ; i++ ){
                   Virus virus_3= clas_virussen.get(overlap_id[i]);
                    gastheren.add( virus_3);
                }
                System.out.println(gastheren.isEmpty());
                 Collections.sort(gastheren);
                 
                 overlap.setText("");
         for(int i = 0 ; i < gastheren.size();i++){
             int virus_id_sorted = gastheren.get(i).getId();
             String virus_id_sort= String.valueOf(virus_id_sorted );
             overlap.append(virus_id_sort);
            overlap.append("\n");
         }
          }
          }
           }  
catch(Exception exep){
             JOptionPane.showMessageDialog(null, "De volgende error is opgetreden" + exep);
         }
}
              
}


