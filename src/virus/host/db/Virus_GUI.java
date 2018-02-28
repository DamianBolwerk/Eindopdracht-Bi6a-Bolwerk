/*
 Dit is een applicatie die in staat is om tsv te lezen van virushostdb en het mogelijk
maakt om voor elke host een lijst met virussen te genereren die hun kunnen infecteerd.
Met deze applicatie kan ook gekeken worden welke virussen tussen 2 hosten overeenkomen.
De virussen kunnen gefilterd worden met zijn classificatie en het is mogelijk om te orderen met
de virus id , classificatie en de aantal hosts die hun infecteren.

De programma heeft nu een te uitgebreide classificatie lijst waardoor het zelfden voorkomt
dat de virussen van een bepaalde classificatie overeenkomt met de virussen van een bepaalde
host. Om die reden wordt er steeds geprint welke virussen er bij de gekozen classificatie hoort
en welke virussen de gekozen host heeft.
 */
package virus.host.db;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Damian Bolwerk
 * @version 1.0
 * @since 28-2-2018
 */
public class Virus_GUI extends JFrame implements ActionListener,ItemListener  {
    
    private JPanel classification,host,radio,lijsten,zoek,overlap;
    private JTextField bestandField;
    private String[] viralClassification,hostId_1,hostId_2;
    private JComboBox viralClassification_box,hostId_1_box,hostId_2_box;
    private JRadioButton id,Classificatie,Aantal_hosts;
    private ButtonGroup sort;
    private JTextArea virusLijst_1,virusLijst_2,overeenkomst;
    private JScrollPane virusLijst_1_scroll,virusLijst_2_scroll,overeenkomst_scroll;
    private JLabel  bestandField_label,viralClassification_label,hostId_label,
            radio_label,virusLijst_label,overeenkomst_label;
    private JButton search,browse;
    private GridBagConstraints gbc = new GridBagConstraints() ;

    /**
     * @param args the command line arguments
     * Maakt de frame van de programma.
     */
    public static void main(String[] args) {
       Virus_GUI frame = new  Virus_GUI();
        frame.setSize(800, 700);
        frame.setTitle("Virus overlap search");
        frame.createGUI();
        frame.show();
        
    }
     /**
     * Maakt de GUI van de programma.
     */
     public void createGUI(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new GridBagLayout() );
         zoek = new JPanel();
        bestandField_label = new JLabel ("vul hier de filelocatie of URL in");
        bestandField= new JTextField();
        bestandField.setColumns(25);
        browse = new JButton(" browse");
        browse.addActionListener(this);
        search = new JButton("open");
        search.addActionListener(this);
        zoek.add( bestandField_label);
        zoek.add( bestandField);
        zoek.add(browse);
        zoek.add(search);
        gbc.gridx=1;
        gbc.gridy=1;
        window.add(zoek ,gbc);
        
        classification = new JPanel();
        viralClassification_label = new JLabel ("Viral classification");
        viralClassification_box = new JComboBox();
        viralClassification_box.setPreferredSize(new Dimension(300,20));
        viralClassification_box.addItemListener(this);
         viralClassification_box.setEnabled(false);
        classification.add( viralClassification_label);
        classification.add(viralClassification_box);
        gbc.gridx=1;
        gbc.gridy=2;
        window.add(classification,gbc);
        
        host= new JPanel();
        hostId_label = new JLabel("Host ID");
        hostId_1_box= new JComboBox();
        hostId_1_box.setPreferredSize(new Dimension(300,20));
        hostId_1_box.addItemListener(this);
        hostId_1_box.setEnabled(false);
        hostId_2_box= new JComboBox();
        hostId_2_box.setPreferredSize(new Dimension(300,20));
        hostId_2_box.addItemListener(this);
        hostId_2_box.setEnabled(false);
        
        host.add(hostId_label);
        host.add(hostId_1_box);
        host.add(hostId_2_box);
        gbc.gridx=1;
        gbc.gridy=3;
        window.add(host,gbc);
        
        radio = new JPanel ();
        radio_label = new JLabel("Sorteren");
        sort = new ButtonGroup();
        id = new JRadioButton("ID", true);
        id.addItemListener(this);
        id.setEnabled(false);
        Classificatie = new JRadioButton("Classificatie", false);
        Classificatie.addItemListener(this);
        Classificatie.setEnabled(false);
        Aantal_hosts = new JRadioButton("Aantal hosts", false);
        Aantal_hosts.addItemListener(this);
        Aantal_hosts.setEnabled(false);
        sort.add(id);
        sort.add(Classificatie);
        sort.add(Aantal_hosts);
        radio .add(radio_label);
        radio .add(id);
        radio .add(Classificatie);
        radio .add(Aantal_hosts );
        gbc.gridx=1;
        gbc.gridy=4;
        window.add(radio ,gbc);
        
        lijsten= new JPanel();
        virusLijst_label = new JLabel("Viruslijst");
        virusLijst_1 = new JTextArea();
        virusLijst_1 .setColumns(20);
        virusLijst_1 .setRows(10);
        virusLijst_1_scroll = new JScrollPane (virusLijst_1); 
        virusLijst_1_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        virusLijst_1.setLineWrap(true);
        virusLijst_1.setWrapStyleWord(true);
        virusLijst_2 = new JTextArea();
        virusLijst_2 .setColumns(20);
        virusLijst_2 .setRows(10);
        virusLijst_2_scroll = new JScrollPane (virusLijst_2); 
        virusLijst_2_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        virusLijst_2.setLineWrap(true);
        virusLijst_2.setWrapStyleWord(true);
        lijsten.add(virusLijst_label);
        lijsten.add(virusLijst_1_scroll);
        lijsten.add(virusLijst_2_scroll);
        gbc.gridx=1;
        gbc.gridy=5;
        window.add(lijsten,gbc);
            
        overlap = new JPanel ();
        overeenkomst = new JTextArea();
        overeenkomst .setColumns(20);
        overeenkomst .setRows(10);
        overeenkomst_scroll = new JScrollPane (overeenkomst); 
        overeenkomst_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        overeenkomst.setLineWrap(true);
        overeenkomst.setWrapStyleWord(true);
        overlap.add(overeenkomst_scroll);
        gbc.gridx=1;
        gbc.gridy=6;
        window.add(overlap,gbc);
           }
     /**
     Deze methode zorgt ervoor dat als er op de browse knop gedrukt wordt er een
     bestand geselecteerd kan worden  en als er op de search knop gedrukt wordt
     de bestand wordt geopend. De methodes die worden aangeroepen zorgen ervoor
     dat de bruikbare data uit de bestand in datastructuren wordt opgeslagen.
     @param  event , de knop die wordt ingedrukt.
     @param ItemEvent , wordt aangeroepen als een combobox of radiobutton veranderd wordt
     @see VirusLogica.openBestand(), VirusLogica.browseBestand(),VirusLogica.dataExtract()
     */
      public void actionPerformed (ActionEvent event) {
    if(event.getSource() == search  ){
       BufferedReader bestand= VirusLogica.openBestand(bestandField.getText());
       if (bestand.equals(null)){
           System.out.println("Er is iets mis gegaan met het vinden van uw bestand");
       }
   VirusLogica.dataExtract(bestand,this);
      }
    if(event.getSource() == browse  ){
       String bestand_naam = VirusLogica.browseBestand();
        if (bestand_naam == null){
           System.out.println("Er is iets mis gegaan met het zoeken van uw bestand");
       }
        else{
       bestandField.setText(bestand_naam);
        }
       }
    }
         public void itemStateChanged(ItemEvent e) {
     if (viralClassification_box.isEnabled()){
    if (e.getSource()instanceof JComboBox) {
        if(e.getStateChange()== ItemEvent.SELECTED){
        VirusLogica.comboBoxChange(this,e);
        }
    }
     if (e.getSource()instanceof JRadioButton) {
          if(e.getStateChange()== ItemEvent.SELECTED){
        VirusLogica.JRadioButtonChange(this,e);
          }
    }
       }
       }
       public JComboBox getClassification(){
    return this.viralClassification_box;
}
        public JComboBox getHostId_1_box(){
    return this.hostId_1_box;
}
         public JComboBox getHostId_2_box(){
    return this.hostId_2_box;
}
          public JTextArea getVirusLijst_1(){
    return this.virusLijst_1;
}
           public JTextArea getVirusLijst_2(){
    return this.virusLijst_2;
}
           public JTextArea getOvereenkomst(){
    return this.overeenkomst;
}
           public  JRadioButton getRadioId(){
    return this.id;
}
            public  JRadioButton getRadioClassificatie(){
    return this.Classificatie;
}
            public  JRadioButton getRadioAantalHost(){
    return this.Aantal_hosts;
}
             public  ButtonGroup getRadioGroup(){
    return this.sort;
}
}