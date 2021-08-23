package main.Controller;
import main.View.ErrorGUI;
import main.View.MainForm;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.io.File;

public class Logging {

    public static String OUTPUT_PATH = System.getProperty("user.home") + File.separator +"Desktop" +File.separator +"log.log";

    //TODO
    private static final Logger log = Logger.getLogger(Logging.class);

    public static void infoMsg(String msg)

    {
        log.info(msg);

    }

    public static void startMsg(String msg)

    {
        log.info(msg);

    }


    public static void endMsg(String msg)

    {
        log.info(msg);

    }


    // Ошибка
    public static void errorMsg(String msg)

    {
        //error message
    //    System.out.println("Ошибка");
        //logging below
        log.error(msg);
    }

}
