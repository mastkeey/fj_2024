package ru.mastkey.fj_2024.lesson5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.mastkey.fj_2024.lesson5.command.InitCommand;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InitializationServiceTest {

    @Mock
    private ScheduledExecutorService scheduledThreadPool;

    @Mock
    private Future<Void> commandFuture;

    @Spy
    private List<InitCommand> initCommands = new ArrayList<>();

    @Mock
    private InitCommand initCommand1;

    @Mock
    private InitCommand initCommand2;

    @InjectMocks
    private InitializationService initializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initCommands.add(initCommand1);
        initCommands.add(initCommand2);
    }

    @Test
    void testInitAllData_Success() throws Exception {
        when(initCommand1.execute()).thenReturn(commandFuture);
        when(initCommand2.execute()).thenReturn(commandFuture);

        when(commandFuture.get()).thenReturn(null);

        assertDoesNotThrow(() -> initializationService.initAllData());

        verify(initCommand1).execute();
        verify(initCommand2).execute();
        verify(commandFuture, times(2)).get();
    }

    @Test
    void testInitAllData_ThrowsExceptionOnError() throws Exception {
        when(initCommand1.execute()).thenReturn(commandFuture);
        when(initCommand2.execute()).thenReturn(commandFuture);

        when(commandFuture.get()).thenThrow(new ExecutionException(new RuntimeException("Command execution failed")));

        ServiceException exception = assertThrows(ServiceException.class, () -> initializationService.initAllData());
        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
        assertEquals("Error during data initialization", exception.getMessage());

        verify(initCommand1).execute();
        verify(initCommand2).execute();
        verify(commandFuture).get();
    }

    @Test
    void testInitAllData_Interrupted() throws Exception {
        when(initCommand1.execute()).thenReturn(commandFuture);
        when(initCommand2.execute()).thenReturn(commandFuture);

        when(commandFuture.get()).thenThrow(new InterruptedException("Execution interrupted"));

        ServiceException exception = assertThrows(ServiceException.class, () -> initializationService.initAllData());
        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
        assertEquals("Error during data initialization", exception.getMessage());

        verify(initCommand1).execute();
        verify(initCommand2).execute();
        verify(commandFuture, times(1)).get();
    }
}