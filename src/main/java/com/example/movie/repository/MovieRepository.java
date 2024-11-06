package com.example.movie.repository;

import com.example.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    Page<Movie>findByTitleContainingIgnoreCase(String key, Pageable pageable);
    @Query(value = "SELECT * FROM movie ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Movie> findRandomMovies();
    List<Movie> findTop10ByOrderByReleaseDateDesc();
    List<Movie> findTop10ByOrderByTitleAsc();
    @Query("SELECT m FROM Movie m JOIN m.categories c WHERE c.id = :categoryId")
    Page<Movie> findMoviesByCategoryId(Integer categoryId, Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.actors c WHERE c.id = :actorId")
    Page<Movie> findMoviesByActorId(Integer actorId, Pageable pageable);
}
