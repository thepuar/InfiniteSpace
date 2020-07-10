package es.thepuar.InfiniteSpace.dao;

import es.thepuar.InfiniteSpace.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesDAO  extends JpaRepository<Serie, Long> {
}
