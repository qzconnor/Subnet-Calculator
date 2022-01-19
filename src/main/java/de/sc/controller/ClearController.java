package de.sc.controller;

import de.sc.views.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ClearController implements ActionListener {
    private MainView view;
    private boolean initTheme;

    private ArrayList<String> network;
    private String CIDR;
    private JPanel lastCenterPanel;


    public ClearController(MainView view, boolean initTheme){
        //Hand over the Main Window and set boolean to check if the theme should be set or not
        this.view = view;
        this.initTheme = initTheme;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //if initTheme = true then update the current theme
        if(initTheme){
            this.view.getConfigManager().setTheme(e.getActionCommand());
        }
        //Switch for both cases Clear or Restore
        switch (e.getActionCommand()){
           case "Clear":
               //Temporary save current values.
               this.lastCenterPanel = this.view.getCenterPane();
               this.network = this.view.getNetworkAddress().getStrings();
               this.CIDR = this.view.getCIDR();

               //Enable the restore button
               this.view.getRestoreBtn().setEnabled(true);

               //
               //Looping over the Text fields and clear the values and set the text fields to 0
               for (JTextField field : this.view.getNetworkAddress().getTextFields()){
                   field.setText("0");
               }
               //Clear the results field with empty space
               JPanel temp = new JPanel();
               temp.setBorder(BorderFactory.createEtchedBorder());
               this.view.setCenterPane(temp);
               break;
           case "Restore":
               //Looping over the text field except for the last one.
               //Set the text field value to the temporary saved values.
               for (int i = 0; i < this.view.getNetworkAddress().getTextFields().size()-1; i++) {
                   this.view.getNetworkAddress().getTextFields().get(i).setText(network.get(i));
               }
               //set back the temporary saved CIDR value
               this.view.getNetworkAddress().getCIDR().setText(this.CIDR);
               //if the min calculated once then set the previous calculation.
               if(this.lastCenterPanel != null){
                   this.view.setCenterPane(this.lastCenterPanel);
               }

               //Remove temporary values and set it back to null
               this.lastCenterPanel = null;
               this.network = null;
               this.CIDR = null;
               //Disable the restore Button
               this.view.getRestoreBtn().setEnabled(false);
               break;
        }



    }


}
