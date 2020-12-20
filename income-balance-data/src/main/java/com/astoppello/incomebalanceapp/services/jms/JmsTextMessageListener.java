package com.astoppello.incomebalanceapp.services.jms;

import com.astoppello.incomebalanceapp.config.JMSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JmsTextMessageListener {

    @JmsListener(destination = JMSConfig.textMsgQueue)
    public void onMessage(String msg) {
        System.out.println("###" + msg + "###");
    }
}
