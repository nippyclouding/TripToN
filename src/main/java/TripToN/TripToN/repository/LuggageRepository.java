package TripToN.TripToN.repository;

import TripToN.TripToN.domain.Luggage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuggageRepository extends JpaRepository<Luggage, Long> {
    Page<Luggage> findAllByOrderByDateTimeDesc(Pageable pageable);
}
