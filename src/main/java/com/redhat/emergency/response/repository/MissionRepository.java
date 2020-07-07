package com.redhat.emergency.response.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.emergency.response.model.Mission;

@ApplicationScoped
public class MissionRepository {

    private static final ConcurrentHashMap<String, Mission> repository = new ConcurrentHashMap<>();

    public void add(Mission mission) {
        repository.put(mission.getKey(), mission);
    }

    public Optional<Mission> get(String key) {
        return Optional.ofNullable(repository.get(key));
    }

    public List<Mission> getAll() {
        return new ArrayList<>(repository.values());
    }

    public void clear() {
        repository.clear();
    }

    public List<Mission> getByResponderId(String responderId) {
         return repository.values().stream()
            .filter(m -> m.getResponderId().equals(responderId)).collect(Collectors.toList());
    }
}
