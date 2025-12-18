package com.cvsnewsletter.repositories;

import com.cvsnewsletter.entities.Kudos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KudosRepository extends JpaRepository<Kudos, Integer> {
    List<Kudos> findByIsApprovedFalse();

    List<Kudos> findByIsApprovedTrueOrderByDateDesc();
}
