package br.com.actionlabs.carboncalc;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.model.CarbonEmissions;
import br.com.actionlabs.carboncalc.repository.CarbonEmissionsRepository;
import br.com.actionlabs.carboncalc.repository.TransportationEmissionFactorRepository;
import br.com.actionlabs.carboncalc.service.CalculationService;
import br.com.actionlabs.carboncalc.service.CarbonCalculatorService;
import br.com.actionlabs.carboncalc.service.UFService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarbonCalculatorApplicationTests {

	@Autowired private CarbonEmissionsRepository carbonEmissionsRepository;
	@Autowired private TransportationEmissionFactorRepository transportationEmissionFactorRepository;
	@Autowired private UFService ufService;
	@Autowired private CalculationService calculationService;
	@Autowired private CarbonCalculatorService carbonCalculatorService;

	private final String
		name = "André",
		uf = "PR",
		invalidUf = "ZZ",
		email = "andre@email.com",
		phoneNumber = "41988888888";
	private final double PRFactor = 0.4;

	private CarbonEmissions getCarbonEmissions() {
		CarbonEmissions emission = new CarbonEmissions();
		emission.setUf(uf);
		emission.setName(name);
		emission.setEmail(email);
		emission.setPhoneNumber(phoneNumber);
		emission.setEnergy(100);
		emission.setTransportation(2.64);
		emission.setSolidWasteRecyclable(8.08);
		emission.setSolidWasteNonRecyclable(72.72);
		emission.setEmissionsTotal(123.44);
		return emission;
	}

	@Test
	void testStartCalculationValid() {
		StartCalcRequestDTO request = new StartCalcRequestDTO(email, name, uf, phoneNumber);
		StartCalcResponseDTO response = carbonCalculatorService.startCalculation(request);
		assertNotNull(response.getId());
		assertTrue(carbonEmissionsRepository.findById(response.getId()).isPresent());
	}

	@Test
	void testUpdateCalculationValid() {
		CarbonEmissions emission = getCarbonEmissions();
		emission.setId(carbonEmissionsRepository.save(emission).getId());

		UpdateCalcInfoRequestDTO request = new UpdateCalcInfoRequestDTO();
		request.setId(emission.getId());
		request.setEnergyConsumption(100);
		request.setSolidWasteTotal(101);
		request.setRecyclePercentage(0.2);
		request.setTransportation(Arrays.asList(new TransportationDTO(TransportationType.CAR, 12),
				new TransportationDTO(TransportationType.MOTORCYCLE, 4)));

		UpdateCalcInfoResponseDTO response = carbonCalculatorService.updateCalculation(request);
		assertTrue(response.isSuccess());
		CarbonEmissions updatedEmission = carbonEmissionsRepository.findById(emission.getId()).orElseThrow();
		assertEquals((PRFactor * request.getEnergyConsumption()), updatedEmission.getEnergy());
	}

	@Test
	void testGetCarbonEmissionValid() {
		CarbonEmissions emission = getCarbonEmissions();
		emission.setId(carbonEmissionsRepository.save(emission).getId());
		CarbonCalculationResultDTO result = assertDoesNotThrow(() -> carbonCalculatorService.getCarbonEmission(emission.getId()));
		assertEquals(100, result.getEnergy());
	}

	@Test
	void testCalculateEnergyEmissionValid() {
		int consumption = 100;
		double result = calculationService.calculateEnergyEmission(uf, consumption);
		assertEquals(40, result);
	}

	@Test
	void testCalculateTransportationEmissionValid() {
		List<TransportationDTO> transports = List.of(
				new TransportationDTO(TransportationType.CAR, 12),
				new TransportationDTO(TransportationType.MOTORCYCLE, 4)
		);
		double result = calculationService.calculateTransportationEmission(transports);
		assertEquals(2.64, result);
	}

	@Test
	void testCalculateEnergyEmissionInvalidUF() {
		int consumption = 50;
		assertThrows(NoSuchElementException.class, () -> calculationService.calculateEnergyEmission(invalidUf, consumption));
	}

	@Test
	void testCalculateSolidWasteEmissionInvalidUF() {
		int wasteTotal = 101;
		double recyclePercentage = 0.2;
		assertThrows(NoSuchElementException.class, () -> calculationService.calculateSolidWasteEmission(invalidUf, wasteTotal, recyclePercentage));
	}

	@Test
	void testCalculateTransportationEmissionInvalidTransportation() {
		List<TransportationDTO> transports = List.of(
				new TransportationDTO(null, 5) // Simulating an invalid type
		);
		assertThrows(IllegalArgumentException.class, () -> calculationService.calculateTransportationEmission(transports));
	}

}
