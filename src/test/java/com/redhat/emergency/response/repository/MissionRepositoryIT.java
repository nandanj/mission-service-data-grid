package com.redhat.emergency.response.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;

import com.redhat.emergency.response.model.Mission;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MissionRepositoryIT {

    @Inject
    MissionRepository missionRepository;

    @BeforeEach
    void clearCache() {
        missionRepository.clear();
    }

    @Test
    void testAddAndRetrieveMission() {

        JsonObject json = new JsonObject().put("id", UUID.randomUUID().toString()).put("incidentId", "incident123")
                .put("incidentLat", new BigDecimal("30.12345").doubleValue()).put("incidentLong", new BigDecimal("-70.98765").doubleValue())
                .put("responderId", "responder123")
                .put("responderStartLat", new BigDecimal("31.12345").doubleValue()).put("responderStartLong", new BigDecimal("-71.98765").doubleValue())
                .put("destinationLat", new BigDecimal("32.12345").doubleValue()).put("destinationLong", new BigDecimal("-72.98765").doubleValue())
                .put("status", "CREATED").put("responderLocationHistory", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.45678").doubleValue())
                .put("lon", new BigDecimal("-70.65432").doubleValue()).put("timestamp", Instant.now().toEpochMilli())))
                .put("steps", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.14785").doubleValue())
                .put("lon", new BigDecimal("-70.91546").doubleValue()).put("wayPoint", false).put("destination", false)));

        Mission mission = json.mapTo(Mission.class);

        missionRepository.add(mission);

        Optional<Mission> fromCache = missionRepository.get(mission.getKey());

        assertThat(fromCache.isPresent(), is(true));
        assertThat(fromCache.get().getId(), notNullValue());
        assertThat(fromCache.get().getIncidentId(), equalTo("incident123"));
        assertThat(fromCache.get().getIncidentLat(), equalTo(new BigDecimal("30.12345")));
        assertThat(fromCache.get().getIncidentLong(), equalTo(new BigDecimal("-70.98765")));
        assertThat(fromCache.get().getResponderId(), equalTo("responder123"));
        assertThat(fromCache.get().getResponderStartLat(), equalTo(new BigDecimal("31.12345")));
        assertThat(fromCache.get().getResponderStartLong(), equalTo(new BigDecimal("-71.98765")));
        assertThat(fromCache.get().getDestinationLat(), equalTo(new BigDecimal("32.12345")));
        assertThat(fromCache.get().getDestinationLong(), equalTo(new BigDecimal("-72.98765")));
        assertThat(fromCache.get().getStatus(), equalTo("CREATED"));
        assertThat(fromCache.get().getResponderLocationHistory().size(), equalTo(1));
        assertThat(fromCache.get().getSteps().size(), equalTo(1));

    }

    @Test
    void testGetAllMissions() {

        JsonObject json = new JsonObject().put("id", UUID.randomUUID().toString()).put("incidentId", "incident123")
                .put("incidentLat", new BigDecimal("30.12345").doubleValue()).put("incidentLong", new BigDecimal("-70.98765").doubleValue())
                .put("responderId", "responder123")
                .put("responderStartLat", new BigDecimal("31.12345").doubleValue()).put("responderStartLong", new BigDecimal("-71.98765").doubleValue())
                .put("destinationLat", new BigDecimal("32.12345").doubleValue()).put("destinationLong", new BigDecimal("-72.98765").doubleValue())
                .put("status", "CREATED").put("responderLocationHistory", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.45678").doubleValue())
                        .put("lon", new BigDecimal("-70.65432").doubleValue()).put("timestamp", Instant.now().toEpochMilli())))
                .put("steps", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.14785").doubleValue())
                        .put("lon", new BigDecimal("-70.91546").doubleValue()).put("wayPoint", false).put("destination", false)));

        Mission mission = json.mapTo(Mission.class);

        JsonObject json2 = new JsonObject().put("id", UUID.randomUUID().toString()).put("incidentId", "incident456")
                .put("incidentLat", new BigDecimal("30.12345").doubleValue()).put("incidentLong", new BigDecimal("-70.98765").doubleValue())
                .put("responderId", "responder456")
                .put("responderStartLat", new BigDecimal("31.12345").doubleValue()).put("responderStartLong", new BigDecimal("-71.98765").doubleValue())
                .put("destinationLat", new BigDecimal("32.12345").doubleValue()).put("destinationLong", new BigDecimal("-72.98765").doubleValue())
                .put("status", "CREATED").put("responderLocationHistory", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.45678").doubleValue())
                        .put("lon", new BigDecimal("-70.65432").doubleValue()).put("timestamp", Instant.now().toEpochMilli())))
                .put("steps", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.14785").doubleValue())
                        .put("lon", new BigDecimal("-70.91546").doubleValue()).put("wayPoint", false).put("destination", false)));

        Mission mission2 = json2.mapTo(Mission.class);

        missionRepository.add(mission);
        missionRepository.add(mission2);

        List<Mission> fromCache = missionRepository.getAll();

        assertThat(fromCache.size(), equalTo(2));
    }

    @Test
    void testClear() {

        missionRepository.clear();

        List<Mission> fromCache = missionRepository.getAll();

        assertThat(fromCache.size(), equalTo(0));
    }

    @Test
    void testGetByResponderId() {

        JsonObject json = new JsonObject().put("id", UUID.randomUUID().toString()).put("incidentId", "incident123")
                .put("incidentLat", new BigDecimal("30.12345").doubleValue()).put("incidentLong", new BigDecimal("-70.98765").doubleValue())
                .put("responderId", "responder123")
                .put("responderStartLat", new BigDecimal("31.12345").doubleValue()).put("responderStartLong", new BigDecimal("-71.98765").doubleValue())
                .put("destinationLat", new BigDecimal("32.12345").doubleValue()).put("destinationLong", new BigDecimal("-72.98765").doubleValue())
                .put("status", "CREATED").put("responderLocationHistory", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.45678").doubleValue())
                        .put("lon", new BigDecimal("-70.65432").doubleValue()).put("timestamp", Instant.now().toEpochMilli())))
                .put("steps", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.14785").doubleValue())
                        .put("lon", new BigDecimal("-70.91546").doubleValue()).put("wayPoint", false).put("destination", false)));

        Mission mission = json.mapTo(Mission.class);

        JsonObject json2 = new JsonObject().put("id", UUID.randomUUID().toString()).put("incidentId", "incident456")
                .put("incidentLat", new BigDecimal("30.12345").doubleValue()).put("incidentLong", new BigDecimal("-70.98765").doubleValue())
                .put("responderId", "responder456")
                .put("responderStartLat", new BigDecimal("31.12345").doubleValue()).put("responderStartLong", new BigDecimal("-71.98765").doubleValue())
                .put("destinationLat", new BigDecimal("32.12345").doubleValue()).put("destinationLong", new BigDecimal("-72.98765").doubleValue())
                .put("status", "CREATED").put("responderLocationHistory", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.45678").doubleValue())
                        .put("lon", new BigDecimal("-70.65432").doubleValue()).put("timestamp", Instant.now().toEpochMilli())))
                .put("steps", new JsonArray().add(new JsonObject().put("lat", new BigDecimal("30.14785").doubleValue())
                        .put("lon", new BigDecimal("-70.91546").doubleValue()).put("wayPoint", false).put("destination", false)));

        Mission mission2 = json2.mapTo(Mission.class);

        missionRepository.add(mission);
        missionRepository.add(mission2);

        List<Mission> fromCache = missionRepository.getByResponderId("responder123");

        assertThat(fromCache.size(), equalTo(1));
        Mission missionFromCache = fromCache.get(0);
        assertThat(missionFromCache.getResponderId(), equalTo("responder123"));

    }



}
