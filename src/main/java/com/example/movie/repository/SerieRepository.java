package com.example.movie.repository;

import com.example.movie.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Integer> {
    List<Serie> findByMovieId(int movieId);
}
