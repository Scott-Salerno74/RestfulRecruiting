package lvc.edu.mas;

import java.io.Serializable;

public class Recruit implements Serializable {
    private static final long serialVersionUID = -6261569056452316656L;
    private int id;
    private String fName;
    private String lName;
    private String schoolYear;
    private  String sport;
    private String location;
    private int recruitmentScore;
    private boolean recruited;
    private College recruitedBy;

    public Recruit(int id, String fName,String lName, String schoolYear, String sport, String location, int recruitmentScore){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.schoolYear = schoolYear;
        this.sport = sport;
        this.location = location;
        this.recruitmentScore = recruitmentScore;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRecruited() {
        return recruited;
    }

    public void setRecruited(boolean recruited) {
        this.recruited = recruited;
    }

    public College getRecruitedBy() {
        return recruitedBy;
    }

    public void setRecruitedBy(College recruitedBy) {
        this.recruitedBy = recruitedBy;
    }

    public int getRecruitmentScore() {
        return recruitmentScore;
    }

    public void setRecruitmentScore(int recruitmentScore) {
        this.recruitmentScore = recruitmentScore;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public String getSport() {
        return sport;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
