package com.example.movie.service;

import com.example.movie.model.*;
import com.example.movie.model.Specifications.MovieSpecifications;
import com.example.movie.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService {
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final SerieRepository serieRepository;
    private  final ActorRepository actorRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, SerieRepository serieRepository, CategoryRepository categoryRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.serieRepository = serieRepository;
        this.categoryRepository = categoryRepository;
        this.actorRepository = actorRepository;
    }

    public ResponseEntity<?> getTop (String mode, Integer category_id) {
        List<Movie> movieList = new ArrayList<>();
        switch (mode) {
            case "1":
                movieList = movieRepository.findTop10ByOrderByReleaseDateDesc();
                break;
            case "2":
                movieList = movieRepository.findRandomMovies();
                break;
            case "3":
                movieList = movieRepository.findTop10ByOrderByTitleAsc();
                break;
            case "4":
                Pageable pageable = PageRequest.of(0, 5);
                Page<Movie> moviePage = movieRepository.findMoviesByCategoryId(category_id, pageable);
                movieList = moviePage.getContent();
                break;
            default:
                break;
        }
        if(!movieList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(movieList);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getPaginate(int page, int size, String key) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Movie> listMovie = null;
        if(key != null || !key.isEmpty()) {
            listMovie = movieRepository.findByTitleContainingIgnoreCase(key,pageable);
        }
        else {
            listMovie = movieRepository.findAll(pageable);
        }
        if(!listMovie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listMovie);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> create(Movie movie) {
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else {
            movieRepository.save(movie);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getDetailMovie(int id) {
        Optional<Movie> movieDetail = movieRepository.findById(id);
        if (movieDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Serie> series = serieRepository.findByMovieId(id);
        List<Actor> actors = actorRepository.findAllByMovieId(id);
        List<Category> categories = categoryRepository.findAllByMovieId(id);
        Movie movie = movieDetail.get();
        Map<String, Object> response = new HashMap<>();
        response.put("movie", movie);
        response.put("series", series);
        response.put("actors",actors);
        response.put("categories",categories);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> getById(int id) {
        Optional<Movie> movieDetail = movieRepository.findById(id);
        if(movieDetail != null) {
            return ResponseEntity.status(HttpStatus.OK).body(movieDetail);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> update(Movie movie) {
        Optional<Movie> movieDetail = movieRepository.findById(movie.getId());
        if(movieDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            Movie movieToUpdate = movieDetail.get();
            movieToUpdate.setTitle(movie.getTitle());
            movieToUpdate.setDescription(movie.getDescription());
            movieToUpdate.setCountryId(movie.getCountryId());
            movieToUpdate.setImage(movie.getImage());
            movieToUpdate.setThumb(movie.getThumb());
            movieToUpdate.setStatus(movie.getStatus());
            movieToUpdate.setTime(movie.getTime());
            movieToUpdate.setQuality(movie.getQuality());
            movieToUpdate.setTrailer(movie.getTrailer());
            movieToUpdate.setDirector(movie.getDirector());
            movieToUpdate.setReleaseDate(movie.getReleaseDate());
            movieToUpdate.setType(movie.getType());
            movieRepository.save(movieToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getSerieById(Integer id) {
        Optional<Movie> movieDetail = movieRepository.findById(id);
        if(movieDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            List<Serie> serieList = serieRepository.findByMovieId(id);
            return ResponseEntity.status(HttpStatus.OK).body(serieList);
        }
    }

    public ResponseEntity<?> CreateEpisode(Serie serie) {
        if (serie == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else {
            serieRepository.save(serie);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> UpdateEpisode(Serie serie) {
        Optional<Serie> serieDetail = serieRepository.findById(serie.getId());
        if (serieDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Serie serieUpdate = serieDetail.get();
        serieUpdate.setEpisode(serie.getEpisode());
        serieUpdate.setName(serie.getName());
        serieRepository.save(serieUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity<?> delete(int id) {
        Optional<Movie> movieDetail = movieRepository.findById(id);
        if(movieDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            movieRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getByCategory(int page, int size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Movie> movies = movieRepository.findMoviesByCategoryId(categoryId,pageable);
        if(!movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(movies);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getByActor(int page, int size, Integer actorId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Movie> movies = movieRepository.findMoviesByActorId(actorId,pageable);
        if(!movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(movies);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getPaginateBy(int page, int size, Integer categoryId, Integer actorId, String type) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Movie> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and(MovieSpecifications.hasCategoryId(categoryId));
        }

        if (actorId != null) {
            spec = spec.and(MovieSpecifications.hasActorId(actorId));
        }

        if(type != null || !type.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasType(type));
        }
        Page<Movie> listMovie = movieRepository.findAll(spec, pageable);
        if(!listMovie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listMovie);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> createActorMovie(Integer movieId, Integer actorId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        Actor actor = actorRepository.findById(actorId).orElse(null);
        if (actor == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        movie.getActors().add(actor);
        movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity<?> createCategoryMovie(Integer movieId, Integer categoryId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        movie.getCategories().add(category);
        movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity<?> deleteActorMovie(Integer movieId, Integer actorId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
        }
        Actor actor = actorRepository.findById(actorId).orElse(null);
        if (actor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actor not found");
        }

        if (movie.getActors().contains(actor)) {
            movie.getActors().remove(actor);
            movieRepository.save(movie);
            return ResponseEntity.status(HttpStatus.OK).body("Actor removed from movie successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Actor is not associated with this movie");
        }
    }

    public ResponseEntity<?> deleteCategoryMovie(Integer movieId, Integer categoryId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
        }
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        if (movie.getCategories().contains(category)) {
            movie.getCategories().remove(category);
            movieRepository.save(movie);
            return ResponseEntity.status(HttpStatus.OK).body("Category removed from movie successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is not associated with this movie");
        }
    }
}


