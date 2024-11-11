package com.example.mob.controladores;

import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.PessoaComDeficienciaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PessoaComDeficienciaController {

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    // Processa o login da pessoa com deficiência
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        try {
            PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);

            // Verifica se a pessoa existe e se a senha está correta
            if (pessoa != null && pessoa.getSenha().equals(senha)) {
                session.setAttribute("pessoaComDeficienciaLogada", pessoa);  // Armazena o objeto completo da pessoa na sessão
                return "redirect:/login-usuario";  // Redireciona para a página login-usuario após o login
            } else {
                model.addAttribute("error", "Email ou senha inválidos!");
                return "login";  // Retorna para a página de login com mensagem de erro
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ocorreu um erro durante o login. Por favor, tente novamente.");
            return "login";
        }
    }

    // Página de sucesso para a pessoa com deficiência
    @GetMapping("/login-usuario")
    public String successPessoaPage(HttpSession session, Model model) {
        // Recupera a pessoa logada diretamente da sessão
        PessoaComDeficiencia pessoaLogada = (PessoaComDeficiencia) session.getAttribute("pessoaComDeficienciaLogada");

        // Verifica se a pessoa logada existe
        if (pessoaLogada != null) {
            model.addAttribute("pessoa", pessoaLogada);  // Adiciona a pessoa ao modelo
            return "login-usuario";  // Renderiza o template login-usuario
        } else {
            model.addAttribute("error", "Pessoa não encontrada na sessão.");
            return "redirect:/login";  // Redireciona para o login se a pessoa não estiver logada
        }
    }

    // Tratamento global de exceções
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
        return "error";  // Nome do template Thymeleaf para exibir a página de erro
    }
}
