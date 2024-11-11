package com.example.mob.repositorios;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.Viagem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    
    // Busca motorista pelo email e senha
    Motorista findByEmailAndSenha(String email, String senha);

    // Retorna motoristas disponíveis
    List<Motorista> findByDisponivelTrue();
    
    public interface ViagemRepository extends JpaRepository<Viagem, Long> {
        List<Viagem> findByMotoristaIdAndStatus(Long motoristaId, String status);
    }

}


