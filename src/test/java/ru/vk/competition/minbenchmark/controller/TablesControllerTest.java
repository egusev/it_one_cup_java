package ru.vk.competition.minbenchmark.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TablesControllerTest {

    @Test
    public void testTypeConverter() {
        assertEquals("CHARACTER VARYING", TablesController.typeConverter("VARCHAR"));
        assertEquals("CHARACTER VARYING", TablesController.typeConverter("VARCHAR(0)"));
        assertEquals("CHARACTER VARYING", TablesController.typeConverter("VARCHAR(10000)"));
        assertEquals("INTEGER", TablesController.typeConverter("INTEGER"));
    }

}