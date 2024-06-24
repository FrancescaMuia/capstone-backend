package it.epicode.viniEVinili.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByUsername(String username);

    public List<UserResponsePrj> findAllBy();
}
