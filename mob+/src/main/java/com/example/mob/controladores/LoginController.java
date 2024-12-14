package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.Cras;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.PessoaComDeficienciaService;
import com.example.mob.servicos.OngService;
import com.example.mob.servicos.CrasService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    @Autowired
    private OngService ongService;

    @Autowired
    private CrasService crasService;

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

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        // Verifica Motorista
        Motorista motorista = motoristaService.findByEmail(email);
        if (motorista != null && motorista.getSenha().equals(senha)) {
            session.setAttribute("usuarioId", motorista.getId());
            session.setAttribute("motoristaLogado", motorista);
            session.setAttribute("usuarioTipo", "motorista");
            return "redirect:/login-motorista";
        }

        // Verifica Pessoa com Deficiência
        PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);
        if (pessoa != null && pessoa.getSenha().equals(senha)) {
            session.setAttribute("usuarioId", pessoa.getId());
            session.setAttribute("usuarioNome", pessoa.getNome());
            session.setAttribute("usuarioTipo", "pessoa");
            return "redirect:/login-usuario";
        }

        // Verifica ONG
        Ong ong = ongService.findByEmail(email);
        if (ong != null && ong.getSenha().equals(senha)) {
            session.setAttribute("emailLogado", email);
            session.setAttribute("usuarioTipo", "ong");
            return "redirect:/success";
        }

        // Verifica CRAS
        Cras cras = crasService.findByEmail(email);
        if (cras != null && cras.getSenha().equals(senha)) {
            session.setAttribute("emailLogado", email);
            session.setAttribute("usuarioTipo", "cras");
            return "redirect:/success-cras";
        }

        model.addAttribute("error", "Email ou senha inválidos!");
        return "login";
    }

    @GetMapping("/success")
    public String successOngPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Ong ong = ongService.findByEmail(email);

        if (ong != null) {
            model.addAttribute("ong", ong);
            return "success";
        }

        model.addAttribute("error", "ONG não encontrada.");
        return "error";
    }

    @GetMapping("/success-cras")
    public String successCrasPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(email);

        if (cras != null) {
            model.addAttribute("cras", cras);
            return "success-cras";
        }

        model.addAttribute("error", "CRAS não encontrado.");
        return "error";
    }

    @GetMapping("/visualizar-pessoas-cadastradas-cras")
    public String visualizarPessoasCadastradasCras(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(email);

        if (cras != null) {
            model.addAttribute("pessoas", pessoaComDeficienciaService.findByCras(cras));
            return "visualizar-pessoas-cadastradas-cras";
        }

        model.addAttribute("error", "Nenhum CRAS autenticado.");
        return "error";
    }

    @GetMapping("/cadastrar-pessoa-deficiencia-cras")
    public String cadastrarPessoaComDeficienciaCrasPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(email);

        if (cras == null) {
            model.addAttribute("error", "CRAS não autenticado.");
            return "error";
        }

        model.addAttribute("cras", cras);
        return "cadastrar-pessoa-deficiencia-cras";
    }

    @PostMapping("/cadastrar-pessoa-deficiencia-cras")
    public String processarCadastroPessoaComDeficienciaCras(
            @RequestParam String nome,
            @RequestParam List<String> deficiencias,
            @RequestParam(required = false) String descricao,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(email);

        if (cras == null) {
            model.addAttribute("error", "CRAS não autenticado.");
            return "error";
        }

        PessoaComDeficiencia novaPessoa = new PessoaComDeficiencia();
        novaPessoa.setNome(nome);
        novaPessoa.setDeficiencias(deficiencias); // Lista de deficiências
        novaPessoa.setDescricao(descricao);
        novaPessoa.setCras(cras);

        try {
            pessoaComDeficienciaService.savePessoaComDeficiencia(novaPessoa);
            model.addAttribute("message", "Cadastro realizado com sucesso!");
            return "redirect:/visualizar-pessoas-cadastradas-cras";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao cadastrar a pessoa: " + e.getMessage());
            return "error";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
        return "error";
    }
}
