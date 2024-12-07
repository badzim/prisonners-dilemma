package fr.uga.miage.m1.my_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
class SchedulerServiceTest {

    @SpyBean
    private PingSchedulerService pingSchedulerService;

    @SpyBean
    private SseService sseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartSchedulerWithException() throws InterruptedException {
        // Arrange
        doThrow(new RuntimeException("Simulated Exception"))
                .when(sseService).handleDisconnectedPlayers();

        // Act
        pingSchedulerService.startScheduler();

        // Simuler un temps d'attente pour permettre au scheduler de s'exécuter
        // Use Awaitility to wait until the mocked method has been called at least once
        await().atMost(10, SECONDS).untilAsserted(() ->
                verify(sseService, atLeast(1)).handleDisconnectedPlayers()
        );

        // Assert
                  }

    @Test
    void testStopScheduler() {
        // Arrange
        ScheduledExecutorService pingSchedulerMock = mock(ScheduledExecutorService.class);
        doReturn(false).when(pingSchedulerMock).isShutdown();
        pingSchedulerService.setPingScheduler(pingSchedulerMock);

        // Act
        pingSchedulerService.stopScheduler();

        // Assert
        verify(pingSchedulerMock, times(1)).shutdown();
    }
}