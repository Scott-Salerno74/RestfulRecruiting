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
    public static ConcurrentHashMap<Integer,Recruit> getRecruitData(){
        return recruitConcurrentHashMap;
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
    //Methods for specific Id numbers
    @Path("/{id}")
    @PUT
    @Produces("application/json")
    public void changeRecruitInfo( @PathParam("id") int id,  String update) throws ParseException, IOException {
        JSONParser parse = new JSONParser();
        JSONObject obj = new JSONObject();
        obj = (JSONObject) parse.parse(update);
        if (obj.containsKey("lName")){
            recruitConcurrentHashMap.get(id).setlName( obj.get("lName").toString());
        }
        if (obj.containsKey("fName")){
            recruitConcurrentHashMap.get(id).setfName( obj.get("fName").toString());
        }
        if (obj.containsKey("location")){
            recruitConcurrentHashMap.get(id).setLocation( obj.get("location").toString());
        }
        if (obj.containsKey("recruitmentScore")){
            recruitConcurrentHashMap.get(id).setRecruitmentScore( Integer.parseInt(obj.get("recruitmentScore").toString()));
        }
        if (obj.containsKey("schoolYear")){
            recruitConcurrentHashMap.get(id).setSchoolYear( obj.get("schoolYear").toString());
        }
        if (obj.containsKey("sport")){
            recruitConcurrentHashMap.get(id).setSport( obj.get("sport").toString());
        }
        storeRecruits(recruitConcurrentHashMap);
    }
    @Path("{id}")
    @GET
    @Produces("application/json")
    public String getRecruitById(@PathParam("id") int id) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONObject jsonResponse = new JSONObject();
        response.append(mapper.writeValueAsString(recruitConcurrentHashMap.get(id)));
        jsonResponse.put("recruit", response);
        return jsonResponse.toJSONString();
    }

    //Get Recruited By
    @Path("{id}/recruitedBy")
    @GET
    public String getRecruitedBy(@PathParam("id") int id ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONObject obj = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        response.append(mapper.writeValueAsString(recruitConcurrentHashMap.get(id)));
        obj.put("College", response);
        jsonResponse.put("rel", "next");
        jsonResponse.put("href", Main.BASE_URI +"/colleges/" + id );
        jsonResponse.put("type","application/json");
        return jsonResponse.toJSONString();


    }

    //Get Query Params
    @Path("/query")
    @GET
    public String searchRecruitBySport(@QueryParam("sport") String sportQuery) throws ParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        //Loop through concurrent hash-map and add to string response
        for(int i = 1; i <= recruitConcurrentHashMap.size(); i++){
            if (recruitConcurrentHashMap.get(i).getSport().equalsIgnoreCase(sportQuery)){
                String json = mapper.writeValueAsString(recruitConcurrentHashMap.get(i));
                jsonArray.add(json);
            }
        }
        jsonResponse.put("recruits",jsonArray);
        return jsonResponse.toJSONString();


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
//    public static void main(String[] args) throws IOException {
//        ConcurrentHashMap<Integer,Recruit> update = storeRecruits(recruitConcurrentHashMap);
//    }
//    //hello Commit
}
