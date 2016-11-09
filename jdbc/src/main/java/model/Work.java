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
    private float rate=0;




    public Work(int id, int creatorId, String description, int status, String shortDescription, int reward, float rate) {
        this.id = id;
        this.creatorId = creatorId;
        this.description = description;
        this.status = status;
        this.shortDescription = shortDescription;
        this.reward=reward;
        this.rate=rate;
    }

    public Work(int creatorId, String description, int status, String shortDescription, int reward,float rate) {
        this(0,creatorId,description,status,shortDescription,reward,rate);
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

    public float getRate() {
        return rate;
    }
}
