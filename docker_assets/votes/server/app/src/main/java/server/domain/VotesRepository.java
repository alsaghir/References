package server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Integer> {

    Optional<Votes> findTopBy();

}
