package de.sc.views;

import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import de.sc.controller.CalculateController;
import de.sc.controller.ClearController;
import de.sc.utils.IpField;
import de.sc.utils.ConfigManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MainView extends JFrame {

    private ConfigManager configManager;

    private JMenuBar mainMenuBar;
    private JMenu mainMenu;

    private IpField networkAddress;
    //private IpField subnetMask;

    private JButton calculateBtn;
    private JButton clearBtn;
    private JButton restoreBtn;

    //Panels
    private JPanel contentPanel;
    private JPanel ipFieldsPanel;
    private JPanel resultPanel_center;

    public MainView(){
        configManager = new ConfigManager(this);
        init();
    }
    /**
     * Initialise the Frame(Window).
     * Setting default values.
     */
    private void init(){
        //Press on X close the window / App
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set the window title
        setTitle("Subnet Calculator");
        //set the window size to 500x385
        setSize(500,385);
        //set Window location to the middle
        setLocationRelativeTo(null);
        //set window is not resizable
        setResizable(false);

        //Load image icon from Image off resource
        try {
            BufferedImage iconForMyButton = ImageIO.read(ClassLoader.getSystemResourceAsStream("logo32x32.png"));
            setIconImage(iconForMyButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //add and init the components
        initComponents();

        //check if properties are in the config
        //set the default values to the config values
        if(configManager.getConfig().getProperty("address") != null){
            setNetworkAddressField(configManager.getConfig().getProperty("address"));
        }
        if(configManager.getConfig().getProperty("cidr") != null){
            networkAddress.getCIDR().setText(configManager.getConfig().getProperty("cidr"));
        }

        //Show the window
        setVisible(true);
    }

    private void initComponents(){
        //HEAD
        //Set JMenuBar for themes
        setJMenuBar(this.mainMenuBar = new JMenuBar());
        //adding JMenu calling Themes
        this.mainMenu = new JMenu("Themes");
        //Set Hotkey (T) for the themes Menu
        this.mainMenu.setMnemonic(KeyEvent.VK_T);
        //Looping through available themes and add to the menu.
        for (FlatAllIJThemes.FlatIJLookAndFeelInfo info : FlatAllIJThemes.INFOS) {
            JMenuItem menuItem = new JMenuItem(info.getName());
            menuItem.addActionListener(new ClearController(this , true));
            mainMenu.add(menuItem);
        }
        //Set Rows and Cols for popup menu.
        this.mainMenu.getPopupMenu().setLayout(new GridLayout(21,0));
        this.mainMenuBar.add(this.mainMenu);

        //CONTENT
        this.contentPanel = new JPanel(new BorderLayout());
        //Adding spacing around the content
        this.contentPanel.setBorder(new EmptyBorder(5, 25, 25, 25));

        //Set ip fields with layout of BorderLayout
        this.ipFieldsPanel = new JPanel(new BorderLayout());

        //init the Ip field component
        // Label = IPv4 Address
        // Default text = 0
        this.networkAddress = new IpField("IPv4 Address", "0");

        //add the ip field to the main panel
        this.ipFieldsPanel.add(this.networkAddress.getMainPanel(), BorderLayout.NORTH);

        //Adding the components for the buttons
        JPanel ipUtilsPanel = new JPanel(new FlowLayout());

        //Adding Calculate button
        this.calculateBtn = new JButton("Calculate");
        this.calculateBtn.addActionListener(new CalculateController(this));
        ipUtilsPanel.add(this.calculateBtn);

        ClearController clearController = new ClearController(this, false);

        //Adding Clear button
        this.clearBtn = new JButton("Clear");
        clearBtn.addActionListener(clearController);

        ipUtilsPanel.add(clearBtn);

        //Adding Restore button
        this.restoreBtn = new JButton("Restore");
        restoreBtn.addActionListener(clearController);
        //set it to default disabled
        restoreBtn.setEnabled(false);

        //add it to the panel
        ipUtilsPanel.add(restoreBtn);
        this.ipFieldsPanel.add(ipUtilsPanel, BorderLayout.SOUTH);
        this.contentPanel.add(this.ipFieldsPanel, BorderLayout.NORTH);

        //RESULTS

        //Adding result panels
        JPanel resultPanel = new JPanel(new BorderLayout());
        JPanel resultPanel_top = new JPanel(new BorderLayout());
        //adding Results panel to the left
        resultPanel_top.add(new JLabel("Results"), BorderLayout.WEST);

        //Output fields
        //adding an empty field with a border
        resultPanel_center = new JPanel();
        resultPanel_center.setBorder(BorderFactory.createEtchedBorder());

        resultPanel.add(resultPanel_center, BorderLayout.CENTER);
        resultPanel.add(resultPanel_top, BorderLayout.NORTH);
        this.contentPanel.add(resultPanel, BorderLayout.CENTER);
        getContentPane().add(contentPanel);
    }


    public ConfigManager getConfigManager() {
        return configManager;
    }

    public JPanel getResultPanelCenter() {
        return resultPanel_center;
    }

    public void setCenterPane(JPanel panel){
        //getting the parent
        Container parent = this.resultPanel_center.getParent();
        //get the index of the output panel
        int index = parent.getComponentZOrder(this.resultPanel_center);
        //remove the output panel
        parent.remove(this.resultPanel_center);
        //replace and add it back.
        panel.setBorder(BorderFactory.createEtchedBorder());
        this.resultPanel_center = panel;
        parent.add(this.resultPanel_center, index);

        //repaint and update
        parent.validate();
        parent.repaint();
    }

    public JPanel getCenterPane() {
        return this.resultPanel_center;
    }

    public IpField getNetworkAddress() {
        return networkAddress;
    }

    public ArrayList<JTextField> getNetworkAddressFields() {
        //Create copy of the network address fields
        ArrayList<JTextField> copy = new ArrayList<>(networkAddress.getTextFields());
        //and remove the last text field.
        copy.remove(copy.size()-1);
        return copy;
    }
    public void setNetworkAddressField(String address) {
        //Ssplit the address in on "."
        String[] parts = address.split("\\.");
        //Loop over the parts and set it to the fitting part.
        for (int i = 0; i < getNetworkAddressFields().size() ; i++) {
            getNetworkAddressFields().get(i).setText(parts[i]);
        }
    }
    //Get cidr from the text field
    public String getCIDR(){
        return networkAddress.getCIDR().getText();
    }

    public JButton getRestoreBtn() {
        return restoreBtn;
    }
}
