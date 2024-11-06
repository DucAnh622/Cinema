package com.example.movie.controller;
import com.example.movie.model.Category;
import com.example.movie.service.CategoryService;
import com.example.movie.util.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category/")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController (CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/all")
    public ResponseEntity<?> findAll() {
        return  categoryService.getAll_cateory();
    }

    @GetMapping("/system/paginate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public  ResponseEntity<?> paginate(@RequestParam int page, @RequestParam int size) {
        return categoryService.getPaginate(page, size);
    }

    @PostMapping("/system/create")
    @ApiMessage("Create success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @GetMapping("/public/top")
    public ResponseEntity<?> getTop5() {
        return categoryService.getTop5();
    }

    @GetMapping("/public/find")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return categoryService.findById(id);
    }

    @DeleteMapping("/system/delete")
    @ApiMessage("Delete success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        return categoryService.delete(id);
    }

    @PutMapping("/system/update")
    @ApiMessage("Update success!")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody Category category) {
        return categoryService.update(category);
    }
}
