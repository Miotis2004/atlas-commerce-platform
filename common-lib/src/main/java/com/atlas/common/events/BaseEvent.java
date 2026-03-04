package com.atlas.common.events;

import java.time.Instant;
import java.util.UUID;

public record BaseEvent(
        UUID eventId,
        String eventType,
        int eventVersion,
        Instant occurredAt,
        String correlationId
) {
}
