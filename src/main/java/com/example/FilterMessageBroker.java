package com.example;

import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.command.Message;
import org.apache.activemq.util.ByteSequence;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.List;

public class FilterMessageBroker extends BrokerFilter {
    List<String> forbiddenWords;
    String errorMessageToReplace;
    public FilterMessageBroker(Broker next,List<String> forbiddenWords,String errorMessageToReplace) {
        super(next);
        this.forbiddenWords = forbiddenWords;
        this.errorMessageToReplace = errorMessageToReplace;
    }

    public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
        // Initialize JSON Parser
        JSONParser parser = new JSONParser();

        // Initialize variable that will content of message
        String messageContent = "";

        // Temporary storage for message. Before converting it to string
        ByteSequence byteSequence = messageSend.getContent();

        // Convert byteSequence to String
        if (byteSequence != null) {
            messageContent = new String(byteSequence.toArray());
        }

        // Extract message content from the whole message JSON object
        JSONObject jsonObjReceived = (JSONObject) parser.parse(messageContent);
        String sendMessageData = (String) jsonObjReceived.get("message");
        sendMessageData = sendMessageData.toLowerCase();

        // Initialize isVulgar variable
        Boolean isVulgar = false;

        // If one of the forbidden word is being sent by a user
        // Then replace its content with error message
        for(String word:forbiddenWords){
            if(sendMessageData.contains(word)){
                jsonObjReceived.put("message",errorMessageToReplace);
                String newJsonObj = jsonObjReceived.toJSONString();
                ByteSequence newSendMessage = new ByteSequence(newJsonObj.getBytes());
                messageSend.setContent(newSendMessage);
                break;
            }
        }


        super.send(producerExchange, messageSend);
    }
}
