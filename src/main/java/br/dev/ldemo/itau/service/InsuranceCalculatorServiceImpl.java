package br.dev.ldemo.itau.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InsuranceCalculatorServiceImpl implements InsuranceCalculatorService {

    public double calculate(Double vehicleValue, String location) {
        log.info("Calculating insurance for value {} and location {}", vehicleValue, location);

        if( vehicleValue == null || vehicleValue <= 0.0 || location == null){
            throw new IllegalArgumentException("Invalid vehicle value or location");
        }
        double price = vehicleValue;
        double rate = 0;

        if (price <= 70000 ){
            rate =  "SP".equalsIgnoreCase(location)? 0.05 : 0.04;
        } else if (price <= 100000) {
            rate = 0.055;
        } else {
            rate = 0.06;
        }
        return price * rate;
    }
}
