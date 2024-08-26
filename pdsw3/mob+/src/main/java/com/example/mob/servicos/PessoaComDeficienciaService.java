package com.example.mob.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.repositorios.PessoaComDeficienciaRepository;

@Service
public class PessoaComDeficienciaService {

    @Autowired
    private PessoaComDeficienciaRepository pessoaComDeficienciaRepository;

    public PessoaComDeficiencia savePessoaComDeficiencia(PessoaComDeficiencia pessoaComDeficiencia) {
        return pessoaComDeficienciaRepository.save(pessoaComDeficiencia);
    }

    public PessoaComDeficiencia findById(Long id) {
        return pessoaComDeficienciaRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        pessoaComDeficienciaRepository.deleteById(id);
    }

    public PessoaComDeficiencia findByEmail(String email) {
        return pessoaComDeficienciaRepository.findByEmail(email);
    }

    // Adicione outros métodos conforme necessário
}
