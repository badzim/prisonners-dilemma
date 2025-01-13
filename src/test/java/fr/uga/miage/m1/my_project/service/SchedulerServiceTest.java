package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.web.service.PingSchedulerServiceImpl;
import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
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
    private PingSchedulerServiceImpl pingSchedulerServiceImpl;

    @SpyBean
    private SseServiceImpl sseServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartSchedulerWithException() throws InterruptedException {
        // Arrange
        doThrow(new RuntimeException("Simulated Exception"))
                .when(sseServiceImpl).handleDisconnectedPlayers();

        // Act
        pingSchedulerServiceImpl.startScheduler();

        // Simuler un temps d'attente pour permettre au scheduler de s'exécuter
        // Use Awaitility to wait until the mocked method has been called at least once
        await().atMost(10, SECONDS).untilAsserted(() ->
                verify(sseServiceImpl, atLeast(1)).handleDisconnectedPlayers()
        );

        // Assert
                  }

    @Test
    void testStopScheduler() {
        // Arrange
        ScheduledExecutorService pingSchedulerMock = mock(ScheduledExecutorService.class);
        doReturn(false).when(pingSchedulerMock).isShutdown();
        pingSchedulerServiceImpl.setPingScheduler(pingSchedulerMock);

        // Act
        pingSchedulerServiceImpl.stopScheduler();

        // Assert
        verify(pingSchedulerMock, times(1)).shutdown();
    }
}