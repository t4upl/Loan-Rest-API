package com.example.springLoan.service;

import com.example.springLoan.AbstractTest;
import com.example.springLoan.enums.DataTypeEnum;
import com.example.springLoan.enums.SettingName;
import com.example.springLoan.model.ProductTypeSetting;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ProductTypeSettingServiceTest extends AbstractTest {

    ProductTypeSettingService productTypeSettingService;
    List<ProductTypeSetting> productTypeSettings;

    @Before
    public void setUp() {
        this.productTypeSettingService = new ProductTypeSettingServiceImpl(null);
        this.productTypeSettings = new ArrayList<>();
        ProductTypeSetting productTypeSetting = mock(ProductTypeSetting.class, Mockito.RETURNS_DEEP_STUBS);
        when(productTypeSetting.getSetting().getName()).thenReturn(SettingName.MIN_AMOUNT);
        when(productTypeSetting.getSetting().getDataType().getName()).thenReturn(DataTypeEnum.INTEGER);
        when(productTypeSetting.getValue()).thenReturn("-1");
        this.productTypeSettings.add(productTypeSetting);
    }

    @Test
    public void returnProductTypeSettingOnfindAndGetAsInteger(){
        SettingName name = SettingName.AMOUNT;
        ProductTypeSetting productTypeSetting = mock(ProductTypeSetting.class, Mockito.RETURNS_DEEP_STUBS);

        //given
        when(productTypeSetting.getSetting().getName()).thenReturn(name);
        when(productTypeSetting.getSetting().getDataType().getName()).thenReturn(DataTypeEnum.INTEGER);
        when(productTypeSetting.getValue()).thenReturn("999");
        productTypeSettings.add(productTypeSetting);

        //when
        Integer integer = productTypeSettingService.findAndGetAsInteger(productTypeSettings, name.toString());

        //then
        Assert.assertEquals(999, integer.longValue());
    }

    @Test (expected = RuntimeException.class)
    public void throwExceptionWhenBadDataTypeName(){
        SettingName name = SettingName.AMOUNT;
        ProductTypeSetting productTypeSetting = mock(ProductTypeSetting.class, Mockito.RETURNS_DEEP_STUBS);

        //given
        when(productTypeSetting.getSetting().getName()).thenReturn(name);
        when(productTypeSetting.getSetting().getDataType().getName()).thenReturn(DataTypeEnum.DOUBLE);
        when(productTypeSetting.getValue()).thenReturn("999");
        productTypeSettings.add(productTypeSetting);

        //when
        productTypeSettingService.findAndGetAsInteger(productTypeSettings, name.toString());
    }

    @Test (expected = RuntimeException.class)
    public void throwExceptionWhenNoProductTypeSettingWithName(){
        productTypeSettingService.findAndGetAsInteger(productTypeSettings, "no such name in list");
    }
}
