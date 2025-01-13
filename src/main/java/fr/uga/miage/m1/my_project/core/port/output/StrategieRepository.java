package fr.uga.miage.m1.my_project.core.port.output;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;

public interface StrategieRepository {
    Strategie getStrategie(TYPE_STRATEGIE TYPESTRATEGIE);
}
