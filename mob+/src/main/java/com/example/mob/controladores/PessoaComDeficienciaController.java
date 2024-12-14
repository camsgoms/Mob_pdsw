package com.example.mob.controladores;

import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.PessoaComDeficienciaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PessoaComDeficienciaController {

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    // Página de sucesso para a pessoa com deficiência
    @GetMapping("/perfil-usuario")
    public String perfilUsuario(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            PessoaComDeficiencia pessoaLogada = pessoaComDeficienciaService.findByIdWithDeficiencias(usuarioId);
            if (pessoaLogada != null) {
                model.addAttribute("pessoa", pessoaLogada);
                return "perfil-usuario"; // Redireciona para o template perfil-usuario.html
            }
        }

        model.addAttribute("error", "Usuário não autenticado ou não encontrado.");
        return "redirect:/login"; // Redireciona para login se não autenticado
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
        return "error"; // Nome do template Thymeleaf para exibir a página de erro
    }
}
