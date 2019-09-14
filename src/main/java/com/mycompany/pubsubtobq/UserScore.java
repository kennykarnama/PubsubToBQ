/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

/**
 *
 * @author kenny
 */
@DefaultCoder(AvroCoder.class)
public class UserScore implements Serializable{

        @SerializedName("eventSerial")
        private String eventSerial;
        @SerializedName("segmentSerial")
        private String segmentSerial;
        @SerializedName("userSerial")
        private String userSerial;
        @SerializedName("duration")
        private String duration;
        @SerializedName("score")
        private Float score;
        @SerializedName("scoreMultiplier")
        private Float scoreMultiplier;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("schoolID")
        private String schoolID;
        
        public UserScore(){}
        
        public UserScore(String eventSerial, String segmentSerial, String userSerial, String duration,
                Float score, Float scoreMultiplier, String createdAt, String updatedAt, String schoolID) {
            this.eventSerial = eventSerial;
            this.segmentSerial = segmentSerial;
            this.userSerial = userSerial;
            this.duration = duration;
            this.score = score;
            this.scoreMultiplier = scoreMultiplier;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.schoolID = schoolID;
        }
        
    public void setEventSerial(String eventSerial) {
        this.eventSerial = eventSerial;
    }

    public void setSegmentSerial(String segmentSerial) {
        this.segmentSerial = segmentSerial;
    }

    public void setUserSerial(String userSerial) {
        this.userSerial = userSerial;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setScoreMultipler(Float scoreMultipler) {
        this.scoreMultiplier = scoreMultipler;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }
    
    
        
        public String getEventSerial() {
            return eventSerial;
        }

        public String getSegmentSerial() {
            return segmentSerial;
        }

        public String getUserSerial() {
            return userSerial;
        }

        public String getDuration() {
            return duration;
        }

        public Float getScore() {
            return score;
        }

        public Float getScoreMultipler() {
            return scoreMultiplier;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getSchoolID() {
            return schoolID;
        }
        
    }
    

