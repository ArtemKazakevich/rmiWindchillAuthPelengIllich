
import wt.method.RemoteMethodServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;

public class Test {

    public static void main(String[] args) {
        String in = "7084.00.00.000-12_7084.15.00.400_DD3_V5.01.BIN";
        String searchName = in.substring(0, in.lastIndexOf(".")); // or is it WTDocName? OR EVEN NUMBER??????
        String fileExtension = in.substring(in.lastIndexOf("."), in.length());

        System.out.println(searchName);
    }
}
