package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mob.entidades.ConectaRecife;
import com.example.mob.servicos.ConectaRecifeService;

@Controller
public class ConectaRecifeController {

    @Autowired
    private ConectaRecifeService conectaRecifeService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("conecta", new ConectaRecife());
        return "loginConecta";
    }

    @PostMapping("/login-conecta")
    public String loginConecta(@ModelAttribute ConectaRecife conectaRecife) {
        boolean isValidUser = conectaRecifeService.validateUser(conectaRecife.getEmail(), conectaRecife.getSenha());
        if (isValidUser) {
            return "redirect:/telainicial"; // Altere aqui para a página correta
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/telainicial")
    public String telaInicial() {
        return "telainicial";
    }
}
