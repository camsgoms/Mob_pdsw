package com.example.mob.servicos;

import com.example.mob.dto.ViagemDTO;
import com.example.mob.entidades.Viagem;
import com.example.mob.repositorios.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViagemService {

    private final ViagemRepository viagemRepository;

    @Autowired
    public ViagemService(ViagemRepository viagemRepository) {
        this.viagemRepository = viagemRepository;
    }

    // Método para salvar uma viagem diretamente com a entidade Viagem
    public Viagem salvarViagem(Viagem viagem) {
        return viagemRepository.save(viagem);
    }

    // Método para salvar uma viagem a partir de um ViagemDTO
    public Viagem salvarViagem(ViagemDTO viagemDTO) {
        Viagem viagem = new Viagem(viagemDTO.getPontoPartida(), viagemDTO.getPontoFinal());
        return viagemRepository.save(viagem);
    }

    // Método para buscar viagens de um motorista específico
    public List<Viagem> buscarViagensPorMotorista(Long motoristaId) {
        return viagemRepository.findByMotoristaId(motoristaId);
    }

    // Método para buscar viagens de uma pessoa com deficiência específica
    public List<Viagem> buscarViagensPorPessoaComDeficiencia(Long pessoaComDeficienciaId) {
        return viagemRepository.findByPessoaComDeficienciaId(pessoaComDeficienciaId);
    }

    // Método para buscar uma viagem por ID
    public Viagem buscarViagemPorId(Long id) {
        Optional<Viagem> viagem = viagemRepository.findById(id);
        return viagem.orElse(null);  // Retorna null se a viagem não for encontrada
    }

    // Método para buscar viagens de um motorista com um status específico
    public List<Viagem> buscarViagensPorMotoristaComStatus(Long motoristaId, String status) {
        return viagemRepository.findByMotoristaIdAndStatus(motoristaId, status);
    }
}
