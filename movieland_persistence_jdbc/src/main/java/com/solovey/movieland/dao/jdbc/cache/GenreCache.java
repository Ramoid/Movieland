package com.solovey.movieland.dao.jdbc.cache;

import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class GenreCache implements GenreDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile List<Genre> genresCache;

    private final GenreDao genreDao;

    @Autowired
    public GenreCache(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genresCacheCopy = genresCache;
        List<Genre> genresCacheToReturn= new ArrayList<>();
        for (Genre genre : genresCacheCopy){
            Genre copyGenre= new Genre();
            copyGenre.setId(genre.getId());
            copyGenre.setName(genre.getName());
            genresCacheToReturn.add(copyGenre);
        }
        return genresCacheToReturn;
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${genre_cache_refresh_interval}", initialDelayString = "${genre_cache_refresh_interval}")
    private void invalidate() {
        log.info("Start genre cache refresh");
        long startTime = System.currentTimeMillis();
        genresCache = genreDao.getAllGenres();
        log.info("Genre chache has been reloaded. It took {} ms", System.currentTimeMillis() - startTime);
    }


}
