package com.tcm.prescription.repository;

import com.tcm.prescription.entity.Herb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HerbRepository extends JpaRepository<Herb, Long> {
    Optional<Herb> findByName(String name);
    List<Herb> findByNature(String nature);
    List<Herb> findByToxicityLevel(Integer toxicityLevel);
    List<Herb> findByNameContaining(String keyword);
}