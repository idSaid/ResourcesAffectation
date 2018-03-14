package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Parametre;
import org.springframework.stereotype.Service;

@Service
public interface ParameterDao {

    void updateParametre(Parametre parametre);

}
