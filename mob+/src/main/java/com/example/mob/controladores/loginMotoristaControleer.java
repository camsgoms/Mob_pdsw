package com.example.mob.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.Viagem;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.ViagemService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/motoristas")
public class loginMotoristaControleer {
	
	private final MotoristaService motoristaService;
    private final ViagemService viagemService;

    @Autowired
    public loginMotoristaControleer(MotoristaService motoristaService, ViagemService viagemService) {
        this.motoristaService = motoristaService;
        this.viagemService = viagemService;
    }

    // Mapeamento para exibir a página de login do motorista
    @GetMapping("/login")
    public String mostrarPaginaLogin(Model model, HttpSession session) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado"); // Mantém o motorista logado na sessão
        if (motorista == null) {
            motorista = new Motorista();  // Se não houver motorista logado, cria um novo objeto vazio
        }
        model.addAttribute("motorista", motorista);
        return "login-motorista"; // Nome do arquivo HTML para a página de login
    }

    // Mapeamento para processar o login do motorista
 
    @PostMapping("/login")
    public String loginMotorista(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        Motorista motorista = motoristaService.buscarPorEmailESenha(email, senha);
        if (motorista != null) {
            session.setAttribute("motoristaLogado", motorista);  // Armazena o motorista logado na sessão
            model.addAttribute("motorista", motorista);  // Adiciona o motorista ao modelo
            return "login-motorista";  // Aqui você está redirecionando para a página de login-motorista, após o login
        } else {
            model.addAttribute("motorista", new Motorista()); // Garante que o objeto motorista está presente mesmo com erro
            model.addAttribute("loginError", "Email ou senha incorretos.");
            return "login-motorista";  // Retorna para a página de login com erro
        }
    }


    
    // Mapeamento para atualizar a disponibilidade do motorista, usando o ID no caminho da URL
    @PostMapping("/{id}/atualizar-disponibilidade")
    public String atualizarDisponibilidade(@PathVariable Long id, @RequestParam Integer disponivel, HttpSession session, Model model) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");

        // Se o motorista não estiver na sessão, busca do banco
        if (motorista == null) {
            motorista = motoristaService.buscarMotoristaPorId(id);
            if (motorista == null) {
                model.addAttribute("error", "Motorista não encontrado.");
                return "redirect:/motoristas/login";  // Redireciona para o login se o motorista não for encontrado
            }
        }

        // Atualiza a disponibilidade do motorista
        motorista.setDisponivel(disponivel == 1);
        motoristaService.salvarMotorista(motorista);

        // Atualiza o motorista na sessão
        session.setAttribute("motoristaLogado", motorista);

        model.addAttribute("motorista", motorista);  // Certifica-se de que o motorista está no modelo
        model.addAttribute("message", "Disponibilidade atualizada com sucesso!");

        return "login-motorista"; // Volta para a página de login com os dados atualizados
    }

    // Mapeamento para exibir as viagens feitas para o motorista logado
    @GetMapping("/visualizar-viagens")
    public String visualizarViagens(HttpSession session, Model model) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado"); // Obtém o motorista logado da sessão
        if (motorista != null) {
            // Buscar todas as viagens associadas ao motorista logado
            List<Viagem> viagens = viagemService.buscarViagensPorMotorista(motorista.getId());
            model.addAttribute("viagens", viagens);
            model.addAttribute("motorista", motorista);
            return "visualizar-viagens";  // Retorna para a página de visualização de viagens
        } else {
            return "redirect:/motoristas/login";  // Redireciona para o login se o motorista não estiver logado
        }
    }

}
