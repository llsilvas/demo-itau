package br.dev.ldemo.itau.service;

import br.dev.ldemo.itau.dto.CustomerInputDto;
import br.dev.ldemo.itau.dto.CustomerOutputDto;
import br.dev.ldemo.itau.dto.CustomerWrapper;
import br.dev.ldemo.itau.entity.Customer;
import br.dev.ldemo.itau.exception.ResourceNotFoundException;
import br.dev.ldemo.itau.repository.CustomerRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRespository customerRespository;

    @Mock
    private InsuranceCalculatorServiceImpl insuranceCalculatorServiceImpl;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    private CustomerInputDto customerInputDto;
    private CustomerWrapper customerWrapper;

    @BeforeEach
    void setUp() {
        // Setup test data
        customerInputDto = new CustomerInputDto(
                "Jonatan",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                "SP",
                50000.0
        );

        customerWrapper = new CustomerWrapper();
        customerWrapper.setCustomer(customerInputDto);
    }

    @Test
    void calculateInsurance_ShouldReturnCorrectOutputDto() {
        // Arrange
        double expectedInsuranceValue = 2500.0;
        when(insuranceCalculatorServiceImpl.calculate(customerInputDto.vehicle_value(), customerInputDto.location()))
                .thenReturn(expectedInsuranceValue);

        // Act
        CustomerOutputDto result = customerServiceImpl.calculateInsurance(customerWrapper);

        // Assert
        assertNotNull(result);
        assertEquals(customerInputDto.name(), result.name());
        assertEquals(customerInputDto.location(), result.location());
        assertEquals(expectedInsuranceValue, result.vehicle_value());

        verify(customerRespository, times(1)).save(any(Customer.class));
    }

    @Test
    void calculateInsurance_WithDifferentLocation_ShouldCalculateCorrectly() {
        // Arrange
        CustomerInputDto rjCustomer = new CustomerInputDto(
                "Joao",
                "98765432100",
                LocalDate.of(1995, 5, 5),
                "RJ",
                60000.0
        );

        CustomerWrapper wrapper = new CustomerWrapper();
        wrapper.setCustomer(rjCustomer);

        double expectedInsuranceValue = 2400.0;
        when(insuranceCalculatorServiceImpl.calculate(rjCustomer.vehicle_value(), rjCustomer.location()))
                .thenReturn(expectedInsuranceValue);

        // Act
        CustomerOutputDto result = customerServiceImpl.calculateInsurance(wrapper);

        // Assert
        assertNotNull(result);
        assertEquals(rjCustomer.name(), result.name());
        assertEquals(rjCustomer.location(), result.location());
        assertEquals(expectedInsuranceValue, result.vehicle_value());

        verify(customerRespository, times(1)).save(any(Customer.class));
    }

    @Test
    void calculateInsurance_WithHighVehicleValue_ShouldCalculateCorrectly() {
        // Arrange
        CustomerInputDto expensiveVehicle = new CustomerInputDto(
                "Ricardo",
                "11122233344",
                LocalDate.of(1985, 10, 10),
                "SP",
                150000.0
        );

        CustomerWrapper wrapper = new CustomerWrapper();
        wrapper.setCustomer(expensiveVehicle);

        double expectedInsuranceValue = 9000.0;
        when(insuranceCalculatorServiceImpl.calculate(expensiveVehicle.vehicle_value(), expensiveVehicle.location()))
                .thenReturn(expectedInsuranceValue);

        // Act
        CustomerOutputDto result = customerServiceImpl.calculateInsurance(wrapper);

        // Assert
        assertNotNull(result);
        assertEquals(expensiveVehicle.name(), result.name());
        assertEquals(expensiveVehicle.location(), result.location());
        assertEquals(expectedInsuranceValue, result.vehicle_value());

        verify(customerRespository, times(1)).save(any(Customer.class));
    }

    @Test
    void calculateInsurance_WithZeroVehicleValue_Error(){
        // Arrange
        CustomerInputDto zeroVehicle = new CustomerInputDto(
                "Leandro",
                "11122233344",
                LocalDate.of(1983, 11, 27),
                "SP",
                0.0
        );

        CustomerWrapper wrapper = new CustomerWrapper();
        wrapper.setCustomer(zeroVehicle);

        when(insuranceCalculatorServiceImpl.calculate(zeroVehicle.vehicle_value(), zeroVehicle.location()))
                .thenThrow(new IllegalArgumentException("Invalid vehicle value or location"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerServiceImpl.calculateInsurance(wrapper));
    }

    @Test
    void getAllInsurance_ShouldReturnListOfCustomerOutputDto() {
        // Arrange
        List<Customer> customers = Arrays.asList(
            Customer.builder()
                .id(1L)
                .name("Jonatan")
                .document("12345678900")
                .birthDate(LocalDate.of(1990, 1, 1))
                .location("SP")
                .vehicle_value(50000.0)
                .build(),
            Customer.builder()
                .id(2L)
                .name("Maria")
                .document("98765432100")
                .birthDate(LocalDate.of(1985, 5, 15))
                .location("RJ")
                .vehicle_value(60000.0)
                .build()
        );

        when(customerRespository.findAll()).thenReturn(customers);

        // Act
        List<CustomerOutputDto> result = customerServiceImpl.getAllInsurance();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first customer
        assertEquals(1L, result.get(0).id());
        assertEquals("Jonatan", result.get(0).name());
        assertEquals("SP", result.get(0).location());
        assertEquals(50000.0, result.get(0).vehicle_value());

        // Verify second customer
        assertEquals(2L, result.get(1).id());
        assertEquals("Maria", result.get(1).name());
        assertEquals("RJ", result.get(1).location());
        assertEquals(60000.0, result.get(1).vehicle_value());

        verify(customerRespository, times(1)).findAll();
    }

    @Test
    void getCustomerById_WithValidId_ShouldReturnCustomerOutputDto() {
        // Arrange
        Long customerId = 1L;
        Customer customer = Customer.builder()
            .id(customerId)
            .name("Jonatan")
            .document("12345678900")
            .birthDate(LocalDate.of(1990, 1, 1))
            .location("SP")
            .vehicle_value(50000.0)
            .build();

        when(customerRespository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        CustomerOutputDto result = customerServiceImpl.getCustomerById(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("Jonatan", result.name());
        assertEquals("SP", result.location());
        assertEquals(50000.0, result.vehicle_value());

        verify(customerRespository, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        when(customerRespository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> customerServiceImpl.getCustomerById(invalidId)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRespository, times(1)).findById(invalidId);
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnCustomerOutputDto() {
        // Arrange
        Long customerId = 1L;
        Customer existingCustomer = Customer.builder()
            .id(customerId)
            .name("Jonatan")
            .document("12345678900")
            .birthDate(LocalDate.of(1990, 1, 1))
            .location("SP")
            .vehicle_value(50000.0)
            .build();

        CustomerInputDto updatedData = new CustomerInputDto(
            "Jonatan Silva",
            "12345678900",
            LocalDate.of(1990, 1, 1),
            "RJ",
            60000.0
        );

        Customer updatedCustomer = Customer.builder()
            .id(customerId)
            .name(updatedData.name())
            .document(updatedData.document())
            .birthDate(updatedData.birthDate())
            .location(updatedData.location())
            .vehicle_value(updatedData.vehicle_value())
            .build();

        when(customerRespository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRespository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        CustomerOutputDto result = customerServiceImpl.update(customerId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals(updatedData.name(), result.name());
        assertEquals(updatedData.location(), result.location());
        assertEquals(updatedData.vehicle_value(), result.vehicle_value());

        verify(customerRespository, times(1)).findById(customerId);
        verify(customerRespository, times(1)).save(any(Customer.class));
    }

    @Test
    void update_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        CustomerInputDto updatedData = new CustomerInputDto(
            "Jonatan Silva",
            "12345678900",
            LocalDate.of(1990, 1, 1),
            "RJ",
            60000.0
        );

        when(customerRespository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> customerServiceImpl.update(invalidId, updatedData)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRespository, times(1)).findById(invalidId);
        verify(customerRespository, never()).save(any(Customer.class));
    }

    @Test
    void delete_WithValidId_ShouldDeleteCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer existingCustomer = Customer.builder()
            .id(customerId)
            .name("Jonatan")
            .document("12345678900")
            .birthDate(LocalDate.of(1990, 1, 1))
            .location("SP")
            .vehicle_value(50000.0)
            .build();

        when(customerRespository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act
        customerServiceImpl.delete(customerId);

        // Assert
        verify(customerRespository, times(1)).findById(customerId);
        verify(customerRespository, times(1)).delete(existingCustomer);
    }

    @Test
    void delete_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        when(customerRespository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> customerServiceImpl.delete(invalidId)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRespository, times(1)).findById(invalidId);
        verify(customerRespository, never()).delete(any(Customer.class));
    }
}
