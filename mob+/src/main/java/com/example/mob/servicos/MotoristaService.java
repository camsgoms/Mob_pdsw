package com.example.mob.servicos;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.repositorios.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository motoristaRepository;

    public void salvarMotorista(Motorista motorista) {
        motoristaRepository.save(motorista);
    }

    public List<Motorista> listarMotoristasCompatíveis(PessoaComDeficiencia pessoa) {
        if (pessoa == null || pessoa.getDeficiencias() == null) {
            System.out.println("Pessoa com deficiência ou suas deficiências estão nulas.");
            return List.of();
        }

        List<Motorista> motoristas = motoristaRepository.findAll();

        return motoristas.stream()
                .filter(Motorista::isDisponivel)
                .filter(motorista -> motorista.getNecessidadesEspeciais().stream()
                        .map(this::normalizeString)
                        .anyMatch(necessidade -> pessoa.getDeficiencias().stream()
                                .map(this::normalizeString)
                                .anyMatch(necessidade::equals)))
                .collect(Collectors.toList());
    }

    private String normalizeString(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[\\s\\-]+", "");
    }

    public List<Motorista> listarMotoristasDisponiveis() {
        return motoristaRepository.findByDisponivelTrue();
    }

    public Motorista buscarMotoristaPorId(Long id) {
        return motoristaRepository.findById(id).orElse(null);
    }

    public Motorista buscarPorEmailESenha(String email, String senha) {
        return motoristaRepository.findByEmailAndSenha(email, senha);
    }

    public Motorista findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email vazio ou nulo fornecido para busca.");
            return null;
        }
        return motoristaRepository.findByEmail(email);
    }

    public Boolean isMotorista(String email) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email vazio ou nulo fornecido para verificação.");
            return false;
        }
        return motoristaRepository.existsByEmail(email);
    }
}
