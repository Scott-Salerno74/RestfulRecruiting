package lvc.edu.mas;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/restful_recruitment/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() throws IOException, ClassNotFoundException {
        // create a resource config that scans for JAX-RS resources and providers
        // in lvc.edu.mas package
        final ResourceConfig rc = new ResourceConfig().packages("lvc.edu.mas");
        RecruitResource.initRecruitData();
        RecruitmentDataResource.initRecruitedData();
        CollegeResource.initCollegeData();
        //RecruitmentDataResource.updateFromData();
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        RecruitmentDataResource.updateFromData();
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }

    /**
     * Store Recruit Database
     * @param database
     * @throws IOException
     */
    public static void storeRecruits(ConcurrentHashMap<Integer,Recruit> database) throws IOException {
        File file = new File("recruits.txt");
        FileOutputStream out = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(database);
        os.close();

    }
    /**
     * Load in Recruit database
     * @throws IOException
     */

    public static ConcurrentHashMap loadRecruits() throws IOException, ClassNotFoundException {
        File file = new File("recruits.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,Recruit> database = (ConcurrentHashMap<Integer, Recruit>) inS.readObject();
        inS.close();
        return database;
    }

    /**
     * Store College Database
     * @param database
     * @throws IOException
     */
    public static void storeColleges(ConcurrentHashMap<Integer,College> database) throws IOException {
        File file = new File("colleges.txt");
        FileOutputStream out = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(database);
        os.close();

    }
    /**
     * Load in database
     * @throws IOException
     */

    public static ConcurrentHashMap loadColleges() throws IOException, ClassNotFoundException {
        File file = new File("colleges.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,College> database = (ConcurrentHashMap<Integer, College>) inS.readObject();
        inS.close();
        return database;
    }
}

