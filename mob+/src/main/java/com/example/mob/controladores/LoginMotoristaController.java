package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.servicos.MotoristaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginMotoristaController {

    @Autowired
    private MotoristaService motoristaService;

    // Processa o login do motorista
    @PostMapping("/motorista/login") // Alterado para evitar conflitos
    public String loginMotorista(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        try {
            Motorista motorista = motoristaService.findByEmail(email);

            if (motorista != null && motorista.getSenha().equals(senha)) {
                session.setAttribute("usuarioId", motorista.getId());
                return "redirect:/motorista/pagina-inicial"; // Página inicial do motorista
            } else {
                model.addAttribute("error", "Email ou senha inválidos!");
                return "login-motorista"; // Página de login com erro
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao processar login: " + e.getMessage());
            return "login-motorista"; // Página de login com erro
        }
    }

    // Página inicial para o motorista
    @GetMapping("/motorista/pagina-inicial")
    public String motoristaDashboard(HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motorista != null) {
                model.addAttribute("motorista", motorista);
                return "pagina-inicial-motorista"; // Nome do template para a página inicial do motorista
            }
        }

        model.addAttribute("error", "Usuário não autenticado.");
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
        return "error";
    }
}
