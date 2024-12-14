package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mob.entidades.Ong;
<<<<<<< HEAD
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.OngService;
=======
import com.example.mob.entidades.Cras;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.OngService;
import com.example.mob.servicos.CrasService;
>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
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
<<<<<<< HEAD
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    // Página de login para ONGs/CRAS
=======
    private CrasService crasService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
    @GetMapping("/login-ongcras")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos!");
        }
        return "redirect:/loginONGCRAS.html"; // Redireciona para o arquivo na pasta static
    }

<<<<<<< HEAD
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
=======
    @PostMapping("/login-ongcras")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);
        PessoaComDeficiencia pessoaComDeficiencia = pessoaComDeficienciaService.findByEmail(email);

        if (ong != null && ong.getSenha().equals(senha)) {
            session.setAttribute("emailLogado", email);
            return "redirect:/success";
        } else if (cras != null && cras.getSenha().equals(senha)) {
            session.setAttribute("emailLogado", email);
            return "redirect:/success-cras";
        } else if (pessoaComDeficiencia != null && pessoaComDeficiencia.getSenha().equals(senha)) {
            return "redirect:/login-usuario";
        } else {
            model.addAttribute("error", "Email ou senha inválidos!");
            return "redirect:/loginONGCRAS";
        }
    }

    @GetMapping("/success")
    public String successOngPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Ong ong = ongService.findByEmail(email);

        if (ong != null) {
            model.addAttribute("ong", ong);
        } else {
            model.addAttribute("error", "ONG não encontrada");
        }

        return "success";
    }

    @GetMapping("/success-cras")
    public String successCrasPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(email);

        if (cras != null) {
            model.addAttribute("cras", cras);
        } else {
            model.addAttribute("error", "CRAS não encontrado");
        }
        return "success-cras";
    }

>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
    @GetMapping("/cadastrar-pessoa-deficiencia")
    public String cadastrarPessoaComDeficienciaPage() {
        return "cadastrarPessoaComDeficiencia";
    }

<<<<<<< HEAD
    // Processa o cadastro de uma pessoa com deficiência
=======
>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
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
<<<<<<< HEAD
        return "redirect:/success"; // Redireciona para a página de sucesso gerenciada pelo LoginController
=======
        return "redirect:/success";
    }

    @GetMapping("/visualizar-pessoas-cadastradas")
    public String visualizarPessoasCadastradas(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");
        Ong ong = ongService.findByEmail(email);

        if (ong != null) {
            model.addAttribute("pessoas", pessoaComDeficienciaService.findByOng(ong));
        } else {
            model.addAttribute("error", "Nenhuma entidade encontrada.");
        }

        return "visualizar-pessoas-cadastradas";
>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
    }

    // Método para gerar uma senha aleatória
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8]; // 8 bytes = 8 caracteres
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
