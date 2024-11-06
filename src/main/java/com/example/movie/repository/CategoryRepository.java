package com.example.movie.repository;

import com.example.movie.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findTop3ByOrderByIdDesc();
    @Query("SELECT a FROM Category a JOIN a.movies m WHERE m.id = :movieId")
    List<Category> findAllByMovieId(Integer movieId);
}
