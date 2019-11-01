package lvc.edu.mas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.*;
import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Root resource (exposed at "recruits" path)
 */
@Path("recruits")
public class RecruitResource implements Serializable{
    private static ConcurrentHashMap<Integer,Recruit> recruitConcurrentHashMap = new ConcurrentHashMap<>();
    public static  AtomicInteger id = new AtomicInteger(0);
    public static ConcurrentHashMap<Integer, Recruit> storeRecruits(ConcurrentHashMap<Integer,Recruit> database) throws IOException {
        File file = new File("recruits.txt");
        FileOutputStream out = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(database);
        os.close();

        return database;
    }
    /**
     * Load in database
     * @throws IOException
     */

    public static ConcurrentHashMap loadRecruit() throws IOException, ClassNotFoundException {
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
    public String getRecruit() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        //Loop through concurrent hash-map and add to string response
        for(int i = 1; i <= recruitConcurrentHashMap.size(); i++){
            Recruit r = recruitConcurrentHashMap.get(i);
            String json = mapper.writeValueAsString(r);
            jsonArray.add(json);

        }
        jsonResponse.put("recruits",jsonArray);
        return jsonResponse.toJSONString();
    }

    @POST
    @Produces("application/json")
    public void addRecruit(String recruitJson) throws ParseException, IOException {
        int newID = id.incrementAndGet();
        JSONParser parser = new JSONParser();
        JSONObject recruit = (JSONObject) parser.parse(recruitJson);
        Recruit r = new Recruit(newID,recruit.get("fName").toString(),recruit.get("lName").toString(),recruit.get("schoolYear").toString(), recruit.get("sport").toString(), recruit.get("location").toString(), Integer.parseInt(recruit.get("recruitmentScore").toString()));
        recruitConcurrentHashMap.put(newID,r);
        storeRecruits(recruitConcurrentHashMap);
        System.out.println(r.getfName() + " " +r.getlName() + " " + r.getLocation() + " " + newID);
        System.out.println(recruitConcurrentHashMap.get(1).getfName());
    }
    @Path("/{id}")
    @PUT
    @Produces("application/json")
    public void changeRecruitInfo(String update) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject college = (JSONObject) parser.parse(update);
        System.out.println(college.toJSONString());

    }

    public static void initRecruitData() throws IOException, ClassNotFoundException {
        File file = new File("recruits.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,Recruit> database = (ConcurrentHashMap<Integer, Recruit>) inS.readObject();
        inS.close();

        recruitConcurrentHashMap = database;

        int initialId = 0;
        Enumeration<Integer> keys = recruitConcurrentHashMap.keys();
        while(keys.hasMoreElements()){
            int newID = keys.nextElement();
            if(newID > initialId) {
                initialId = newID;
            }
            id = new AtomicInteger(newID);
        }
    }
    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<Integer,Recruit> update = storeRecruits(recruitConcurrentHashMap);
    }
    //hello Commit
}
