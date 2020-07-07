package com.redhat.emergency.response.rest;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.emergency.response.model.Mission;
import com.redhat.emergency.response.repository.MissionRepository;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;

@ApplicationScoped
public class RestApi {

    @Inject
    MissionRepository repository;

    @Route(path = "/api/missions", methods = HttpMethod.GET, produces = "application/json")
    void allMissions(RoutingExchange ex) {

        List<Mission> missions = repository.getAll();
        ex.response().putHeader("Content-Type", "application/json")
                .setStatusCode(200).end(Json.encode(missions));
    }

    @Route(path = "/api/missions/clear", methods = HttpMethod.POST)
    void clearAll(RoutingExchange ex) {

        repository.clear();
        ex.response().putHeader("Content-Type", "application/json")
                .setStatusCode(201).end();
    }

    @Route(path = "/api/missions/responders/:id", methods = HttpMethod.GET, produces = "application/json")
    void missionByResponder(RoutingExchange ex) {

        ex.getParam("id").ifPresentOrElse(responderId -> {
            Optional<Mission> mission = repository.getByResponderId(responderId).stream()
                    .filter(m -> m.getStatus().equalsIgnoreCase("UPDATED") || (m.getStatus().equalsIgnoreCase("CREATED"))).findFirst();
            mission.ifPresentOrElse(m -> ex.response().putHeader("Content-Type", "application/json").setStatusCode(200)
                    .end(Json.encode(m)), () -> ex.response().setStatusCode(204).end());
            }, () -> ex.response().setStatusCode(204).end());
    }

}
