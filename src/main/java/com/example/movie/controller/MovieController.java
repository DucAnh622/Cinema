package com.example.movie.controller;

import com.example.movie.DTO.ActorMovieDTO;
import com.example.movie.DTO.CategoryMovieDTO;
import com.example.movie.model.Movie;
import com.example.movie.model.Serie;
import com.example.movie.service.MovieService;
import com.example.movie.util.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie/")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/public/top")
    ResponseEntity<?> getTop (@RequestParam String mode,
                              @RequestParam(required = false) Integer category
                             )
    {
        return movieService.getTop(mode,category);
    }

    @GetMapping("/public/paginateBy")
    ResponseEntity<?> getPaginateBy (@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) Integer category,
                                     @RequestParam(required = false) Integer actor,
                                     @RequestParam(required = false) String type
    ) {
        return movieService.getPaginateBy(page, size, category, actor, type);
    }

    @GetMapping("/public/getByCategory")
    ResponseEntity<?> getMovieByCateory (@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) Integer categoryId) {
        return movieService.getByCategory(page, size, categoryId);
    }

    @GetMapping("/public/getByActor")
    ResponseEntity<?> getMovieByActor (@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) Integer actorId) {
        return movieService.getByActor(page, size, actorId);
    }

    @GetMapping("/public/paginate")
    ResponseEntity<?> getPaginate (@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String key) {
        return movieService.getPaginate(page,size,key);
    }

    @PostMapping("/system/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> create(@RequestBody Movie movie) {
        return movieService.create(movie);
    }
    @GetMapping("/public/find")
    ResponseEntity<?> find(@RequestParam Integer id) {
        return movieService.getById(id);
    }

    @GetMapping("/public/detail")
    ResponseEntity<?> getDetail(@RequestParam Integer id) {
        return movieService.getDetailMovie(id);
    }

    @PutMapping("/system/update")
    @ApiMessage("Update success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> update(@RequestBody Movie movie) {
        return movieService.update(movie);
    }

    @DeleteMapping("/system/delete")
    @ApiMessage("Delete success!")
    ResponseEntity<?> delete(@RequestParam Integer id) {
        return movieService.delete(id);
    }

    @GetMapping("/public/serie")
    ResponseEntity<?> getEpisode(@RequestParam Integer id) {
        return movieService.getSerieById(id);
    }

    @PostMapping("/system/serie/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> createSerie(@RequestBody Serie serie) {
        return movieService.CreateEpisode(serie);
    }

    @PutMapping("/system/serie/update")
    @ApiMessage("Update success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> updateSerie(@RequestBody Serie serie) {
        return movieService.UpdateEpisode(serie);
    }

    @PostMapping("/system/actor_movie/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> createActorMovie(@RequestBody ActorMovieDTO actorMovieDTO) {
        return movieService.createActorMovie(actorMovieDTO.getMovie_id(),actorMovieDTO.getActor_id());
    }

    @PostMapping("/system/category_movie/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> createCategoryMovie(@RequestBody CategoryMovieDTO categoryMovieDTO) {
        return movieService.createCategoryMovie(categoryMovieDTO.getMovie_id(),categoryMovieDTO.getCategory_id());
    }

    @DeleteMapping("/system/actor_movie/delete")
    @ApiMessage("Delete success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> deleteActorMovie(@RequestBody ActorMovieDTO actorMovieDTO) {
        return movieService.deleteActorMovie(actorMovieDTO.getMovie_id(),actorMovieDTO.getActor_id());
    }

    @DeleteMapping("/system/category_movie/delete")
    @ApiMessage("Delete success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> deleteCategoryMovie(@RequestBody CategoryMovieDTO categoryMovieDTO) {
        return movieService.deleteCategoryMovie(categoryMovieDTO.getMovie_id(),categoryMovieDTO.getCategory_id());
    }
}
