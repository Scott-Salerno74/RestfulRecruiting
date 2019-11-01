package lvc.edu.mas;

import java.io.Serializable;

public class RecruitedClass implements Serializable {
    private College college;
    private Recruit recruit;

    public RecruitedClass(College college, Recruit recruit){
        this.college = college;
        this.recruit = recruit;

    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Recruit getRecruit() {
        return recruit;
    }

    public void setRecruit(Recruit recruit) {
        this.recruit = recruit;
    }
}
