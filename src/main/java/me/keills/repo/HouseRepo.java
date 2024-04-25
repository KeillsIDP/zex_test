package me.keills.repo;

import me.keills.model.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepo extends CrudRepository<House, Long> {
    Optional<House> findByAddress(String address);
}
