package es.thepuar.InfiniteSpace.dao;

import es.thepuar.InfiniteSpace.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodioDAO extends JpaRepository<Episodio, Long> {
}
