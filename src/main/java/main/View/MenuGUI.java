package main.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGUI extends JMenuBar{

    private JMenu menu;
    private JMenuItem menuItemConfigurationPath;
    private JMenuItem menuItemExit;

    public MenuGUI() {
        init();
        configItemListeners();
    }

    private void init() {
        menu = new JMenu("Настройки");
        this.add(menu);

        //adding choose to JMenu
        menuItemConfigurationPath = new JMenuItem("Настроить путь сохранения файлов");
        menu.add(menuItemConfigurationPath);

        menuItemExit = new JMenuItem("Выход");
        menu.add(menuItemExit);
    }

    private void configItemListeners() {

        menuItemConfigurationPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
