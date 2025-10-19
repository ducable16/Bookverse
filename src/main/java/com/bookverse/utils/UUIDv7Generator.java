package com.bookverse.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.UUID;

@Component
public class UUIDv7Generator implements BeforeExecutionGenerator {

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object entity,
            Object currentValue,
            EventType eventType
    ) {
        if (currentValue != null) {
            return currentValue;
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
