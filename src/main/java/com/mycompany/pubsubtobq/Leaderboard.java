/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kenny
 */
public class Leaderboard {
    @DefaultCoder(AvroCoder.class)
    static class UserScore {
        @Nullable String eventSerial;
        @Nullable String segmentSerial;
        @Nullable String userSerial;
        @Nullable String duration;
        @Nullable Float score;
        @Nullable Float scoreMultipler;
        @Nullable String createdAt;
        @Nullable String updatedAt;
        @Nullable String schoolID;
        
        public UserScore(){}
        
        public UserScore(String eventSerial, String segmentSerial, String userSerial, String duration,
                Float score, Float scoreMultiplier, String createdAt, String updatedAt, String schoolID) {
            this.eventSerial = eventSerial;
            this.segmentSerial = segmentSerial;
            this.userSerial = userSerial;
            this.duration = duration;
            this.score = score;
            this.scoreMultipler = scoreMultiplier;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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
            return scoreMultipler;
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
    /**
     * ParseUserScoreMessageFn is a static class that extends DoFn
     * this static class is used to parse pubsub message
     * to UserScore model
     */
    static class ParseUserScoreMessageFn extends DoFn<PubsubMessage, UserScore> {
        private static final Logger LOG = LoggerFactory.getLogger(ParseUserScoreMessageFn.class);
        private final Counter numParseErrors = Metrics.counter("main", "ParseErrors");
        
        @ProcessElement
        public void ProcessElement(ProcessContext c) {
            try {
                String eventSerial = c.element().getAttribute("eventSerial");
                String segmentSerial = c.element().getAttribute("segmentSerial");
                String userSerial = c.element().getAttribute("userSerial");
                String duration = c.element().getAttribute("duration");
                Float score = Float.parseFloat(c.element().getAttribute("score"));
                Float scoreMultiplier = Float.parseFloat(c.element().getAttribute("scoreMultiplier"));
                String createdAt = c.element().getAttribute("createdAt");
                String updatedAt = c.element().getAttribute("updatedAt");
                String schoolID = c.element().getAttribute("schoolID");
                UserScore us = new UserScore(eventSerial,segmentSerial,userSerial,duration,
                score,scoreMultiplier,createdAt,updatedAt,schoolID);
                c.output(us);
            }catch(NumberFormatException e) {
                LOG.info("Error ProcessElement ", c.element().toString()+" err -> "+e.getMessage());
                numParseErrors.inc();
            }
            
        }
    }
    
    static class ParseUserScoreToTableRow extends DoFn<UserScore,TableRow> {
        private static final Logger LOG = LoggerFactory.getLogger(ParseUserScoreMessageFn.class);
        private final Counter numParseErrors = Metrics.counter("main", "ParseErrors");
        
        @ProcessElement
        public void ProcessElement(ProcessContext c) {
            UserScore us = c.element();
            TableRow row = new TableRow();
            row.set("event_serial", us.getEventSerial());
            row.set("segment_serial", us.getSegmentSerial());
            row.set("user_serial",us.getUserSerial());
            row.set("duration", us.getDuration());
            row.set("score", us.getScore());
            row.set("scoreMultiplier", us.getScoreMultipler());
            row.set("created_at", us.getCreatedAt());
            row.set("updated_at", us.getUpdatedAt());
            row.set("school_id", us.getSchoolID());
            c.output(row);
        }
    }
}
