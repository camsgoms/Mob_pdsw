package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.servicos.MotoristaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MotoristaController {

    @Autowired
    private MotoristaService motoristaService;

    // Página para alterar senha do motorista
    @GetMapping("/alterar-senha-motorista")
    public String alterarSenhaMotorista(HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motorista != null) {
                model.addAttribute("motorista", motorista);
                return "alterar-senha-motorista"; // Nome do template HTML
            }
        }

        model.addAttribute("error", "Usuário não autenticado.");
        return "redirect:/login"; // Redireciona para login se não autenticado
    }

    // Processar alteração de senha
    @PostMapping("/alterar-senha-motorista")
    public String salvarNovaSenhaMotorista(@RequestParam String novaSenha, HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motorista != null) {
                motorista.setSenha(novaSenha);
                motoristaService.salvarMotorista(motorista);
                model.addAttribute("message", "Senha alterada com sucesso!");
                return "redirect:/perfil-motorista"; // Redireciona para a página de perfil do motorista
            }
        }

        model.addAttribute("error", "Erro ao alterar a senha.");
        return "alterar-senha-motorista"; // Retorna à página de alteração de senha com mensagem de erro
    }

    // Página para alterar dados do motorista
    @GetMapping("/alterar-dados-motorista")
    public String alterarDadosMotorista(HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motorista != null) {
                model.addAttribute("motorista", motorista);
                return "alterar-dados-motorista"; // Nome do template HTML
            }
        }

        model.addAttribute("error", "Usuário não autenticado.");
        return "redirect:/login"; // Redireciona para login se não autenticado
    }

    // Processar alteração de dados
    @PostMapping("/alterar-dados-motorista")
    public String salvarNovosDadosMotorista(@RequestParam String nome, @RequestParam String telefone, HttpSession session, Model model) {
        Long motoristaId = (Long) session.getAttribute("usuarioId");

        if (motoristaId != null) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
            if (motorista != null) {
                motorista.setNome(nome);
                motorista.setTelefone(telefone);
                motoristaService.salvarMotorista(motorista);
                model.addAttribute("message", "Dados alterados com sucesso!");
                return "redirect:/perfil-motorista"; // Redireciona para a página de perfil do motorista
            }
        }

        model.addAttribute("error", "Erro ao alterar os dados.");
        return "alterar-dados-motorista"; // Retorna à página de alteração de dados com mensagem de erro
    }
}
