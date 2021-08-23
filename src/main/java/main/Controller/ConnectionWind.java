package main.Controller;


import main.Start;
import main.View.ErrorGUI;
import main.View.InfoGUI;
import main.View.MainForm;
import sun.misc.BASE64Encoder;
import wt.doc.WTDocument;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.List;

//Singletone
public class ConnectionWind implements RemoteAccess {
     static {
     
     }
     
     private RemoteMethodServer remotemethodserver;
     private static ConnectionWind connection;
     private boolean isFindWTDocument;
     
     private ConnectionWind() {
          configSocket();
     }
     
     //Singletone realisation
     public static ConnectionWind getInstance() {
          if (connection == null) {
               try {
                    connection = new ConnectionWind();
               } catch (Exception ex) {
                    ex.printStackTrace();
               }
          }
          return connection;
     }
     
     //login and password unactive
     //TODO: login and password pass automaticly
     private void configSocket() {
          String serverUrl = "https://wch.peleng.jsc.local/Windchill/"; //windchill
          URL url = null;
          
          try {
               url = new URL(serverUrl);
          } catch (MalformedURLException e) {
               new ErrorGUI(e.getMessage()); //вызываем GUI при ошибке
               e.printStackTrace();
          }
          
          String serviceName = "MethodServer";
          remotemethodserver = RemoteMethodServer.getInstance(url, serviceName);
          //remotemethodserver.setUserName("login");
          //remotemethodserver.setPassword("password");
          
     }
     
     private URL formDownloadebleURL(String WTDocName) {
          Class[] rmiArgTypes = new Class[1];
          rmiArgTypes[0] = java.lang.String.class;
          Object[] rmiArgs = new Object[1];
          rmiArgs[0] = WTDocName;
          Object urlDownload = null;
          
          try {
               urlDownload = remotemethodserver.invoke("getDownloadableURL",
                       "ext.by.peleng.serverside.ServerSide",
                       null, rmiArgTypes, rmiArgs);
               isFindWTDocument = true;
          } catch (Exception e) {
               new ErrorGUI(e.getMessage() + " this is bad");
               e.printStackTrace();
          }
          
          return (URL) urlDownload;
     }
     
     //Using formDownloadebleURL method
     public void downloadWTDocument(String WTDocNumber, String fileName, String izd) throws Exception {
          //String searchName = WTDocNumber.substring(0, WTDocNumber.lastIndexOf("."));
          String fileExtension = WTDocNumber.substring(WTDocNumber.lastIndexOf("."), WTDocNumber.length());
          
          URL urlDownload = formDownloadebleURL(WTDocNumber);
          
          File folder1 = new File(getJARPath() + File.separator + izd);
          if (!folder1.exists()) {
               folder1.mkdir();
          }
          File folder = new File(folder1.getPath() + "/Firmwares");
          if (!folder.exists()) {
               folder.mkdir();
          }
          
          if (isFindWTDocument) {
               
               HttpURLConnection url = (HttpURLConnection) urlDownload.openConnection();
               BASE64Encoder enc = new BASE64Encoder();
               String encodedAuthorization = enc.encode("user:pass".getBytes());
               url.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
               url.setRequestProperty("Content-Type", "application/octet-stream");
               url.setDoOutput(true);
               url.setDoInput(true);
               url.setRequestMethod("POST");
               InputStream in = url.getInputStream();
               //MtlLinkCreo.displayMessage("ModelItem value", "in:" + in.available() + " byte");
               
               //Create a folder
               
               //Docs will be stored there

// todo izd here
               String outputDownloadedFilesLocation = folder.getPath() + File.separator + fileName;
               FileOutputStream fout = new FileOutputStream(outputDownloadedFilesLocation);
               byte[] buff = new byte[1024];
               int len = in.read(buff);
               while (len != -1) {
                    fout.write(buff, 0, len);
                    len = in.read(buff);
               }
               //System.out.println(in.available()); - was uncommented
               in.close();
               fout.close();
               url.disconnect();
               
          }
          
     }
     
     //Gets strings of all WTDocuments that is exists in folder
     //container (Folder)
     public List<String> getStringListWTDocs(String folder) {
          if (folder.length() > 0) {
               Class[] rmiArgTypes = new Class[1];
               rmiArgTypes[0] = java.lang.String.class;
               Object[] rmiArgs = new Object[1];
               rmiArgs[0] = folder;
               List<String> obj = null;
               try {
                    obj = (List<String>) remotemethodserver.invoke(
                            "getSubFolderListName",
                            "ext.by.peleng.serverside.ServerSide",
                            null, rmiArgTypes, rmiArgs);
               } catch (Exception e) {
                    new ErrorGUI(e.getMessage());
                    e.printStackTrace();
               }
               return obj;
          } else return null;
     }
     
     
     // todo: change IF for WHILE HASNEXT
     public File getFileWTDoc(String WTDocName) throws Exception {
          if (WTDocName.length() > 0) {
               Class[] rmiArgTypes = new Class[1];
               rmiArgTypes[0] = java.lang.String.class;
               Object[] rmiArgs = new Object[1];
               rmiArgs[0] = WTDocName;
               Object obj = null;
               obj = remotemethodserver.invoke("getWTDocFile", "ext.by.peleng.serverside.ServerSide", null, rmiArgTypes, rmiArgs);
               return (File) obj;
          } else return null;
     }
     
     public String getWTDocFileName(String WTDocName) throws Exception {
          if (WTDocName.length() > 0) {
               Class[] rmiArgTypes = new Class[1];
               rmiArgTypes[0] = java.lang.String.class;
               Object[] rmiArgs = new Object[1];
               rmiArgs[0] = WTDocName;
               Object obj = null;
               obj = remotemethodserver.invoke("getWTDocFileNameAndState", "ext.by.peleng.serverside.ServerSide", null, rmiArgTypes, rmiArgs);
               return (String) obj;
          } else return null;
     }
     
     public List<String> getWTDocFileNameListWhichStartsLike(String WTDocName) throws Exception {
          if (WTDocName.length() > 0) {
               Class[] rmiArgTypes = new Class[1];
               rmiArgTypes[0] = java.lang.String.class;
               Object[] rmiArgs = new Object[1];
               rmiArgs[0] = WTDocName;
               Object obj = null;
               obj = remotemethodserver.invoke("getWTDocFileNameListWhichStartsLike", "ext.by.peleng.serverside.ServerSide", null, rmiArgTypes, rmiArgs);
               return (List<String>) obj;
          } else return null;
     }
     
     public String getJARPath() throws UnsupportedEncodingException {
          URL url = Start.class.getProtectionDomain().getCodeSource().getLocation();
          String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
          String parentPath = new File(jarPath).getParentFile().getPath();
          return parentPath;
     }
     
     
     //todo NEW CLASS
     
     public List<String> getWTDocsListUsingAttrs(String ispoln, String izd) throws Exception {
          if (!ispoln.isEmpty() && !izd.isEmpty()) {
               Class[] rmiArgTypes = new Class[2];
               rmiArgTypes[0] = java.lang.String.class;
               rmiArgTypes[1] = java.lang.String.class;
               
               
               Object[] rmiArgs = new Object[2];
               rmiArgs[0] = ispoln;
               rmiArgs[1] = izd;
               
               Object obj = null;
               obj = remotemethodserver.invoke("getWTDocFileNameListWhichStartsLike", "ext.by.peleng.serverside.ServerSide", null, rmiArgTypes, rmiArgs);
               return (List<String>) obj;
          } else return null;
     }
     
     
}