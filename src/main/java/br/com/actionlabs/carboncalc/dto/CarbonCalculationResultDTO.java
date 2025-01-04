package br.com.actionlabs.carboncalc.dto;

import br.com.actionlabs.carboncalc.model.CarbonEmissions;
import lombok.Data;

@Data
public class CarbonCalculationResultDTO {
  private double energy;
  private double transportation;
  private double solidWaste;
  private double total;

  public static CarbonCalculationResultDTO fromEntity(CarbonEmissions emissions) {
    CarbonCalculationResultDTO dto = new CarbonCalculationResultDTO();
    dto.setEnergy(emissions.getEnergy());
    dto.setTransportation(emissions.getTransportation());
    dto.setSolidWaste(Double.sum(emissions.getSolidWasteRecyclable(), emissions.getSolidWasteNonRecyclable()));
    dto.setTotal(emissions.getEmissionsTotal());
    return dto;
  }
}
