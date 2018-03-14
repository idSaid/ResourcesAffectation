package fr.ul.m2sid.clustering_api.rjava;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public interface RJava {

        public void connect() throws RserveException;

        public void disconnect();

        public String eval(String command) throws RserveException, REXPMismatchException;

}
