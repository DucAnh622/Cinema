package com.example.movie.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.movie.model.Actor;
import com.example.movie.service.ActorService;
import com.example.movie.util.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actor/")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/public/paginate")
    ResponseEntity<?> getPaginate (@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String key) {
        return actorService.getPaginate(page, size, key);
    }

    @GetMapping("/public/top")
    ResponseEntity<?> getTop (@RequestParam(required = false) String mode,
                              @RequestParam(required = false) Integer maxAge,
                              @RequestParam(required = false) Integer minAge
                             )
    {
        return actorService.getTop(mode, minAge, maxAge);
    }

    @GetMapping("/public/paginate/filter")
    ResponseEntity<?> getPaginateFilter (@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) Integer countryId,
                                         @RequestParam(required = false) Integer minAge,
                                         @RequestParam(required = false) Integer maxAge,
                                         @RequestParam(required = false) String type
                                         )
    {
        return actorService.getPaginateFilter(page, size, minAge, maxAge, countryId, type);
    }

    @GetMapping("/system/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> getAll() {
        return actorService.findAll();
    }
    @PostMapping("/system/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> create(@RequestBody Actor actor) {
        return actorService.create(actor);
    }
     @GetMapping("/public/find")
     ResponseEntity<?> find(@RequestParam Integer id) {
         return actorService.findById(id);
     }

     @PutMapping("/system/update")
     @ApiMessage("Update success!")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
     ResponseEntity<?> update(@RequestBody Actor actor) {
         return actorService.update(actor);
     }

     @DeleteMapping("/system/delete")
     @ApiMessage("Delete success!")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
     ResponseEntity<?> delete(@RequestParam Integer id) {
         return actorService.delete(id);
     }
}
