package com.example.mob.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mob.entidades.EntidadeCadastradora;

public interface EntidadeCadastradoraRepository extends JpaRepository<EntidadeCadastradora, Long> {
}
