package com.solovey.movieland.web.util.dto;

import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDtoConverter {
    public List<MovieDto> convertMoviestoMoviesDto(List<Movie> movies){
        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie:movies){
            MovieDto movieDto=new MovieDto();
            movieDto.setMovieId(movie.getMovieId());
            movieDto.setCountries(movie.getCountries());
            movieDto.setDescription(movie.getDescription());
            movieDto.setGenres(movie.getGenres());
            movieDto.setNameNative(movie.getNameNative());
            movieDto.setNameRussian(movie.getNameRussian());
            movieDto.setPicturePath(movie.getPicturePath());
            movieDto.setPrice(movie.getPrice());
            movieDto.setRating(movie.getRating());
            movieDto.setYearOfRelease(movie.getYearOfRelease());
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }
    public MovieDto convertMovietoMovieDto(Movie movie){

            List<ReviewDto> reviewDtos = new ArrayList<>();

            MovieDto movieDto=new MovieDto();
            movieDto.setMovieId(movie.getMovieId());
            movieDto.setCountries(movie.getCountries());
            movieDto.setDescription(movie.getDescription());
            movieDto.setGenres(movie.getGenres());
            movieDto.setNameNative(movie.getNameNative());
            movieDto.setNameRussian(movie.getNameRussian());
            movieDto.setPicturePath(movie.getPicturePath());
            movieDto.setPrice(movie.getPrice());
            movieDto.setRating(movie.getRating());
            movieDto.setYearOfRelease(movie.getYearOfRelease());

            if ( movie.getReviews()!=null) {
                for (Review review : movie.getReviews()) {
                    ReviewDto reviewDto = new ReviewDto();
                    reviewDto.setId(review.getId());
                    reviewDto.setText(review.getText());
                    UserDto userDto = new UserDto();
                    userDto.setId(review.getUser().getId());
                    userDto.setNickname(review.getUser().getNickname());
                    reviewDto.setUser(userDto);
                    reviewDtos.add(reviewDto);
                }
                movieDto.setReviews(reviewDtos);
            }

        return movieDto;
    }


}
