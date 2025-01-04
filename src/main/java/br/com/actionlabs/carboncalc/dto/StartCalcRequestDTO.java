package br.com.actionlabs.carboncalc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartCalcRequestDTO {
  private String email;
  private String name;
  private String uf;
  private String phoneNumber;
}
