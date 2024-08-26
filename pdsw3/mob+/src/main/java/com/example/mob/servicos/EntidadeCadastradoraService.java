package com.example.mob.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mob.entidades.EntidadeCadastradora;
import com.example.mob.repositorios.EntidadeCadastradoraRepository;

@Service
public class EntidadeCadastradoraService {

    @Autowired
    private EntidadeCadastradoraRepository entidadeRepository;

    public EntidadeCadastradora buscarPorId(Long id) {
        return entidadeRepository.findById(id).orElse(null);
    }
}

