package com.example.mob.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mob.entidades.Ong;
import com.example.mob.repositorios.OngRepository;

@Service
public class OngService {

    @Autowired
    private OngRepository ongRepository;

    public void saveOng(Ong ong) {
        ongRepository.save(ong);
    }

    public Iterable<Ong> getAllOngs() {
        return ongRepository.findAll();
    }

    public Ong findByEmail(String email) {
        return ongRepository.findByEmail(email);
    }
}
