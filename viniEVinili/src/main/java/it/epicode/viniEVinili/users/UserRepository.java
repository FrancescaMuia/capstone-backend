package it.epicode.viniEVinili.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByUsername(String username);

    public List<UserResponsePrj> findAllBy();

    Optional<User> findOneByUsername(String username);
}
