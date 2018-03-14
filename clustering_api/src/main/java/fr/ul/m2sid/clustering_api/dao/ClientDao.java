package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Client;
import org.springframework.stereotype.Service;

@Service
public interface ClientDao {

    int getCluster(Client client);

}
