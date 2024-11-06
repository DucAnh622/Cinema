package com.example.movie.model.Specifications;

import com.example.movie.model.Actor;
import org.springframework.data.jpa.domain.Specification;

public class ActorSpecifications {
    public static Specification<Actor> hasCountryId(Integer countryId) {
        return (root, query, criteriaBuilder) ->
                countryId != null ? criteriaBuilder.equal(root.get("countryId"), countryId) : null;
    }

    public static Specification<Actor> hasAgeBetween(Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            if (minAge != null && maxAge != null) {
                return criteriaBuilder.between(root.get("age"), minAge, maxAge);
            } else if (minAge != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge);
            } else if (maxAge != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge);
            }
            return null;
        };
    }
}
