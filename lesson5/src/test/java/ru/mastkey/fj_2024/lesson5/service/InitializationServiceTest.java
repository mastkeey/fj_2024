package ru.mastkey.fj_2024.lesson5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InitializationServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private LocationService locationService;

    @Mock
    private Future<Void> categoryFuture;

    @Mock
    private Future<Void> locationFuture;

    @InjectMocks
    private InitializationService initializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitAllData_Success() throws Exception {
        when(categoryService.init()).thenReturn(categoryFuture);
        when(locationService.init()).thenReturn(locationFuture);

        when(categoryFuture.get()).thenReturn(null);
        when(locationFuture.get()).thenReturn(null);

        assertDoesNotThrow(() -> initializationService.initAllData());

        verify(categoryService).init();
        verify(locationService).init();
        verify(categoryFuture).get();
        verify(locationFuture).get();
    }

    @Test
    void testInitAllData_ThrowsExceptionOnError() throws Exception {
        when(categoryService.init()).thenReturn(categoryFuture);
        when(locationService.init()).thenReturn(locationFuture);

        when(categoryFuture.get()).thenReturn(null);
        when(locationFuture.get()).thenThrow(new ExecutionException(new RuntimeException("Location init failed")));

        ServiceException exception = assertThrows(ServiceException.class, () -> initializationService.initAllData());
        assertEquals("Error during data initialization", exception.getMessage());

        verify(categoryService).init();
        verify(locationService).init();
        verify(categoryFuture).get();
        verify(locationFuture).get();
    }
}