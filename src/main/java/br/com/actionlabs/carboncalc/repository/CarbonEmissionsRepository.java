package br.com.actionlabs.carboncalc.repository;

import br.com.actionlabs.carboncalc.model.CarbonEmissions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarbonEmissionsRepository extends MongoRepository<CarbonEmissions, String> {
    Optional<CarbonEmissions> findById(String id);
}
