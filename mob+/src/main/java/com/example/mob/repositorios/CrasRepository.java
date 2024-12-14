package com.example.mob.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mob.entidades.Cras;

public interface CrasRepository extends JpaRepository<Cras, Long> {
    Cras findByEmail(String email);
}
