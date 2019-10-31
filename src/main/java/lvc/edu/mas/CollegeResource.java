package lvc.edu.mas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("colleges")
public class CollegeResource {
    private static ConcurrentHashMap<Integer,College> collegeConcurrentHashMap = new ConcurrentHashMap<>();
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


    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public String getCollege() {
        JSONObject response = null;
        return  response.toJSONString();
    }

    @Path("/{id}")
    @POST
    @Produces("application/json")
    public void addCollege(String collegeJson) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject college = (JSONObject) parser.parse(collegeJson);
        String collegeString= college.get("sports").toString();
        String[] sportsArr = collegeString.split(",");
        //System.out.println(co[0] + co[1]);
       College c = new College(Integer.parseInt( college.get("id").toString()),college.get("name").toString(),Integer.parseInt(college.get("numRecruitLimit").toString()),college.get("location").toString(),sportsArr);
        System.out.println(c.getName() + " " + c.getLocation());
    }
    @Path("/{id}")
    @PUT
    @Produces("application/json")
    public void changeCollegeInfo(String update) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject college = (JSONObject) parser.parse(update);
        System.out.println(college.toJSONString());

    }


}