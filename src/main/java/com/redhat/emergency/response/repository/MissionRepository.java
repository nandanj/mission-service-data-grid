package com.redhat.emergency.response.repository;

import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.emergency.response.model.Mission;

@ApplicationScoped
public class MissionRepository {

    private static final ConcurrentHashMap<String, Mission> repository = new ConcurrentHashMap<>();

    public void add(Mission mission) {
        repository.put(mission.getKey(), mission);
    }

}
