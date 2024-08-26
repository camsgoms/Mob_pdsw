package com.example.mob.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mob.entidades.Ong;

public interface OngRepository extends JpaRepository<Ong, Long> {
    Ong findByEmail(String email);
}
