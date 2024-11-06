package com.example.movie.service;

import com.example.movie.model.Actor;
import com.example.movie.model.Specifications.ActorSpecifications;
import com.example.movie.repository.ActorRepository;
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
import java.util.stream.Collectors;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public ResponseEntity<?> getPaginate(int page, int size, String key) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Actor> listActor = null;
        if(key != null || !key.isEmpty()) {
            listActor = actorRepository.findByNameContainingIgnoreCase(key, pageable);
        }
        else {
            listActor = actorRepository.findAll(pageable);
        }
        if(!listActor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listActor);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getPaginateFilter(int page, int size, Integer minAge, Integer maxAge, Integer countryId, String type) {
        Sort sort = Sort.by("name").ascending();
        if ("desc".equalsIgnoreCase(type) && type != null && !type.isEmpty()) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Actor> spec = Specification.where(null);

        if (countryId != null) {
            spec = spec.and(ActorSpecifications.hasCountryId(countryId));
        }
        if (minAge != null && maxAge != null) {
            spec = spec.and(ActorSpecifications.hasAgeBetween(minAge, maxAge));
        }
        Page<Actor> listActor = actorRepository.findAll(spec, pageable);
        if (!listActor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listActor);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> getTop (String mode, Integer minAge, Integer maxAge) {
        List<Actor> listActor = new ArrayList<>();
        Specification<Actor> spec = Specification.where(null);
        if (minAge != null && maxAge != null) {
            spec = spec.and(ActorSpecifications.hasAgeBetween(minAge, maxAge));
        }
        switch (mode) {
            case "1":
                listActor = actorRepository.findTop10ByOrderByBirthdayDesc();
                break;
            case "2":
                if(maxAge != null && minAge != null) {
                    Pageable pageable = PageRequest.of(0,8);
                    listActor = actorRepository.findAll(spec, pageable).getContent();
                }
                break;
            default:
                listActor = actorRepository.findRandomTop8();
                break;
        }
        if(!listActor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listActor);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> findAll() {
        List<Actor> actors = actorRepository.findAll();
        if(actors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            List<Map<String, Object>> result = actors.stream()
                    .map(actor -> {
                        Map<String, Object> actorInfo = new HashMap<>();
                        actorInfo.put("id", actor.getId());
                        actorInfo.put("name", actor.getName());
                        return actorInfo;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    public ResponseEntity<?> create(Actor actor) {
        if (actor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else {
            actorRepository.save(actor);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> findById(Integer id) {
        Optional<Actor> actorDetail = actorRepository.findById(id);
        if(actorDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(actorDetail);
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        Optional<Actor> actorDetail = actorRepository.findById(id);
        if(actorDetail != null) {
            actorRepository.delete(actorDetail.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> update(Actor actor) {
        if (actor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<Actor> actorDetailOptional = actorRepository.findById(actor.getId());
        if (actorDetailOptional.isPresent()) {
            Actor actorDetail = actorDetailOptional.get();
            actorDetail.setName(actor.getName());
            actorDetail.setBirthday(actor.getBirthday());
            actorDetail.setAge(actor.getAge());
            actorDetail.setCountryId(actor.getCountryId());
            actorDetail.setImage(actor.getImage());
            actorDetail.setAge(actor.getAge());
            actorRepository.save(actorDetail);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
