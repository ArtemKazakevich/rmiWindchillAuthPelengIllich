package main.View;

import main.Controller.Logging;

import javax.swing.*;

public class InfoGUI {


    public InfoGUI(String message) {
        Logging.infoMsg(message);
        JOptionPane.showMessageDialog(new JFrame(), message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

}
