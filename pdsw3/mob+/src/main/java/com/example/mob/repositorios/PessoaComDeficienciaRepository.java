package com.example.mob.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mob.entidades.PessoaComDeficiencia;

public interface PessoaComDeficienciaRepository extends JpaRepository<PessoaComDeficiencia, Long> {
    
    // Adicionando o método para buscar uma PessoaComDeficiencia pelo email
    PessoaComDeficiencia findByEmail(String email);
}
