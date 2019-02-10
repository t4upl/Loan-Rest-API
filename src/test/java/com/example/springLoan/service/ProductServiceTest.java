package com.example.springLoan.service;

import com.example.springLoan.AbstractTest;
import com.example.springLoan.ClientDataWrapperFactory;
import com.example.springLoan.model.*;
import com.example.springLoan.other.ClientDataWrapper;
import com.example.springLoan.other.decision_system.DecisionSystem;
import com.example.springLoan.repository.CustomerRepository;
import com.example.springLoan.repository.ProductRepository;
import com.example.springLoan.repository.ProductTypeRepository;
import com.example.springLoan.repository.ProductTypeSettingRepository;
import com.example.springLoan.util.constant.EntityUtil;
import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ProductServiceTest extends AbstractTest {

    private final static Integer CLIENT_DATA_WRAPPER_AMOUNT = 5000;

    ProductServiceImpl productServiceImpl;

    @Mock
    ProductRepository productRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ProductTypeRepository productTypeRepository;

    @Mock
    ProductSettingService productSettingService;

    @Mock
    ProductTypeSettingService productTypeSettingService;

    @Mock
    DecisionSystem decisionSystem;

    @Mock
    ProductTypeSettingRepository productTypeSettingRepository;

    ClientDataWrapper clientDataWrapper;



    @Before
    public void setUp(){
        productServiceImpl = new ProductServiceImpl(productRepository, customerRepository, productTypeRepository,
                productTypeSettingRepository, productSettingService, productTypeSettingService, decisionSystem);
        clientDataWrapper = ClientDataWrapperFactory.getClientDataWrapper(CLIENT_DATA_WRAPPER_AMOUNT, "1986-04-08 12:30", 15);
    }

    @Test
    public void whenClientDataNotValidReturnOptionalEmpty(){
        when(decisionSystem.isLoanGiven(any())).thenReturn(false);
        Optional<Product> optionalProduct = productServiceImpl.getLoan(clientDataWrapper);
        Assert.assertFalse(optionalProduct .isPresent());
    }

    @Test
    public void whenClientDataValidReturnProduct(){
        when(decisionSystem.isLoanGiven(any())).thenReturn(true);
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(productTypeRepository.findById(any())).thenReturn(Optional.of(new ProductType()));
        when(productRepository.save(any())).thenAnswer(
                (Answer<Product>) invocationOnMock -> (Product) invocationOnMock.getArguments()[0]);


        when(productSettingService.saveAll(any())).thenAnswer((Answer<Set<ProductSetting>>) invocationOnMock
                -> (Set<ProductSetting>) invocationOnMock.getArguments()[0]);
        when(productSettingService.getProductSetting(any(), any(), any(), any())).thenAnswer(
                new Answer<ProductSetting>() {
                    @Override
                    public ProductSetting answer(InvocationOnMock invocationOnMock) throws Throwable {
                        ProductSettingService productSettingService = new ProductSettingServiceImpl();
                        return productSettingService.getProductSetting(invocationOnMock.getArgument(0),
                                invocationOnMock.getArgument(1), invocationOnMock.getArgument(2),
                                invocationOnMock.getArgument(3));
                    }
                });


        //create productTypeSettings object - one that should be simply copied into ProductSetting
        // and another one (with name = "amount") that should be modified
        final String PRODUCT_TYPE_SETTING1_VALUE = "1000";
        ProductTypeSetting productTypeSetting1 = new ProductTypeSetting(1, PRODUCT_TYPE_SETTING1_VALUE,
                new ProductType(), new Setting(){{setIsRuntimeInput(false);}});
        ProductTypeSetting productTypeSetting2 = new ProductTypeSetting(2, Strings.EMPTY, new ProductType(),
                new Setting(){{
                    setIsRuntimeInput(true);
                    setName(EntityUtil.Setting.AMOUNT);
        }});
        List<ProductTypeSetting> productTypeSettings = Arrays.asList(productTypeSetting1, productTypeSetting2);
        when(productTypeSettingRepository.findByProductType_Id(any())).thenReturn(productTypeSettings);


        Optional<Product> optionalProduct = productServiceImpl.getLoan(clientDataWrapper);
        Assert.assertTrue(optionalProduct.isPresent());

        Set<ProductSetting> productSettings = optionalProduct.get().getProductSettings();
        Assert.assertTrue(productSettings.stream().anyMatch(x
                -> x.getValue().equals(CLIENT_DATA_WRAPPER_AMOUNT.toString())));
        Assert.assertTrue(productSettings.stream().anyMatch(x
                -> x.getValue().equals(PRODUCT_TYPE_SETTING1_VALUE)));

    }

}
