package fr.uga.miage.m1.my_project.core.port.output;

public interface TaskScheduler {
    // Démarrer le planificateur
    void startScheduler();

    // Arrêter le planificateur
    void stopScheduler();
}