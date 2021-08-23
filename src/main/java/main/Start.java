package main;

import main.Controller.Logging;
import main.View.MainForm;
import java.io.File;
import java.io.IOException;

public class Start {

    private static void startEndThread() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Info application is finished!");
                Logging.endMsg("Выполнение приложения завершено.");
            }
        }, "Shutdown-thread"));
    }

    public static void main(String[] args) {

        String ispoln = args[0];
        String izd = args[1];

//           String ispoln = "7084.00.00.000-12";
//           String izd = "7084.15.00.400";


        Logging.startMsg("|||||||||||||||||||||||||   Приложение запущено   |||||||||||||||||||||||||");


        startEndThread();

        System.out.println("Info application is started!");

        try {
            new MainForm(ispoln, izd); // nothing was here
        } catch (NullPointerException e2)
        {
            System.out.println("ERROR");
        }


    }



}
