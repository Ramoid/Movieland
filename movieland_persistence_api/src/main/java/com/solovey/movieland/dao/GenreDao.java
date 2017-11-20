package com.solovey.movieland.dao;

import com.solovey.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    void addGenreMapping(List<Genre> genres, int movieId);

    void removeGenreMappingsByIds(List<Genre> genres, int movieId);
}
