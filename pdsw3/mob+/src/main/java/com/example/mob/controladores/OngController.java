package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mob.entidades.*;
import com.example.mob.servicos.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class OngController {

    @Autowired
    private OngService ongService;

    @GetMapping("/ong")
    public String index(Model model) {
        model.addAttribute("ong", new Ong());
        model.addAttribute("ongs", ongService.getAllOngs());
        return "telainicial";
    }

    @PostMapping("/salvar-ong")
    public String salvarOng(@ModelAttribute Ong ong) {
        String senhaGerada = gerarSenhaAutomatica();
        ong.setSenha(senhaGerada);
        ongService.saveOng(ong);
        return "redirect:/ong/sucesso";
    }

    @GetMapping("/ong/sucesso")
    public String sucesso() {
        return "cadastroong_feito";
    }

    private String gerarSenhaAutomatica() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
