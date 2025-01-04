package br.com.actionlabs.carboncalc.mapper;

import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.model.CarbonEmissions;

import java.util.Optional;

public class CarbonEmissionsMapper {
    public static CarbonEmissions fromDTO(StartCalcRequestDTO dto) throws IllegalArgumentException {
        if (dto == null) {
            throw new IllegalArgumentException("Request dto cannot be null");
        }
        CarbonEmissions calculation = new CarbonEmissions();
        calculation.setName(Optional.ofNullable(dto.getName())
                .orElseThrow(() -> new IllegalArgumentException("Name cannot be null")));
        calculation.setEmail(Optional.ofNullable(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email cannot be null")));
        calculation.setUf(Optional.ofNullable(dto.getUf())
                .orElseThrow(() -> new IllegalArgumentException("UF cannot be null")));
        calculation.setPhoneNumber(Optional.ofNullable(dto.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Phone number cannot be null")));
        return calculation;
    }
}