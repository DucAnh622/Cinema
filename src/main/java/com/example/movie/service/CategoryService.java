package com.example.movie.service;

import com.example.movie.model.Category;
import com.example.movie.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<?> getPaginate(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> listCategory = categoryRepository.findAll(pageable);
        if(!listCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listCategory);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getAll_cateory() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        }
    }

    public ResponseEntity<?> create(Category category) {
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else {
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> findById(Integer id) {
        Optional<Category> categoryDetail = categoryRepository.findById(id);
        if(!categoryDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(categoryDetail);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        Optional<Category> categoryDetail = categoryRepository.findById(id);
        if(!categoryDetail.isEmpty()) {
            categoryRepository.delete(categoryDetail.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> update(Category category) {
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<Category> categoryOptional = categoryRepository
                .findById(category.getId());
        if (categoryOptional.isPresent()) {
            Category categoryDetail = categoryOptional.get();
            categoryDetail.setName(category.getName());
            categoryRepository.save(categoryDetail);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<?> getTop5 () {
        List<Category> categoryList = categoryRepository.findTop3ByOrderByIdDesc();
        if(!categoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(categoryList);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}
