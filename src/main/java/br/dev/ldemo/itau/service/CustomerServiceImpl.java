package br.dev.ldemo.itau.service;

import br.dev.ldemo.itau.dto.CustomerInputDto;
import br.dev.ldemo.itau.dto.CustomerOutputDto;
import br.dev.ldemo.itau.dto.CustomerWrapper;
import br.dev.ldemo.itau.entity.Customer;
import br.dev.ldemo.itau.exception.ResourceNotFoundException;
import br.dev.ldemo.itau.repository.CustomerRespository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for business logic related to customers.
 */

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRespository repository;
    private final InsuranceCalculatorServiceImpl insuranceCalculatorServiceImpl;

    public CustomerServiceImpl(CustomerRespository repository, InsuranceCalculatorServiceImpl insuranceCalculatorServiceImpl) {
        this.repository = repository;
        this.insuranceCalculatorServiceImpl = insuranceCalculatorServiceImpl;
    }


    public CustomerOutputDto calculateInsurance(CustomerWrapper customerWrapper) {
        CustomerInputDto customerInputDto = customerWrapper.getCustomer();
        double valueInsurance = insuranceCalculatorServiceImpl.calculate(customerInputDto.vehicle_value(), customerInputDto.location());
        save(customerInputDto);
        return new CustomerOutputDto(null, customerInputDto.name(), customerInputDto.location(), valueInsurance);
    }

    private void save(CustomerInputDto customerInputDto) {
        Customer customer = customerInputDto.toEntity();
        repository.save(customer);
        log.info("Customer saved with name {}", customer.getName());
    }

    public List<CustomerOutputDto> getAllInsurance() {
        return repository.findAll().stream()
                .map(customer -> new CustomerOutputDto(customer.getId(), customer.getName(), customer.getLocation(), customer.getVehicle_value()))
                .toList();
    }

    public CustomerOutputDto getCustomerById(Long id) {
        return repository.findById(id)
                .map(customer -> new CustomerOutputDto(customer.getId(), customer.getName(), customer.getLocation(), customer.getVehicle_value()))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public CustomerOutputDto update(@NotNull Long id, CustomerInputDto dto) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setName(dto.name());
        customer.setDocument(dto.document());
        customer.setBirthDate(dto.birthDate());
        customer.setLocation(dto.location());
        customer.setVehicle_value(dto.vehicle_value());

        customer = repository.save(customer);

        return new CustomerOutputDto(customer.getId(), customer.getName(), customer.getLocation(), customer.getVehicle_value());

    }

    public void delete(Long id) {
        repository.findById(id).map(
                customer -> {
                    repository.delete(customer);
                    return customer;
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }
}
