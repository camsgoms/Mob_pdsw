package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mob.entidades.Cras;
import com.example.mob.servicos.CrasService;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class CrasController {

    @Autowired
    private CrasService crasService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cras", new Cras());
        model.addAttribute("crasList", crasService.getAllCras());
        return "telainicial"; // Nome do template sem extensão .html
    }

    @PostMapping("/salvar-cras")
    public String salvarCras(@ModelAttribute Cras cras) {
        String senhaGerada = gerarSenhaAutomatica();
        cras.setSenha(senhaGerada);
        crasService.saveCras(cras);
        return "redirect:/cras/sucesso";
    }

    @GetMapping("/cras/sucesso")
    public String sucesso() {
        return "cadastrocras_feito";
    }

    private String gerarSenhaAutomatica() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
