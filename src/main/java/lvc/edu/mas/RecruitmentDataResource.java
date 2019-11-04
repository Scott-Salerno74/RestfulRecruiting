package lvc.edu.mas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Root resource (exposed at "recruitedPage" path)
 */
@Path("recruitedPage")
public class RecruitmentDataResource implements Serializable {
    private static final long serialVersionUID = 2123723517170647427L;
    private static ConcurrentHashMap<Integer,RecruitedClass> dataConcurrentHashMap = new ConcurrentHashMap<>();
    private static  AtomicInteger id = new AtomicInteger(0);
    private static ConcurrentHashMap<Integer, RecruitedClass> storeRecruited(ConcurrentHashMap<Integer,RecruitedClass> database) throws IOException {
        File file = new File("recruitedData.txt");
        FileOutputStream out = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(database);
        os.close();

        return database;
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     */
    @POST
    @Produces("application/json")
    public void recruited(String recruitment) throws ParseException, IOException {
        int newID = id.incrementAndGet();
        JSONParser parse = new JSONParser();
        JSONObject obj = new JSONObject();
        obj = (JSONObject) parse.parse(recruitment);
        College c = CollegeResource.getCollegeData().get(Integer.parseInt(obj.get("collegeID").toString()));
        c.setNumRecruitLimit( c.addRecruit());
        System.out.println(c.getName() + "recruitment Complete " + c.getNumRecruitLimit());
        Recruit r = RecruitResource.getRecruitData().get(Integer.parseInt(obj.get("recruitID").toString()));
        r.setRecruited(true);
        r.setRecruitedBy(c);
        System.out.println(r.getRecruitedBy().getName());
        RecruitedClass rC = new RecruitedClass(c,r);
        dataConcurrentHashMap.put(newID,rC);
        storeRecruited(dataConcurrentHashMap);
    }

    public static void updateFromData() throws IOException {
        ConcurrentHashMap<Integer, Recruit> updateR = RecruitResource.getRecruitData();
        ConcurrentHashMap<Integer, College> updateC = CollegeResource.getCollegeData();
        Enumeration<Integer> keys = dataConcurrentHashMap.keys();
        while (keys.hasMoreElements()) {
            for (int i = 0; i < updateC.size(); i++) {
                if (dataConcurrentHashMap.get(i).getCollege().getId() == updateC.get(i).getId()) {
                    updateC.get(i).setNumRecruitLimit(dataConcurrentHashMap.get(i).getCollege().getNumRecruitLimit());
                    CollegeResource.storeColleges(updateC);
                }
                if (dataConcurrentHashMap.get(i).getRecruit().getId() == updateR.get(i).getId()) {
                    updateR.get(i).setRecruited(dataConcurrentHashMap.get(i).getRecruit().isRecruited());
                    updateR.get(i).setRecruitedBy(dataConcurrentHashMap.get(i).getRecruit().getRecruitedBy());
                    RecruitResource.storeRecruits(updateR);
                }
            }
        }
    }

    public static void initRecruitedData() throws IOException, ClassNotFoundException {
        File file = new File("recruitedData.txt");
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream inS = new ObjectInputStream(in);
        ConcurrentHashMap<Integer,RecruitedClass> database = (ConcurrentHashMap<Integer, RecruitedClass>) inS.readObject();
        inS.close();

        dataConcurrentHashMap = database;

        int initialId = 0;
        Enumeration<Integer> keys = dataConcurrentHashMap.keys();
        while(keys.hasMoreElements()){
            int newID = keys.nextElement();
            if(newID > initialId) {
                initialId = newID;
            }
            id = new AtomicInteger(newID);
        }
    }
//    public static void main(String[] args) throws IOException {
//        ConcurrentHashMap<Integer, RecruitedClass> update = storeRecruited(dataConcurrentHashMap);
//    }
}