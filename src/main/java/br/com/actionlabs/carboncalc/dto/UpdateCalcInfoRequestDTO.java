package br.com.actionlabs.carboncalc.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCalcInfoRequestDTO {
  private String id;
  private int energyConsumption;
  private List<TransportationDTO> transportation;
  private int solidWasteTotal;
  /** from 0 to 1.0 percentage of recyclable solid waste */
  private double recyclePercentage;
}
