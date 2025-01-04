package br.com.actionlabs.carboncalc.dto;

import lombok.Data;

@Data
public class SolidWasteEmissionsDTO {
    private final double recyclableEmission;
    private final double nonRecyclableEmission;

    public SolidWasteEmissionsDTO(double recyclableEmission, double nonRecyclableEmission) {
        this.recyclableEmission = recyclableEmission;
        this.nonRecyclableEmission = nonRecyclableEmission;
    }

    public double getTotalEmission() {
        return recyclableEmission + nonRecyclableEmission;
    }
}