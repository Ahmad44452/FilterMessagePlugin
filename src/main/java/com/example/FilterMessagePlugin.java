package com.example;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

import java.util.List;

public class FilterMessagePlugin implements BrokerPlugin{
    List<String> forbiddenWords;
    String errorMessageToReplace;
    public Broker installPlugin(Broker broker) throws Exception {
        return new FilterMessageBroker(broker,forbiddenWords, errorMessageToReplace);
    }

    public List<String> getForbiddenWords() {
        return forbiddenWords;
    }
    public void setForbiddenWords(List<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    public String getErrorMessageToReplace() {
        return errorMessageToReplace;
    }
    public void setErrorMessageToReplace(String errorMessageToReplace) {
        this.errorMessageToReplace = errorMessageToReplace;
    }
}
