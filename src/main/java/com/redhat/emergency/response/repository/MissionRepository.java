package com.redhat.emergency.response.repository;


import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.redhat.emergency.response.model.Mission;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MissionRepository {

    @Inject
    RemoteCacheManager cacheManager;
    
    private static final Logger log = LoggerFactory.getLogger(MissionRepository.class);
    //    private static final ConcurrentHashMap<String, Mission> repository = new ConcurrentHashMap<>();

    RemoteCache<String, String> missionCache;

    void onStart(@Observes StartupEvent e) {
        log.info("Creating remote cache '" + cacheName + "'" );
        missionCache = initCache();
    }
    
    @ConfigProperty(name = "infinispan.cache.name.mission", defaultValue = "mission")
    String cacheName;
    private RemoteCache<String, String> initCache() {
        Configuration configuration = Configuration.builder().name("mission").mode("SYNC").owners(2).build();
        return cacheManager.administration().getOrCreateCache(cacheName, configuration);
    }
    
    public void add(Mission mission) {
        missionCache.put(mission.getKey(), mission.toJson());
    }

    public Optional<Mission> get(String key) {
	return Optional.ofNullable(missionCache.get(key)).map(s -> {
            Mission mission = null;
            try {
                mission = Json.decodeValue(s, Mission.class);
            } catch (DecodeException e) {
                log.error("Exception decoding mission with id = " + key, e);
            }
            return mission;
        });
    }

    public List<Mission> getAll() {
        return new ArrayList<>(missionCache.values().stream().map(s -> {
		    Mission mission = null;
		    try {
			mission = Json.decodeValue(s, Mission.class);
		    } catch (DecodeException e) {
			log.error("Exception decoding mission " + e);
		    }
		    return mission;
		}).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public void clear() {
        missionCache.clear();
    }

    public List<Mission> getByResponderId(String responderId) {
         return missionCache.values().stream().map(s -> {
            Mission mission = null;
            try {
                mission = Json.decodeValue(s, Mission.class);
            } catch (DecodeException e) {
                log.error("Exception decoding mission", e);
            }
            return mission;
	     }).filter(Objects::nonNull).filter(m -> m.getResponderId().equals(responderId)).collect(Collectors.toList());
    }
}
