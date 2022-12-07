/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.healthcare.chat.notify;

import com.healthcare.chat.model.entity.MessageBox;
import com.pusher.rest.Pusher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * @author johnny
 */
@Component
public class ChatNotifyService {
    // Pusher
    private Pusher pusher =null;
    
    private final String OPERR_CHAT_CHANNEL="oper-chat-channel";
    public final String EVENT_PEAR2PEAR="peer-to-peer";
    public final String EVENT_GROUP_CHAT="group-chat";
    public final String EVENT_BASE_CHAT="base-chat";
    
    @PostConstruct
    public void intitialize(){
        pusher = new Pusher(
                "397007", // App ID
                "b75425501ef9322d9cc2",// Client Id
                "4d19396829a56c5b1dfb" // Secret ID
        );
        pusher.setCluster("us2");
        pusher.setEncrypted(true);
    }
    
    /**
     * 
     * @param event
     * @param data 
     */
    private void sendNotify(String event, Object data) {
        pusher.trigger(OPERR_CHAT_CHANNEL, event, data);

    }
    
    /**
     * 
     * @param messageBox 
     */
    public void sendNotify(MessageBox messageBox) {
        if (messageBox != null) {
            if (messageBox.getGroup() != null) {
                // Notify Group chat
                sendNotify(this.EVENT_GROUP_CHAT, messageBox);
            } else if (messageBox.getToUser() == -1) {
                 // Notify public chat on Base
                sendNotify(this.EVENT_BASE_CHAT, messageBox);
            } else {
                // Notify pear-2-pear chat
                sendNotify(this.EVENT_PEAR2PEAR, messageBox);
            }

        }
    }

}
