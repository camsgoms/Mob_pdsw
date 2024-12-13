package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.PessoaComDeficienciaService;
import com.example.mob.servicos.OngService;
import com.example.mob.entidades.Ong;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    @Autowired
    private OngService ongService;

    @GetMapping("/login-usuario")
    public String loginUsuario(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            PessoaComDeficiencia pessoaLogada = pessoaComDeficienciaService.findByIdWithDeficiencias(usuarioId);
            if (pessoaLogada != null) {
                model.addAttribute("pessoa", pessoaLogada);
                return "login-usuario";
            }
        }

        model.addAttribute("error", "Usuário não autenticado ou não encontrado.");
        return "redirect:/login";
    }

    @GetMapping("/login-motorista")
    public String loginMotoristaPage(HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motoristaLogado = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motoristaLogado != null) {
                model.addAttribute("motorista", motoristaLogado);
                return "login-motorista";
            }
        }

        model.addAttribute("error", "Usuário não autenticado ou não encontrado.");
        return "redirect:/login";
    }

    @PostMapping("/login-pessoa")
    public String loginOng(@RequestParam String email, @RequestParam String senha, Model model) {
        Ong ong = ongService.findByEmail(email);

        if (ong != null && ong.getSenha().equals(senha)) {
            model.addAttribute("nomeUsuario", ong.getNome());
            model.addAttribute("emailUsuario", ong.getEmail());
            return "pagina-inicial";
        } else {
            model.addAttribute("error", "Email ou senha inválidos!");
            return "login-pessoa";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        Motorista motorista = motoristaService.findByEmail(email);
        if (motorista != null && motorista.getSenha().equals(senha)) {
            session.setAttribute("usuarioId", motorista.getId());
            session.setAttribute("motoristaLogado", motorista); // Armazena o objeto do motorista
            session.setAttribute("usuarioTipo", "motorista");
            return "redirect:/login-motorista"; // Corrigido para login-motorista
        }

        try {
            PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);
            if (pessoa != null && pessoa.getSenha().equals(senha)) {
                session.setAttribute("usuarioId", pessoa.getId());
                session.setAttribute("usuarioNome", pessoa.getNome());
                session.setAttribute("usuarioTipo", "pessoa");
                return "redirect:/login-usuario";
            }

            model.addAttribute("error", "Email ou senha inválidos!");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Erro durante o login: " + e.getMessage());
            return "login";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
        return "error";
    }
}
