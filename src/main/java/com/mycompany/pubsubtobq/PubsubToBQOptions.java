/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

/**
 *
 * @author kenny
 */
public interface PubsubToBQOptions extends PipelineOptions{
    
    
    
    @Description("Input topic pubsub for the pipeline")
    String getPubsubTopic();
    void setPubsubTopic(String pubsubTopic);
    
    @Description("Output table for successful written records in BQ")
    String getBQTable();
    void setBQTable(String bqTable);
    
    @Description("Output deadletter table for failed written records in BQ")
    String getBQDeadLetterTable();
    void setBQDeadLetterTable(String bqDeadLetterTable);
}
