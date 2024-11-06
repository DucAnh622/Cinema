package com.example.movie.repository;

import com.example.movie.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>,JpaSpecificationExecutor<Actor> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Actor> findTop10ByOrderByBirthdayDesc();
    @Query(value = "SELECT * FROM actor WHERE EXTRACT(YEAR FROM AGE(CURRENT_DATE, TO_DATE(birthday, 'YYYY-MM-DD'))) > :age ORDER BY TO_DATE(birthday, 'YYYY-MM-DD') DESC LIMIT 8", nativeQuery = true)
    List<Actor> findRandomTop8();
    @Query("SELECT a FROM Actor a JOIN a.movies m WHERE m.id = :movieId")
    List<Actor> findAllByMovieId(Integer movieId);
}
