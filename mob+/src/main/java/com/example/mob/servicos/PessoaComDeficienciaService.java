package com.example.mob.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.Cras;
import com.example.mob.repositorios.PessoaComDeficienciaRepository;

import java.util.List;

@Service
public class PessoaComDeficienciaService {

    @Autowired
    private PessoaComDeficienciaRepository pessoaComDeficienciaRepository;

    // Campo para armazenar a pessoa logada
    private PessoaComDeficiencia pessoaLogada;

    // Salva ou atualiza PessoaComDeficiencia no banco de dados
    public void savePessoaComDeficiencia(PessoaComDeficiencia pessoa) {
        pessoaComDeficienciaRepository.save(pessoa);
    }

    // Busca uma pessoa com deficiência pelo email
    public PessoaComDeficiencia findByEmail(String email) {
        return pessoaComDeficienciaRepository.findByEmail(email.toLowerCase());
    }

    // Busca uma pessoa com deficiência por ID com deficiências carregadas
    public PessoaComDeficiencia findByIdWithDeficiencias(Long id) {
        return pessoaComDeficienciaRepository.findByIdWithDeficiencias(id);
    }

    // Busca uma pessoa com deficiência por ID
    public PessoaComDeficiencia findById(Long id) {
        return pessoaComDeficienciaRepository.findById(id).orElse(null);
    }

    // Exclui uma pessoa com deficiência pelo ID
    public void deleteById(Long id) {
        pessoaComDeficienciaRepository.deleteById(id);
    }

    // Busca pessoas cadastradas por uma ONG específica
    public List<PessoaComDeficiencia> findByOng(Ong ong) {
        return pessoaComDeficienciaRepository.findByOng(ong);
    }

    // Busca pessoas cadastradas por um CRAS específico
    public List<PessoaComDeficiencia> findByCras(Cras cras) {
        return pessoaComDeficienciaRepository.findByCras(cras);
    }

    // Método para definir a pessoa logada
    public void setPessoaLogada(PessoaComDeficiencia pessoa) {
        this.pessoaLogada = pessoa;
    }

    // Método para obter a pessoa logada
    public PessoaComDeficiencia getPessoaLogada() {
        return this.pessoaLogada;
    }

    /**
     * Método para verificar se o email pertence a uma PessoaComDeficiencia.
     * Retorna true se o email pertence, false caso contrário.
     *
     * @param email Email a ser verificado
     * @return true se o email pertence a uma pessoa com deficiência, false caso contrário
     */
    public boolean isPessoaComDeficiencia(String email) {
        return pessoaComDeficienciaRepository.findByEmail(email.toLowerCase()) != null;
    }
}
