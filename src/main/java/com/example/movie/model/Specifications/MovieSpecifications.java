package com.example.movie.model.Specifications;

import com.example.movie.model.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecifications {
    public static Specification<Movie> hasCategoryId(Integer categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("categories").get("category").get("id"), categoryId);
        };
    }
    public static Specification<Movie> hasActorId(Integer actorId) {
        return (root, query, criteriaBuilder) -> {
            if (actorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("actors").get("actor").get("id"), actorId);
        };
    }

    public static Specification<Movie> hasType(String type) {
        return (root, query, criteriaBuilder) ->
                type != null ? criteriaBuilder.equal(root.get("type"), type) : null;
    }
}
