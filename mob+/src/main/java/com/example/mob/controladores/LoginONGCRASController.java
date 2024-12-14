package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.OngService;
import com.example.mob.servicos.PessoaComDeficienciaService;

import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Controller
public class LoginONGCRASController {

    @Autowired
    private OngService ongService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    // Página de login para ONGs/CRAS
    @GetMapping("/login-ongcras")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos!");
        }
        return "redirect:/loginONGCRAS.html"; // Redireciona para o arquivo na pasta static
    }

    // Processa o login de ONGs e CRAS
    @PostMapping("/login-ongcras")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        Ong ong = ongService.findByEmail(email);

        if (ong != null && ong.getSenha().equals(senha)) {
            session.setAttribute("emailLogado", email);
            session.setAttribute("usuarioTipo", "ong");
            return "redirect:/success"; // A rota /success é gerenciada pelo LoginController
        }

        model.addAttribute("error", "Email ou senha inválidos!");
        return "redirect:/loginONGCRAS";
    }

    // Página de cadastro de pessoas com deficiência
    @GetMapping("/cadastrar-pessoa-deficiencia")
    public String cadastrarPessoaComDeficienciaPage() {
        return "cadastrarPessoaComDeficiencia";
    }

    // Processa o cadastro de uma pessoa com deficiência
    @PostMapping("/cadastrar-pessoa-deficiencia")
    public String processarCadastroPessoaComDeficiencia(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam List<String> deficiencias, // Para suportar múltiplas deficiências
            @RequestParam(required = false) String descricao,
            HttpSession session,
            Model model) {

        String emailLogado = (String) session.getAttribute("emailLogado");
        Ong ong = ongService.findByEmail(emailLogado);

        if (ong == null) {
            model.addAttribute("error", "ONG não encontrada.");
            return "error";
        }

        PessoaComDeficiencia novaPessoa = new PessoaComDeficiencia();
        novaPessoa.setNome(nome);
        novaPessoa.setEmail(email);
        novaPessoa.setDeficiencias(deficiencias); // Lista de deficiências
        novaPessoa.setDescricao(descricao);
        novaPessoa.setOng(ong);

        // Gera uma senha aleatória e atribui ao usuário
        String senhaGerada = generateRandomPassword();
        novaPessoa.setSenha(senhaGerada);

        pessoaComDeficienciaService.savePessoaComDeficiencia(novaPessoa);

        model.addAttribute("message", "Cadastro realizado com sucesso! A senha gerada para o usuário foi: " + senhaGerada);
        return "redirect:/success"; // Redireciona para a página de sucesso gerenciada pelo LoginController
    }

    // Método para gerar uma senha aleatória
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8]; // 8 bytes = 8 caracteres
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
