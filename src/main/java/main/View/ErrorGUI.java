package main.View;

import javax.swing.*;

import main.Controller.Logging;

public class ErrorGUI {

        public ErrorGUI(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Ошибка!", JOptionPane.ERROR_MESSAGE);
        Logging.errorMsg(message);
        }

}
