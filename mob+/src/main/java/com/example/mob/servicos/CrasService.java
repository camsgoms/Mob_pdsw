package com.example.mob.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mob.entidades.Cras;
import com.example.mob.repositorios.CrasRepository;

@Service
public class CrasService {

    @Autowired
    private CrasRepository crasRepository;

    // Campo para armazenar o CRAS logado
    private Cras crasLogado;

    public Cras saveCras(Cras cras) {
        return crasRepository.save(cras);
    }

    public List<Cras> getAllCras() {
        return crasRepository.findAll();
    }

    public Cras findByEmail(String email) {
        return crasRepository.findByEmail(email);
    }

    // Método para definir o CRAS logado
    public void setCrasLogado(Cras cras) {
        this.crasLogado = cras;
    }

    // Método para obter o CRAS logado
    public Cras getCrasLogado() {
        return this.crasLogado;
    }
}
