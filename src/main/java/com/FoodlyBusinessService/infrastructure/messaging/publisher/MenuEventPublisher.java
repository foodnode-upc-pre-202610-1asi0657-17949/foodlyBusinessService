package com.FoodlyBusinessService.infrastructure.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.FoodlyBusinessService.application.dto.MenuUpdatedEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MenuEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MenuEventPublisher.class);

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public MenuEventPublisher(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishMenuUpdatedEvent(MenuUpdatedEventDto eventDto) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(eventDto);
            jmsTemplate.convertAndSend("BusinessMenuUpdatedQueue", jsonPayload);
            log.info("Evento publicado en ActiveMQ: {}", jsonPayload);
        } catch (Exception e) {
            log.error("Error publicando evento de menú actualizado", e);
        }
    }
}
