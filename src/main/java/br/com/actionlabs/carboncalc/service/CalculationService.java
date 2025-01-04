package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.SolidWasteEmissionsDTO;
import br.com.actionlabs.carboncalc.dto.TransportationDTO;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.repository.TransportationEmissionFactorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalculationService {

    private final TransportationEmissionFactorRepository transportationEmissionRepository;

    private final UFService ufService;

    public CalculationService(TransportationEmissionFactorRepository transportationEmissionFactorRepository,
                              UFService ufService) {
        this.transportationEmissionRepository = transportationEmissionFactorRepository;
        this.ufService = ufService;
    }

    public double calculateEnergyEmission(String uf, int consumption) {
        Double factor = ufService.getEnergyEmissionFactor(uf);
        return factor * consumption;
    }

    public double calculateTransportationEmission(List<TransportationDTO> transports) throws IllegalArgumentException{
        double totalEmission = 0.0;
        if(transports == null || transports.isEmpty()) {
            return totalEmission;
        }
        Map<TransportationType, Integer> distanceByTransportation =
                transports.stream()
                        .collect(Collectors.groupingBy(TransportationDTO::getType, Collectors.summingInt(TransportationDTO::getMonthlyDistance)));
        for (Map.Entry<TransportationType, Integer> entry : distanceByTransportation.entrySet()) {
            double factor = transportationEmissionRepository.findByType(entry.getKey()).getFactor();
            totalEmission += factor * entry.getValue();
        }
        return totalEmission;
    }

    public SolidWasteEmissionsDTO calculateSolidWasteEmission(String uf, int wasteTotal, double recyclePercentage) throws IllegalStateException{
        Map<Double, Double> factors = ufService.getSolidWasteEmissionFactor(uf);
        double recyclableFactor, nonRecyclableFactor, recyclableWaste, nonRecyclableWaste;
        if (factors.size() != 1) {
            throw new IllegalStateException(String.format(
                    "Unexpected number of waste factors for UF %s. " +
                            "Expected 1 entry, but found %d.", uf, factors.size()));
        }
        Map.Entry<Double, Double> entry = factors.entrySet().iterator().next();
        recyclableFactor = entry.getKey();
        nonRecyclableFactor = entry.getValue();
        recyclableWaste = wasteTotal * recyclePercentage;
        nonRecyclableWaste = wasteTotal - recyclableWaste;
        return new SolidWasteEmissionsDTO(recyclableWaste * recyclableFactor, nonRecyclableWaste * nonRecyclableFactor);
    }
}