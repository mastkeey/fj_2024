package ru.mastkey.fj_2024.lesson5.util;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceUtilTest {

    @Test
    void testCreateConvertRequest() {
        Double amount = 100.0;
        String currency = "USD";

        ConvertCurrencyRequest request = EventServiceUtil.createConvertRequest(amount, currency);

        assertNotNull(request);
        assertEquals(amount, request.getAmount());
        assertEquals(currency, request.getFromCurrency());
        assertEquals("RUB", request.getToCurrency());
    }

    @Test
    void testIsEventWithinBudget_Success() {
        KudaGoEventsResponse event = new KudaGoEventsResponse();
        event.setPrice("500");

        Double convertedBudget = 600.0;

        boolean result = EventServiceUtil.isEventWithinBudget(event, convertedBudget);

        assertTrue(result);
    }

    @Test
    void testIsEventWithinBudget_Failure() {
        KudaGoEventsResponse event = new KudaGoEventsResponse();
        event.setPrice("700");

        Double convertedBudget = 600.0;

        boolean result = EventServiceUtil.isEventWithinBudget(event, convertedBudget);

        assertFalse(result);
    }

    @Test
    void testIsEventWithinBudget_NullPrice() {
        KudaGoEventsResponse event = new KudaGoEventsResponse();
        event.setPrice(null);

        Double convertedBudget = 600.0;

        boolean result = EventServiceUtil.isEventWithinBudget(event, convertedBudget);

        assertFalse(result);
    }

    @Test
    void testGetPriceFromString_ValidPrice() {
        String price = "1000";

        Double result = EventServiceUtil.getPriceFromString(price);

        assertNotNull(result);
        assertEquals(1000.0, result);
    }

    @Test
    void testGetPriceFromString_EmptyPrice() {
        String price = "";

        Double result = EventServiceUtil.getPriceFromString(price);

        assertNull(result);
    }

    @Test
    void testGetPriceFromString_NullPrice() {
        String price = null;

        Double result = EventServiceUtil.getPriceFromString(price);

        assertNull(result);
    }

    @Test
    void testGetPriceFromString_InvalidPrice() {
        String price = "Invalid Price";

        Double result = EventServiceUtil.getPriceFromString(price);

        assertNull(result);
    }
}