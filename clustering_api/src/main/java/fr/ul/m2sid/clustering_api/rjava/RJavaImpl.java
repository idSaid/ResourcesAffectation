package fr.ul.m2sid.clustering_api.rjava;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class RJavaImpl {

    private RConnection connection;
    private static String OS = System.getProperty("os.name").toLowerCase();

    public void connect() throws RserveException {
        connection = new RConnection();
    }

    public void disconnect(){
        connection.close();
    }

    public String eval(String command) throws RserveException, REXPMismatchException {
        return connection.eval(command).asString();
    }

    public static void lancerKmeans() throws REXPMismatchException, RserveException, IOException {

        if (OS.contains("windows")) {
            URL cheminCompletKmeans = new File(new File("").getCanonicalPath() + "/rscripts/RKmeansScript.R").toURL();
            String path = cheminCompletKmeans.toString().substring(6);
            ProcessBuilder kmeans = new ProcessBuilder(".../Rscript.exe", path);
        }

        if (OS.contains("mac")){
            Process kmeans = Runtime.getRuntime().exec("Rscript rscripts/RKmeansScript.R");
            System.out.println(kmeans.getErrorStream().read());
            System.out.println(kmeans.getOutputStream().toString());
        }

    }
}
