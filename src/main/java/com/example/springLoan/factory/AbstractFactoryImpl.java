package com.example.springLoan.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AbstractFactoryImpl implements AbstractFactory {

    ProductSettingFactory productSettingFactory;
    ProductFactory productFactory;

    @Override
    public ProductSettingFactory getProductSettingFactory() {
        return productSettingFactory;
    }

    @Override
    public ProductFactory getProductFactory() { return productFactory; }
}
