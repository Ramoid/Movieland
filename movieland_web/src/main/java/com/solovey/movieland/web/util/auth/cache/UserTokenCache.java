package com.solovey.movieland.web.util.auth.cache;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.web.util.auth.exceptions.UserNotFoundException;
import com.solovey.movieland.web.util.auth.exceptions.UserTokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserTokenCache {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${token_strorage_time_mins}")
    private int tokenStorageTime;

    private Map<String, User> userTokenCache = new ConcurrentHashMap<>();

    public String getUserToken(User user) {
        log.info("Start getting token");
        long startTime = System.currentTimeMillis();
        String token;
        for (Map.Entry<String, User> entry : userTokenCache.entrySet()) {
            if (entry.getValue().getId() == user.getId()) {
                //extract token from cache if present in cache and is not expired
                if (!entry.getValue().getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
                    token = entry.getKey();
                    log.info("Token has been received from cache. It took {} ms", System.currentTimeMillis() - startTime);
                    return token;
                }
            }
        }

        token = UUID.randomUUID().toString();
        user.setTokenGeneratedDateTime(LocalDateTime.now());
        userTokenCache.put(token, user);
        log.info("Token has been generated. It took {} ms", System.currentTimeMillis() - startTime);
        return token;
    }

    public void removeTokenFromCache(String tokenToRemove) {
        log.info("Start removing token {}" + tokenToRemove);
        long startTime = System.currentTimeMillis();

        User removedUser = userTokenCache.remove(tokenToRemove);

        if (removedUser!=null) {
            log.info("User {} has been removed from cache. It took {} ms", removedUser.toString(), System.currentTimeMillis() - startTime);
        }
        else{
            log.info("Token {} does not exist in cache. It took {} ms", tokenToRemove, System.currentTimeMillis() - startTime);
            //may be raise TokenNotFoundException
        }

    }

    public User findUserByToken(String tokenToSearch) throws UserNotFoundException {
        log.info("Start searching user");
        long startTime = System.currentTimeMillis();

        User user = userTokenCache.get(tokenToSearch);
        if (user == null) {
            log.info("User was not found for token {}. It took {} ms", tokenToSearch, System.currentTimeMillis() - startTime);
            throw new UserNotFoundException();
        }
        if (user.getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
            removeTokenFromCache(tokenToSearch);
            log.info("User has been found with expired token {}. It took {} ms", tokenToSearch, System.currentTimeMillis() - startTime);
            throw new UserTokenExpiredException();
        }

        log.info("User {} has been  found for token {}. It took {} ms", user.toString(), tokenToSearch, System.currentTimeMillis() - startTime);


        return user;
    }

    @Scheduled(fixedDelayString = "${token_garbage_collector_interval}", initialDelayString = "${token_garbage_collector_interval}")
    private void tokenCacheGarbageCollector() {
        log.info("Start token cache garbage collector");
        long startTime = System.currentTimeMillis();

        for (Iterator<User> iter = userTokenCache.values().iterator(); iter.hasNext(); ) {
            User user = iter.next();
            if (user.getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
                iter.remove();
                log.info("User {} removed from cache", user.toString());
            }
        }

        log.info("Token cache garbage collector finished work. It took {} ms", System.currentTimeMillis() - startTime);
    }

}
