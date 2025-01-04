package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;
import br.com.actionlabs.carboncalc.repository.EnergyEmissionFactorRepository;
import br.com.actionlabs.carboncalc.repository.SolidWasteEmissionFactorRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UFService {

    EnergyEmissionFactorRepository energyRepository;
    SolidWasteEmissionFactorRepository solidWasteRepository;

    public UFService(EnergyEmissionFactorRepository energyRepository,
                     SolidWasteEmissionFactorRepository solidWasteRepository) {
        this.energyRepository = energyRepository;
        this.solidWasteRepository = solidWasteRepository;
    }

    public Double getEnergyEmissionFactor(String uf) throws NoSuchElementException {
        return Optional.ofNullable(energyRepository.findByUf(uf))
                .map(EnergyEmissionFactor::getFactor)
                .orElseThrow(() -> new NoSuchElementException(String.format("No energy emission factor found (%s)", uf)));
    }


    public Map<Double, Double> getSolidWasteEmissionFactor(String uf) throws NoSuchElementException {
        return Optional.ofNullable(solidWasteRepository.findByUf(uf))
                .map(factor -> Map.of(factor.getRecyclableFactor(), factor.getNonRecyclableFactor()))
                .orElseThrow(() -> new NoSuchElementException(String.format("No solid waste emission factor found (%s)", uf)));
    }
}