package de.sc.controller;

import de.sc.models.CalculationModel;
import de.sc.utils.SubnetUtils;
import de.sc.views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateController implements ActionListener {

    private MainView view;
    private CalculationModel model;

    public CalculateController(MainView view){
        //set main window
        this.view = view;
        //Init data model.
        this.model = new CalculationModel();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //check if the network address is valid
        if(!checkValid()) return;

        //Calculate the subnet information
        SubnetUtils.SubnetInfo info = model.calculate(view.getNetworkAddress().bindTogether(), view.getCIDR());

        ArrayList<String> lines = new ArrayList<>();
        lines.add("Adresse: " + info.getAddress());
        lines.add("Netzmaske: " + info.getNetmask()  + " = " + view.getCIDR());
        lines.add("Wildcard: " + info.getInvertedNetmask());

        lines.add("------------------");

        lines.add("Netzwerk: " + info.getNetworkAddress() + "/" + view.getCIDR());
        lines.add("HostMin: " +info.getLowAddress());
        lines.add("HostMax: " + info.getHighAddress());
        lines.add("Broadcast: " + info.getBroadcastAddress());
        lines.add("Hosts/Netz: " +  info.getAddressCount());


        JPanel result = new JPanel(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);




        area.setText(String.join("\n", lines));

        result.add(area, BorderLayout.CENTER);

        //result.add(new JLabel("Netzmaske: " + info.getNetmask()  + " = " + view.getCIDR()));

        this.view.setCenterPane(result);




        //Try to store the values into the config. and save it.
        try {
            view.getConfigManager().saveProperty("address", view.getNetworkAddress().bindTogether());
            view.getConfigManager().saveProperty("cidr", view.getCIDR());
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
    //Validation check
    private boolean checkValid(){
        //convert ArrayList to Array and check all entries of matching the condition
        //field value must be under 255
        JTextField[] nwFields = view.getNetworkAddressFields().toArray(new JTextField[0]);
        return Arrays.stream(nwFields).allMatch(jTextField -> Integer.parseInt(jTextField.getText()) < 255);
    }



}
