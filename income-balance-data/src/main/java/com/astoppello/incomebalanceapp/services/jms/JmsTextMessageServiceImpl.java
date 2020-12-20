package com.astoppello.incomebalanceapp.services.jms;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

@RequiredArgsConstructor
@Service
public class JmsTextMessageServiceImpl implements JmsTextMessageService {

    private Queue textMessageQueue;
    private JmsTemplate jmsTemplate;

    public JmsTextMessageServiceImpl(Queue textMessageQueue, JmsTemplate jmsTemplate) {
        this.textMessageQueue = textMessageQueue;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendTextMessage(String msg) {
        jmsTemplate.convertAndSend(textMessageQueue, msg);
    }
}
