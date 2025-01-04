package br.com.actionlabs.carboncalc.rest;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.exception.CustomExceptionHandler;
import br.com.actionlabs.carboncalc.service.CarbonCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
@Slf4j
public class OpenRestController {

    @Autowired
    CarbonCalculatorService carbonCalculatorService;
    CustomExceptionHandler.ErrorResponse ErrorResponse;

    /**
     * Returns the carbon footprint for the calculation with the given id.
     *
     * @param id
     * @return a {@link ResponseEntity} containing a {@link CarbonCalculationResultDTO}.
     */
    @GetMapping("result/{id}")
    @Operation(summary = "Get Calculation Result", description = "Retrieves the carbon footprint for the calculation with the specified ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calculation result retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Calculation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> getResult(@PathVariable String id) {
        try {
            return ResponseEntity.ok(carbonCalculatorService.getCarbonEmission(id));
        } catch (NoSuchElementException ex) {
            ErrorResponse = new CustomExceptionHandler.ErrorResponse(
                    "Database error: " + ex.getMessage(),
                    HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse);
        }
    }

    /**
     * Starts the calculation process.
     *
     * @param request {@link StartCalcRequestDTO} containing the user's information.
     * @return a {@link ResponseEntity} containing a {@link StartCalcResponseDTO}.
     */
    @PostMapping("start-calc")
    @Operation(summary = "Start Calculation", description = "Receives user info and starts a new calculation process.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calculation started successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> startCalculation(@RequestBody StartCalcRequestDTO request) {
        try {
            return ResponseEntity.ok(carbonCalculatorService.startCalculation(request));
        } catch (DataAccessException | IllegalArgumentException ex) {
            ErrorResponse = new CustomExceptionHandler.ErrorResponse(
                    "Calculation failed: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse);
        }
    }

    /**
     * Updates de calculation process
     *
     * @param request {@link UpdateCalcInfoRequestDTO} containing the carbon footprint's information.
     * @return a {@link ResponseEntity} containing a {@link UpdateCalcInfoResponseDTO}.
     */
    @PutMapping("info")
    public ResponseEntity<Object> updateInfo(@RequestBody UpdateCalcInfoRequestDTO request) {
        try {
            return ResponseEntity.ok(carbonCalculatorService.updateCalculation(request));
        } catch (DataAccessException | NoSuchElementException | HttpMessageNotReadableException | IllegalArgumentException ex) {
            ErrorResponse = new CustomExceptionHandler.ErrorResponse(
                    "Update failed: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse);
        }
    }
}