package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculationRecordTest {

    private CalculationRecord calculationRecord = new CalculationRecord(111, 10.22, 1.90, 12.65, 23.55, "fr");

    @Test
    void getDistance() {
        assertEquals(111, calculationRecord.getDistance());
    }

    @Test
    void getConsumption() {
        assertEquals(10.22, calculationRecord.getConsumption());
    }

    @Test
    void getPrice() {
        assertEquals(1.90, calculationRecord.getPrice());
    }

    @Test
    void getTotalFuel() {
        assertEquals(12.65, calculationRecord.getTotalFuel());
    }

    @Test
    void getTotalCost() {
        assertEquals(23.55, calculationRecord.getTotalCost());
    }

    @Test
    void getLanguage() {
        assertEquals("fr", calculationRecord.getLanguage());
    }
}