package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.mapper.CarbonEmissionsMapper;
import br.com.actionlabs.carboncalc.model.CarbonEmissions;
import br.com.actionlabs.carboncalc.repository.CarbonEmissionsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CarbonCalculatorService {

    private final CarbonEmissionsRepository carbonEmissionsRepository;

    private final CalculationService calculationService;

    public CarbonCalculatorService(CarbonEmissionsRepository carbonEmissionsRepository,
                                   CalculationService calculationService) {
        this.carbonEmissionsRepository = carbonEmissionsRepository;
        this.calculationService = calculationService;
    }

    public CarbonCalculationResultDTO getCarbonEmission(String id) {
        CarbonEmissions emission = this.getCarbonEmissionById(id);
        return CarbonCalculationResultDTO.fromEntity(emission);
    }

    public StartCalcResponseDTO startCalculation(StartCalcRequestDTO requestDTO) {
        CarbonEmissions footprint = this.saveCalculation(CarbonEmissionsMapper.fromDTO(requestDTO));
        return new StartCalcResponseDTO(footprint.getId());
    }

    public UpdateCalcInfoResponseDTO updateCalculation(UpdateCalcInfoRequestDTO request) throws IllegalArgumentException {
        final int wasteTotal = request.getSolidWasteTotal();
        final double recyclePerc = request.getRecyclePercentage();
        final List<TransportationDTO> transports = request.getTransportation();
        CarbonEmissions footprint = this.getCarbonEmissionById(request.getId());
        final String uf = footprint.getUf();
        SolidWasteEmissionsDTO wasteEmissions = calculationService.calculateSolidWasteEmission(uf, wasteTotal, recyclePerc);
        footprint.setEnergy(calculationService.calculateEnergyEmission(uf, request.getEnergyConsumption()));
        footprint.setTransportation(calculationService.calculateTransportationEmission(transports));
        footprint.setSolidWasteRecyclable(wasteEmissions.getRecyclableEmission());
        footprint.setSolidWasteNonRecyclable(wasteEmissions.getNonRecyclableEmission());
        footprint.setEmissionsTotal(Double.sum(footprint.getEnergy(), footprint.getTransportation())
                + wasteEmissions.getTotalEmission());
        this.saveCalculation(footprint);
        return new UpdateCalcInfoResponseDTO(true);
    }

    private CarbonEmissions getCarbonEmissionById(String id) throws NoSuchElementException {
        return carbonEmissionsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Calculation (%s) not found", id)));
    }

    private CarbonEmissions saveCalculation(CarbonEmissions emission) throws DataAccessException {
        return Optional.of(carbonEmissionsRepository.save(emission))
                .orElseThrow(() -> new DataAccessException("Failed to save calculation") {});
    }

}
