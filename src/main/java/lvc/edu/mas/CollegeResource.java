package lvc.edu.mas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.*;
import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("colleges")
public class CollegeResource implements Serializable{
    private static ConcurrentHashMap<Integer,College> collegeConcurrentHashMap = new ConcurrentHashMap<>();
    public static AtomicInteger id = new AtomicInteger(0);
    public static ConcurrentHashMap<Integer, College> storeColleges(ConcurrentHashMap<Integer,College> database) throws IOException {
        File file = new File("colleges.txt");
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
    @GET
    @Produces("application/json")
    public String getCollege() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        //Loop through concurrent hashmap and add to string response
        for(int i = 1; i <= collegeConcurrentHashMap.size(); i++){
            College c = (College) collegeConcurrentHashMap.get(i);
            String json = mapper.writeValueAsString(c);
            jsonArray.add(json);

        }
        jsonResponse.put("colleges",jsonArray);
        return jsonResponse.toJSONString();
    }

    @POST
    @Produces("application/json")
    public void addCollege(String collegeJson) throws ParseException, IOException {
        int newId = id.incrementAndGet();
        JSONParser parser = new JSONParser();
        JSONObject college = (JSONObject) parser.parse(collegeJson);
        String collegeString= college.get("sports").toString();
        String[] sportsArr = collegeString.split(",");
        //System.out.println(co[0] + co[1]);
        College c = new College(newId,college.get("name").toString(),Integer.parseInt(college.get("numRecruitLimit").toString()),college.get("location").toString(),sportsArr);
        collegeConcurrentHashMap.put(newId,c);
        storeColleges(collegeConcurrentHashMap);
        System.out.println(c.getName() + " " + c.getLocation());
        System.out.println(collegeConcurrentHashMap.get(2).getName() + " " + collegeConcurrentHashMap.get(2).getId() );
    }
    // For Specific Player Ids

    @Path("/{id}")
    @PUT
    @Produces("application/json")
    public void changeCollegeInfo(@PathParam("id") int id, String update) throws ParseException, IOException {
        JSONParser parse = new JSONParser();
        JSONObject obj = new JSONObject();
        obj = (JSONObject) parse.parse(update);
        if (obj.containsKey("name")){
            collegeConcurrentHashMap.get(id).setName( obj.get("name").toString());
        }
        if (obj.containsKey("location")){
            collegeConcurrentHashMap.get(id).setLocation( obj.get("location").toString());
        }
        if (obj.containsKey("sports")){
            String collegeString= obj.get("sports").toString();
            String[] sportsArr = collegeString.split(",");
            collegeConcurrentHashMap.get(id).setAvailableSports(sportsArr);
        }
        if (obj.containsKey("numRecruitLimit")){
            collegeConcurrentHashMap.get(id).setNumRecruitLimit( Integer.parseInt(obj.get("numRecruitLimit").toString()));
        }

        storeColleges(collegeConcurrentHashMap);

    }
    @Path("{id}")
    @GET
    @Produces("application/json")
    public String getCollegeById(@PathParam("id") int id) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder response = new StringBuilder();
        JSONObject jsonResponse = new JSONObject();
        response.append(mapper.writeValueAsString(collegeConcurrentHashMap.get(id)));
        jsonResponse.put("colleges", response);
        return jsonResponse.toJSONString();
    }

    public static void initCollegeData() throws IOException, ClassNotFoundException {
        File file = new File("colleges.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,College> database = (ConcurrentHashMap<Integer, College>) inS.readObject();
        inS.close();

        collegeConcurrentHashMap = database;

        int initialId = 0;
        Enumeration<Integer> keys = collegeConcurrentHashMap.keys();
        while(keys.hasMoreElements()){
            int newID = keys.nextElement();
            if(newID > initialId) {
                initialId = newID;
            }
            id = new AtomicInteger(newID);
        }
    }
    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<Integer,College> update = storeColleges(collegeConcurrentHashMap);
    }


}