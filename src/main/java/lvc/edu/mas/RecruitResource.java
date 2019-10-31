package lvc.edu.mas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Root resource (exposed at "recruits" path)
 */
@Path("recruits")
public class RecruitResource {
    private static ConcurrentHashMap<Integer,Recruit> recruitConcurrentHashMap = new ConcurrentHashMap<>();

    public static void storeRecruits(ConcurrentHashMap<Integer,Recruit> database) throws IOException {
        File file = new File("recruits.txt");
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
        File file = new File("recruits.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,Recruit> database = (ConcurrentHashMap<Integer, Recruit>) inS.readObject();
        inS.close();
        return database;
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces("application/json")
    public String getRecruit() {
        return "Got it!";
    }

    @Path("/{id}")
    @POST
    @Produces("application/json")
    public void addCollege(String recruitJson) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject recruit = (JSONObject) parser.parse(recruitJson);
        Recruit r = new Recruit(Integer.parseInt(recruit.get("id").toString()),recruit.get("fName").toString(),recruit.get("lName").toString(),recruit.get("schoolYear").toString(), recruit.get("sport").toString(), recruit.get("location").toString(), Integer.parseInt(recruit.get("recruitmentScore").toString()));
        System.out.println(r.getlName() + " " + r.getLocation());
    }
    @Path("/{id}")
    @PUT
    @Produces("application/json")
    public void changeCollegeInfo(String update) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject college = (JSONObject) parser.parse(update);
        System.out.println(college.toJSONString());

    }
    //hello Commit
}
