package br.dev.ldemo.itau.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InsuranceCalculatorServiceImplTest {

    private InsuranceCalculatorServiceImpl insuranceCalculatorServiceImpl;

    @BeforeEach
    void setUp() {
        insuranceCalculatorServiceImpl = new InsuranceCalculatorServiceImpl();
    }

    @Test
    void calculate_WithNullVehicleValue_ShouldThrowException() {
        // Arrange
        Double vehicleValue = null;
        String location = "SP";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            insuranceCalculatorServiceImpl.calculate(vehicleValue, location)
        );
        assertEquals("Invalid vehicle value or location", exception.getMessage());
    }

    @Test
    void calculate_WithNegativeVehicleValue_ShouldThrowException() {
        // Arrange
        Double vehicleValue = -1000.0;
        String location = "SP";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            insuranceCalculatorServiceImpl.calculate(vehicleValue, location)
        );
        assertEquals("Invalid vehicle value or location", exception.getMessage());
    }

    @Test
    void calculate_WithZeroVehicleValue_ShouldThrowException() {
        // Arrange
        Double vehicleValue = 0.0;
        String location = "SP";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            insuranceCalculatorServiceImpl.calculate(vehicleValue, location)
        );
        assertEquals("Invalid vehicle value or location", exception.getMessage());
    }

    @Test
    void calculate_WithNullLocation_ShouldThrowException() {
        // Arrange
        Double vehicleValue = 50000.0;
        String location = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            insuranceCalculatorServiceImpl.calculate(vehicleValue, location)
        );
        assertEquals("Invalid vehicle value or location", exception.getMessage());
    }

    @Test
    void calculate_WithLowValueVehicleInSP_ShouldReturn5Percent() {
        // Arrange
        Double vehicleValue = 50000.0;
        String location = "SP";
        double expected = 2500.0;

        // Act
        double result = insuranceCalculatorServiceImpl.calculate(vehicleValue, location);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void calculate_WithLowValueVehicleInRJ_ShouldReturn4Percent() {
        // Arrange
        Double vehicleValue = 50000.0;
        String location = "RJ";
        double expected = 2000.0;

        // Act
        double result = insuranceCalculatorServiceImpl.calculate(vehicleValue, location);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void calculate_WithMediumValueVehicle_ShouldReturn5Point5Percent() {
        // Arrange
        Double vehicleValue = 80000.0;
        String location = "SP";
        double expected = 4400.0;

        // Act
        double result = insuranceCalculatorServiceImpl.calculate(vehicleValue, location);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void calculate_WithHighValueVehicle_ShouldReturn6Percent() {
        // Arrange
        Double vehicleValue = 150000.0;
        String location = "SP";
        double expected = 9000.0;

        // Act
        double result = insuranceCalculatorServiceImpl.calculate(vehicleValue, location);

        // Assert
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provideVehicleValuesAndLocations")
    void calculate_WithVariousInputs_ShouldReturnCorrectValues(
            Double vehicleValue, String location, double expectedRate) {
        // Arrange
        double expected = vehicleValue * expectedRate;

        // Act
        double result = insuranceCalculatorServiceImpl.calculate(vehicleValue, location);

        // Assert
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideVehicleValuesAndLocations() {
        return Stream.of(
            Arguments.of(10000.0, "SP", 0.05),
            Arguments.of(10000.0, "RJ", 0.04),
            Arguments.of(70000.0, "SP", 0.05),
            Arguments.of(70000.1, "RJ", 0.055),
            Arguments.of(100000.0, "SP", 0.055),
            Arguments.of(100000.1, "RJ", 0.06),
            Arguments.of(200000.0, "SP", 0.06)
        );
    }
}
