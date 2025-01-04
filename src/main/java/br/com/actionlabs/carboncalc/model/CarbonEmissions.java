package br.com.actionlabs.carboncalc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("carbonEmissions")
public class CarbonEmissions {
    @Id
    private String id;
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF must be a valid two-letter state code")
    @NotBlank (message = "UF must not be blank")
    private String uf;
    @NotBlank (message = "Name must not be blank")
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank (message = "Email must not be blank")
    private String email;
    @NotBlank (message = "Phone number must not be blank")
    private String phoneNumber;

    private double energy;
    private double transportation;
    private double solidWasteRecyclable;
    private double solidWasteNonRecyclable;
    private double emissionsTotal;
}
