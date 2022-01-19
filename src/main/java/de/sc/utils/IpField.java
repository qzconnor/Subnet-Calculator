package de.sc.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class IpField {

    private final JLabel label;
    private ArrayList<JTextField> textFields;
    private JPanel mainPanel;
    private JPanel fieldPanel;

    public IpField(String label, String defaultText){
        //Init the default text
        //like 0 0 0 0 => 0.0.0.0
        this.label = new JLabel(label);
        //Init the textFields ArrayList
        this.textFields = new ArrayList<>();
        //init the Panel for all Components
        this.mainPanel = new JPanel();
        //Set the layout Manager to "BorderLayout"
        mainPanel.setLayout(new BorderLayout());

        //Init the Panel for the text fields
        this.fieldPanel = new JPanel();
        //Set the layout Manager to "FlowLayout"
        this.fieldPanel.setLayout(new FlowLayout());
        //Adding the Label (Default text) to the mainPanel
        mainPanel.add(this.label, BorderLayout.NORTH);
        //Call function with the default text.
        generate(defaultText);
    }

    //Generate Components
    private void generate(String defaultText){

        //Adding 4 Textfields to the Array
        for (int i = 0; i < 4; i++) {
            //Init a text field.
            JTextField tf = new JTextField();
            //set the default text.
            tf.setText(defaultText);
            //call the function to set properties
            setProperties(tf);
            //adding to arraylist and to the fields panel
            this.fieldPanel.add(tf);
            this.textFields.add(tf);

        }
        //Adding the / Seperator
        this.fieldPanel.add(new JLabel("/"));
        //Init a text field.
        JTextField cdir = new JTextField();
        //call the function to set properties
        setProperties(cdir);
        //adding to arraylist and to the fields panel
        this.fieldPanel.add(cdir);
        this.textFields.add(cdir);
        //Adding the fields panel to the main panel to the bottom border
        this.mainPanel.add(this.fieldPanel,BorderLayout.SOUTH);
    }

    //Function to set the Properties of the Text field.
    private void setProperties(JTextField tf){
        //Center the text in the Text field.
        tf.setHorizontalAlignment(JTextField.CENTER);
        //Check for valid input
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key input detected: " + e.getKeyChar());
                //Check if there are only 3 chars / And check for input is a Number
                if (tf.getText().length() >= 3 || !Character.isDigit(e.getKeyChar())) {
                    //Prevent if wrong input
                    e.consume();
                }

            }
        });
        //Adding focus listener to the text field.
        tf.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //if clicked in the text field select the complete text.
                tf.setSelectionStart(0);
                tf.setSelectionEnd(tf.getText().length());
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        //setting size for the text field.
        tf.setPreferredSize(new Dimension(78,35));
    }

    //Getter for the MainPanel
    public JPanel getMainPanel() {
        return mainPanel;
    }

    //Getter for the list of Text fields
    public ArrayList<JTextField> getTextFields() {
        return textFields;
    }


    //Getter for the current text in the text fields
    public ArrayList<String> getStrings(){
        //init a ArrayList to store all values
        ArrayList<String> strings = new ArrayList<>();
        //Looping over the text fields except the last one
        for (int i = 0; i < this.textFields.size()-1; i++) {
            //Adding it to the ArrayList from the current index
            strings.add(this.textFields.get(i).getText());
        }
        //Return the list full of strings
        return strings;
    }

    //Combine the strings in the List of getStrings()
    public String bindTogether(){
        //Join the strings together with "." and convert the List to an Array of type Sting
        return String.join(".",  getStrings().toArray(new String[0]));
    }

    public JTextField getCIDR(){
        return this.textFields.get(this.textFields.size()-1);
    }

}
