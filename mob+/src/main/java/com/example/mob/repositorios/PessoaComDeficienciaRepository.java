package com.example.mob.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.Cras;

import java.util.List;

public interface PessoaComDeficienciaRepository extends JpaRepository<PessoaComDeficiencia, Long> {

    PessoaComDeficiencia findByEmail(String email);

    List<PessoaComDeficiencia> findByOng(Ong ong);

    List<PessoaComDeficiencia> findByCras(Cras cras);
}
