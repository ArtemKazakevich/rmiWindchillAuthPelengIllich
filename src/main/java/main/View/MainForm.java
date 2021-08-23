package main.View;

import main.Controller.ConnectionWind;
import main.Controller.Logging;
import main.Controller.StateTranslator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame{
//    private JButton btnPerformFind;
    private JPanel rootPanel;
    private JButton saveButton;
    private JList listWTDocs;
    private JList listState;
    private JButton cancelButton;
    private JLabel textAbove;
    private JLabel label;
//    private JTextField textFieldIzdelie;
    private String strFind;
    private List<String> result;
//    private JTextField textFieldKonechnoeIzd;
//    private JTextField textFieldVersijaPO;
//    private JTextField textFieldMicroscheme;


    private DefaultListModel modelListWTDocs;
    private DefaultListModel modelListStates;
    private int i=0;

  //  String perPrim = "";
  //  String konIzd = "";


    /**
     * TODO: избавиться от костыля.
     * Смысл костыля: явный вызов авторизационной формы Windchill.
     * Это надо для того, чтобы сформировать правильно ссылку для скачивания объекта WTDocument.
     * Необходимо явно проходить авторизацию до вызова метода, формирующего ссылку на файл
     * иначе формируется неправильная ссылка, потому что Windchill не дает доступ к файлу, пока
     * не будет пройдена авторизация. Механизм явного вызового авторизации пока не ясен.
     */
    public MainForm(String perPrim, String konIzd) { //nothing was here
        //костыль для явного вызова авторизации

       // setUndecorated(true);
    //    getRootPane().setWindowDecorationStyle(JRootPane.NONE);



        try {
            ConnectionWind.getInstance().getFileWTDoc("THISTEXTWILLNEVERBEFOUND");
        } catch (Exception ex) {
            System.out.println("ERROR: no login");
           Logging.errorMsg(ex.getMessage());
            ex.printStackTrace();
            System.exit(0); //выход из программы, если мы закрываем окно авторизации без ввода каких либо данных

        }

        config();
        setListeners(perPrim, konIzd);// nothing was here
    }

    private void config() {
        //init
        modelListWTDocs = new DefaultListModel();
        modelListStates = new DefaultListModel();

        //Main JFrame settings
        this.setTitle("Сохранение ПО");
        //this.setJMenuBar(new MenuGUI());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/4-this.getSize().width/4, dim.height/3-this.getSize().height/3);

        setVisible(true);
        setSize(600,400);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //rest gui elements settings
 //       label.setHorizontalAlignment(JLabel.CENTER); ----- UNCOMMEEEEENTTTT or maybe not!!!!!!!!!!!!!!
        saveButton.setEnabled(false);
        listWTDocs.setModel(modelListWTDocs);
        listState.setModel(modelListStates);
            }

    //TODO поля версии и позиции сделать по усмотрению
    // TODO пройтись по PerformFind (element, fileName)
    private void setListeners(final String perPrim, final String konIzd) { // todo parameters here



        //найти

                String element = "";

                saveButton.setEnabled(false);
                modelListWTDocs.clear();
                modelListStates.clear();

                 String ispoln = perPrim;
                String izd = konIzd;


        if (ispoln.length() == 0 || izd.length() == 0) {
                    // || microscheme.length() == 0 || versijaPO.length() == 0
                    new ErrorGUI("Заполните все поля!");
                } else if (ispoln.length() > 0 && izd.length() > 0) { // full check was here, now half removed

//                    String output =  konechnoeIzdelie + "_" + pervichnayaPrim + "_" + microscheme +  "_" + versijaPO;
//                    strFind = output;

                    //ЛОГИ

                    Logging.infoMsg("Коренная папка - " + ispoln.substring(0,4));
                    Logging.infoMsg("Исполнение - " + ispoln);
                    Logging.infoMsg("Конечное изделие - " + izd);
//                    Logging.infoMsg("Позиционное обозначение - " + microscheme);
//                    Logging.infoMsg("Версия ПО - " + versijaPO);

                    //ЛОГИ ВЫШЕ

                    //отображение результатов поиска

                    try {
                        result = ConnectionWind.getInstance().getWTDocsListUsingAttrs(ispoln, izd);
                        Logging.infoMsg("РЕЗУЛЬТАТ ПОИСКА - " + result);

                        textAbove.setText("<html>В архиве ПО найдены следующие файлы прошивок для программируемой платы " + konIzd + "<br> (изделие - " + perPrim+ "). Проверьте соответствие файлов ведомости носителей. </html>");

                        //NEW

                        if (result.size() == 0)

                        {
                            System.out.println("ERROR: no files!");
                            System.exit(1);
                        }

                        //NEW ABOVE

                        for (int i =0; i< result.size(); i++) {
                            element = result.get(i);
                            Logging.infoMsg("Имя " + i + "-го файла - " + element);
                            String fileName = element.split("%")[0];
                            String state = StateTranslator.translateState(element.split("%")[1]);
                            modelListWTDocs.addElement(fileName);
                            modelListStates.addElement(state);
                            if (state.equals("Выпущено")) {
                                saveButton.setEnabled(true);
                            } else {
                                new InfoGUI("Невозможно сохранить. Переведите состояние объекта в ВЫПУЩЕНО");
                                saveButton.setEnabled(false);
                                //doesn't allow to download a version in progress
                            }
                            Logging.infoMsg("Состояние объекта - " + state);

                        }


                    } catch (Exception ex) {
                        new ErrorGUI("Ни один файл не найден!");
                    }

                };

        //сохранить
        //Read String elements from modelListWTDocs (getting WTDocument name)
        //and download the files
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (int i=0; i<modelListWTDocs.size(); i++) {
                    String element = (String) modelListWTDocs.getElementAt(i);
                    Logging.infoMsg("Файл №"+ i + " ----- " + element);

                }

                for (i=0; i<modelListWTDocs.size(); i++) {
                        String element = (String) modelListWTDocs.getElementAt(i);
                        try {
                            ConnectionWind.getInstance().downloadWTDocument(element, element, konIzd);
                            Logging.infoMsg("Попытка скачать файл " + element);
//                            new InfoGUI("Сохранено в директории " + ConnectionWind.getInstance().getJARPath() + File.separator + "Firmwares" + File.separator + element); - just a notification
//                            System.out.println("SUCCESS");

                        } catch (Exception e1) {
                            new ErrorGUI(e1.getMessage());
                            System.out.println("ERROR: cannot form the link");
                           }
                }
                System.out.println("SUCCESS");
                System.exit(0);
                saveButton.setEnabled(false);
                modelListWTDocs.clear();
                modelListStates.clear();
            }
        });


        // Cancel button listener, should work
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ERROR: closed by a user");
                System.exit(1);
                saveButton.setEnabled(false);
                modelListWTDocs.clear();
                modelListStates.clear();
            }
        });


    }

}
