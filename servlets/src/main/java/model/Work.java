package model;


import java.util.HashSet;
import java.util.Set;

public class Work {

    private int id;
    private int creatorId;
    private String description;
    private int status;
    private String shortDescription;
    private int reward=0;

    private Set<Integer> workSpecialisations=new HashSet<>();


    public Work(int id, int creatorId, String description, int status, String shortDescription, Set<Integer> workSpecialisations) {
        this.id = id;
        this.creatorId = creatorId;
        this.description = description;
        this.status = status;
        this.shortDescription = shortDescription;
        this.workSpecialisations=workSpecialisations;
    }




    public Work(int creatorId, String description, int status, String shortDescription,Set<Integer> workSpecialisations) {
       this(0,creatorId,description,status,shortDescription,workSpecialisations);
    }

    public Work(int anInt, int anInt1, String string, int anInt2, String string1) {
        //TODO:constructor!!!!
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public int getReward() {
        return reward;
    }


    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
