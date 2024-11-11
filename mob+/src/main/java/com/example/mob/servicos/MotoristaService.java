package com.example.mob.servicos;

import com.example.mob.entidades.Motorista;
import com.example.mob.repositorios.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository motoristaRepository;

    // Método para salvar o motorista no banco de dados
    public void salvarMotorista(Motorista motorista) {
        motoristaRepository.save(motorista);
    }

    // Método para listar motoristas disponíveis (onde o campo disponivel é true)
    public List<Motorista> listarMotoristasDisponiveis() {
        return motoristaRepository.findByDisponivelTrue();
    }

    // Método para buscar motorista por ID
    public Motorista buscarMotoristaPorId(Long id) {
        return motoristaRepository.findById(id).orElse(null);
    }

    // Método para buscar motorista por email e senha
    public Motorista buscarPorEmailESenha(String email, String senha) {
        return motoristaRepository.findByEmailAndSenha(email, senha);
    }
}
