package com.example.mob.repositorios;

import com.example.mob.entidades.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {
    // Busca viagens pelo ID do motorista
    List<Viagem> findByMotoristaId(Long motoristaId);

    // Busca viagens pelo ID da pessoa com deficiência
    List<Viagem> findByPessoaComDeficienciaId(Long pessoaComDeficienciaId);

    // Busca viagens pelo ID do motorista e status da viagem
    List<Viagem> findByMotoristaIdAndStatus(Long motoristaId, String status);
}
