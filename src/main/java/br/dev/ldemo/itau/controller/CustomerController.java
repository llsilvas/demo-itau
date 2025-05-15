package br.dev.ldemo.itau.controller;

import br.dev.ldemo.itau.dto.CustomerInputDto;
import br.dev.ldemo.itau.dto.CustomerOutputDto;
import br.dev.ldemo.itau.dto.CustomerWrapper;
import br.dev.ldemo.itau.service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Service")
@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerServiceImpl service;

    public CustomerController(CustomerServiceImpl service) {
        this.service = service;
    }

    @Operation(summary = "Calculates insurance for a given vehicle and location")
    @ApiResponse(responseCode = "200", description = "Insurance calculation successful")
    @ApiResponse(responseCode = "400", description = "Invalid vehicle value or location")
    @PostMapping("/quote")
    public ResponseEntity<CustomerOutputDto> insuranceQuote(@RequestBody @Valid CustomerWrapper customerWrapper){
        log.info("Calculating insurance for customer {}", customerWrapper.getCustomer());
        return ResponseEntity.ok(service.calculateInsurance(customerWrapper));
    }

    @Operation(summary = "Returns all insurance quotes")
    @ApiResponse(responseCode = "200", description = "Insurance quotes retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No insurance quotes found")
    @GetMapping("/quotes")
    public ResponseEntity<List<CustomerOutputDto>> insuranceQuotes(){
        log.info("Retrieving all insurance quotes");
        return ResponseEntity.ok(service.getAllInsurance());
    }

    @Operation(summary = "Returns an insurance quote by id")
    @ApiResponse(responseCode = "200", description = "Insurance quote retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Insurance quote not found")
    @GetMapping("/quote/{id}")
    public ResponseEntity<CustomerOutputDto> getCustomerById(@Parameter @PathVariable final Long id){
        log.info("Retrieving insurance quote with id {}", id);
        return ResponseEntity.ok(service.getCustomerById(id));
    }


    @Operation(summary = "Updates an insurance quote by id")
    @ApiResponse(responseCode = "200", description = "Insurance quote updated successfully")
    @ApiResponse(responseCode = "404", description = "Insurance quote not found")
    @ApiResponse(responseCode = "400", description = "Invalid vehicle value or location")
    @PutMapping("/quote/{id}")
    public ResponseEntity<CustomerOutputDto> update(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid CustomerInputDto dto
    ){
        log.info("Updating insurance quote with id {}", id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/quotes/{id}")
    public void delete(@PathVariable @NotNull Long id){
        log.info("Deleting insurance quote with id {}", id);
        service.delete(id);
        log.info("Insurance quote deleted with id {}", id);
    }
}
