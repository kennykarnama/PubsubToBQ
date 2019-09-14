/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
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
                PubsubMessage message = c.element();
                
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                
                Gson gson = new Gson();
                
                UserScore us = gson.fromJson(payload, UserScore.class);
//                String eventSerial = message.getAttribute("eventSerial");
//                String segmentSerial = message.getAttribute("segmentSerial");
//                String userSerial = message.getAttribute("userSerial");
//                String duration = message.getAttribute("duration");
//                Float score = Float.parseFloat(message.getAttribute("score"));
//                Float scoreMultiplier = Float.parseFloat(message.getAttribute("scoreMultiplier"));
//                String createdAt = message.getAttribute("createdAt");
//                String updatedAt = message.getAttribute("updatedAt");
//                String schoolID = message.getAttribute("schoolID");
//                UserScore us = new UserScore(eventSerial,segmentSerial,userSerial,duration,
//                score,scoreMultiplier,createdAt,updatedAt,schoolID);
                c.output(us);
            }catch(NumberFormatException|JsonSyntaxException e) {
                LOG.error("Error ProcessElement ", e.getMessage());
                numParseErrors.inc();
            }
            
        }
        
        private  Object getObject(byte[] byteArr) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();
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
            row.set("score_multiplier", us.getScoreMultipler());
            row.set("created_at", us.getCreatedAt());
            row.set("updated_at", us.getUpdatedAt());
            row.set("school_id", us.getSchoolID());
            c.output(row);
        }
    }
}
