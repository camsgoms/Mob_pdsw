package com.example.mob.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mob.entidades.ConectaRecife;

public interface ConectaRecifeRepository extends JpaRepository<ConectaRecife, Long> {
    ConectaRecife findByEmail(String email);
}
