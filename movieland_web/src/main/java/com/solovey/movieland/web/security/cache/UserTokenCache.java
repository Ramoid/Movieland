package com.solovey.movieland.web.security.cache;


import com.solovey.movieland.entity.User;
import com.solovey.movieland.web.security.entity.TokenCacheUser;
import com.solovey.movieland.web.security.exceptions.UserNotFoundException;
import com.solovey.movieland.web.security.exceptions.UserTokenExpiredException;
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

    private Map<String, TokenCacheUser> userTokenCache = new ConcurrentHashMap<>();

    public String getUserToken(User user) {
        log.info("Start getting token");
        long startTime = System.currentTimeMillis();
        String token;
        for (Map.Entry<String, TokenCacheUser> entry : userTokenCache.entrySet()) {
            if (entry.getValue().getUser().getId() == user.getId()) {
                //extract token from cache if present in cache and is not expired
                if (!entry.getValue().getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
                    token = entry.getKey();
                    log.info("Token has been received from cache. It took {} ms", System.currentTimeMillis() - startTime);
                    return token;
                }
            }
        }

        token = UUID.randomUUID().toString();
        TokenCacheUser tokenCacheUser = new TokenCacheUser();
        tokenCacheUser.setTokenGeneratedDateTime(LocalDateTime.now());
        tokenCacheUser.setUser(user);
        userTokenCache.put(token, tokenCacheUser);
        log.info("Token has been generated. It took {} ms", System.currentTimeMillis() - startTime);
        return token;
    }

    public void removeTokenFromCache(String tokenToRemove) {
        log.info("Start removing token {}" + tokenToRemove);
        long startTime = System.currentTimeMillis();

        User removedUser = userTokenCache.remove(tokenToRemove).getUser();

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

        TokenCacheUser tokenCacheUser = userTokenCache.get(tokenToSearch);
        if (tokenCacheUser == null) {
            log.info("User was not found for token {}. It took {} ms", tokenToSearch, System.currentTimeMillis() - startTime);
            throw new UserNotFoundException();
        }
        if (tokenCacheUser.getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
            removeTokenFromCache(tokenToSearch);
            log.info("User has been found with expired token {}. It took {} ms", tokenToSearch, System.currentTimeMillis() - startTime);
            throw new UserTokenExpiredException();
        }

        log.info("User has been  found for token {}. It took {} ms",  tokenToSearch, System.currentTimeMillis() - startTime);


        return tokenCacheUser.getUser();
    }

    @Scheduled(fixedDelayString = "${token_garbage_collector_interval}", initialDelayString = "${token_garbage_collector_interval}")
    private void tokenCacheGarbageCollector() {
        log.info("Start token cache garbage collector");
        long startTime = System.currentTimeMillis();

        for (Iterator<TokenCacheUser> iter = userTokenCache.values().iterator(); iter.hasNext(); ) {
            TokenCacheUser tokenCacheUser = iter.next();
            if (tokenCacheUser.getTokenGeneratedDateTime().isBefore(LocalDateTime.now().minusMinutes(tokenStorageTime))) {
                iter.remove();
                log.info("User {} removed from cache", tokenCacheUser.getUser().getEmail());
            }
        }

        log.info("Token cache garbage collector finished work. It took {} ms", System.currentTimeMillis() - startTime);
    }

}
