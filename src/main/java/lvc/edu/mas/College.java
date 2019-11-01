package lvc.edu.mas;

import java.io.Serializable;

public class College implements Serializable {
    private static final long serialVersionUID = -5046553748071676274L;
    private int id;
private String name;
private int numRecruitLimit;
private String location;
private String[] availableSports;

public College(int id, String name, int numRecruitLimit, String location, String[] availableSports){
    this.id = id;
    this.name = name;
    this.numRecruitLimit = numRecruitLimit;
    this.location = location;
    this.availableSports = availableSports;
}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAvailableSports(String[] availableSports) {
        this.availableSports = availableSports;
    }

    public String[] getAvailableSports() {
        return availableSports;
    }

    public void setNumRecruitLimit(int numRecruitLimit) {
        this.numRecruitLimit = numRecruitLimit;
    }

    public int getNumRecruitLimit() {
        return numRecruitLimit;
    }
    public int addRecruit(){
    int limit = getNumRecruitLimit();
        return limit-1;
    }
}
