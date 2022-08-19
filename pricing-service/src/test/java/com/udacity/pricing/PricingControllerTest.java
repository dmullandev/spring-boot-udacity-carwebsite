package com.udacity.pricing;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.udacity.pricing.api.PricingController;

@RunWith(SpringRunner.class)
@WebMvcTest(PricingController.class)
@AutoConfigureMockMvc
public class PricingControllerTest {
    private static final String PRICING_CONTROLLER_GET_URI_WITH_VALID_VEHICLE_ID = "/services/price?vehicleId=1";
    private static final String PRICING_CONTROLLER_GET_URI_WITH_INVALID_VEHICLE_ID = "/services/price?vehicleId=21";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPriceWithValidVehicleId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(new URI(PRICING_CONTROLLER_GET_URI_WITH_VALID_VEHICLE_ID)))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPriceWithInvalidVehicleId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(new URI(PRICING_CONTROLLER_GET_URI_WITH_INVALID_VEHICLE_ID)))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
