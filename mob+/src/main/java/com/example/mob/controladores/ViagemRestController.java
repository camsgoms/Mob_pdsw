package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mob.dto.ViagemDTO;
import com.example.mob.entidades.Viagem;
import com.example.mob.servicos.ViagemService;

@RestController
public class ViagemRestController {

    private final ViagemService viagemService;

    @Autowired
    public ViagemRestController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @PostMapping("/salvar-viagem")
    public @ResponseBody String salvarViagem(@RequestBody ViagemDTO viagemDTO) {
        // Chama o serviço para salvar os dados da viagem no banco de dados
        Viagem viagem = viagemService.salvarViagem(viagemDTO);
        return "Viagem salva com sucesso! ID: " + viagem.getId();
    }
}
