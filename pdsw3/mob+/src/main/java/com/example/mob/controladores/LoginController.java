package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mob.entidades.Ong;
import com.example.mob.servicos.OngService;

@Controller
public class LoginController {

    @Autowired
    private OngService ongService;

    @GetMapping("/login-pessoa")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos!");
        }
        return "login-pessoa"; // Nome do template para a página de login
    }

    @PostMapping("/login-pessoa")
    public String login(@RequestParam String email, @RequestParam String senha, Model model) {
        Ong ong = ongService.findByEmail(email);

        if (ong != null && ong.getSenha().equals(senha)) {
            model.addAttribute("nomeUsuario", ong.getNome());
            model.addAttribute("emailUsuario", ong.getEmail());
            return "pagina-inicial"; // Nome do template para a página inicial
        } else {
            model.addAttribute("error", "Email ou senha inválidos!");
            return "login-pessoa";
        }
    }
}
