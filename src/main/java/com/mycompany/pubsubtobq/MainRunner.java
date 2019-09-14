/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pubsubtobq;

import com.google.api.services.bigquery.model.TableRow;
import com.mycompany.pubsubtobq.Leaderboard.ParseUserScoreMessageFn;
import com.mycompany.pubsubtobq.Leaderboard.ParseUserScoreToTableRow;
import com.mycompany.pubsubtobq.Leaderboard.UserScore;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.InsertRetryPolicy;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.ParDo;


/**
 *
 * @author kenny
 */


public class MainRunner {
    
    public static void main(String[] args) {
        //Register our custom options class
        PipelineOptionsFactory.register(PubsubToBQOptions.class);
        //validate it
        PubsubToBQOptions options = PipelineOptionsFactory.fromArgs(args)
                                    .withValidation()
                                    .as(PubsubToBQOptions.class);
        //create pipeline
        Pipeline p = Pipeline.create(options);
        // begin read from pubsubio
        PCollection<PubsubMessage> messages = p.apply("ReadTopicsFromPubsub", PubsubIO.readMessagesWithAttributes().fromTopic(options.getPubsubTopic()));
        //Transform pubsub message to UserSCore model
        PCollection<UserScore> userScores = messages.apply("ParseToUserScoreModel", ParDo.of(new ParseUserScoreMessageFn()));
        //Transform userscore to bq tablerow userscore
        PCollection<TableRow> rows = userScores.apply("ParseToBQTableRow", ParDo.of(new ParseUserScoreToTableRow()));
        //Insert to bq
        WriteResult outputTableRow = rows.apply("InsertToUserScore", BigQueryIO.writeTableRows()
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withoutValidation()
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                .withExtendedErrorInfo()
                .withMethod(BigQueryIO.Write.Method.STREAMING_INSERTS)
                .withFailedInsertRetryPolicy(InsertRetryPolicy.retryTransientErrors())
                .to(options.getBQTable())
        );
    }
}
