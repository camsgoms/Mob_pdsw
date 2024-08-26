package com.example.mob.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.Cras;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.OngService;
import com.example.mob.servicos.CrasService;
import com.example.mob.servicos.PessoaComDeficienciaService;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class LoginONGCRASController {

    @Autowired
    private OngService ongService;

    @Autowired
    private CrasService crasService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    @GetMapping("/login-ongcras")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos!");
        }
        return "login-ongcras"; // Nome do arquivo: login-ongcras.html
    }

    @PostMapping("/login-ongcras")
    public String login(@RequestParam String email, @RequestParam String senha, Model model) {
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);
        PessoaComDeficiencia pessoaComDeficiencia = pessoaComDeficienciaService.findByEmail(email);

        if (ong != null && ong.getSenha().equals(senha)) {
            return "redirect:/success";
        } else if (cras != null && cras.getSenha().equals(senha)) {
            return "redirect:/success-cras";
        } else if (pessoaComDeficiencia != null && pessoaComDeficiencia.getSenha().equals(senha)) {
            return "redirect:/login-usuario.html";
        } else {
            model.addAttribute("error", "Email ou senha inválidos!");
            return "login-ongcras"; // Nome do arquivo: login-ongcras.html
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success"; // Nome do arquivo: success.html
    }

    @GetMapping("/success-cras")
    public String successCrasPage() {
        return "success-cras"; // Nome do arquivo: success-cras.html
    }

    @GetMapping("/alterar-senha")
    public String alterarSenhaPage() {
        return "alterarSenha"; // Nome do arquivo: alterarSenha.html
    }

    @PostMapping("/alterar-senha")
    public String processarAlterarSenha(@RequestParam String email, @RequestParam String novaSenha, Model model) {
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);

        if (ong != null) {
            ong.setSenha(novaSenha);
            ongService.saveOng(ong);
            model.addAttribute("message", "Senha alterada com sucesso!");
        } else if (cras != null) {
            cras.setSenha(novaSenha);
            crasService.saveCras(cras);
            model.addAttribute("message", "Senha alterada com sucesso!");
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarSenha"; // Nome do arquivo: alterarSenha.html
        }
        return "redirect:/success";
    }

    @GetMapping("/alterar-dados")
    public String alterarDadosPage() {
        return "alterarDados"; // Nome do arquivo: alterarDados.html
    }

    @PostMapping("/alterar-dados")
    public String processarAlterarDados(@RequestParam String email, @RequestParam String nome, @RequestParam String novoEmail, Model model) {
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);

        if (ong != null) {
            ong.setNome(nome);
            ong.setEmail(novoEmail);
            ongService.saveOng(ong);
            model.addAttribute("message", "Dados alterados com sucesso!");
        } else if (cras != null) {
            cras.setNome(nome);
            cras.setEmail(novoEmail);
            crasService.saveCras(cras);
            model.addAttribute("message", "Dados alterados com sucesso!");
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarDados"; // Nome do arquivo: alterarDados.html
        }
        return "redirect:/success";
    }

    @GetMapping("/sessao-encerrada")
    public String sessaoEncerradaPage() {
        return "sessao-encerrada"; // Nome do arquivo: sessao-encerrada.html
    }

    // Adicionando o novo método para a página de sessão encerrada para pessoa
    @GetMapping("/sessao-encerrada-pessoa")
    public String sessaoEncerradaPessoaPage() {
        return "sessao-encerrada-pessoa"; // Nome do arquivo: sessao-encerrada-pessoa.html
    }

    @GetMapping("/cadastrar-pessoa-deficiencia")
    public String cadastrarPessoaComDeficienciaPage() {
        return "cadastrarPessoaComDeficiencia"; // Nome do arquivo: cadastrarPessoaComDeficiencia.html
    }

    @PostMapping("/cadastrar-pessoa-deficiencia")
    public String processarCadastroPessoaComDeficiencia(@RequestParam String nome, @RequestParam String email, @RequestParam String deficiencia, Model model) {
        // Gerar uma senha aleatória
        String senhaGerada = generateRandomPassword();
        
        PessoaComDeficiencia novaPessoa = new PessoaComDeficiencia();
        novaPessoa.setNome(nome);
        novaPessoa.setEmail(email);
        novaPessoa.setDeficiencia(deficiencia);
        novaPessoa.setSenha(senhaGerada); // Armazenar a senha gerada
        
        pessoaComDeficienciaService.savePessoaComDeficiencia(novaPessoa);

        // Enviar email com a senha gerada (opcional)
        // enviarEmail(email, senhaGerada); // Implementar função de envio de email

        model.addAttribute("message", "Cadastro realizado com sucesso! Sua senha é: " + senhaGerada);
        return "redirect:/success"; // Redireciona para uma página de sucesso
    }

    @GetMapping("/alterar-senha-pessoa")
    public String alterarSenhaPessoaPage() {
        return "alterarSenhaPessoa"; // Nome do arquivo: alterarSenhaPessoa.html
    }

    @PostMapping("/alterar-senha-pessoa")
    public String processarAlterarSenhaPessoa(@RequestParam String email, @RequestParam String novaSenha, Model model) {
        PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);

        if (pessoa != null) {
            pessoa.setSenha(novaSenha);
            pessoaComDeficienciaService.savePessoaComDeficiencia(pessoa);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/success";
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarSenhaPessoa"; // Nome do arquivo: alterarSenhaPessoa.html
        }
    }

    @GetMapping("/alterar-dados-pessoa")
    public String alterarDadosPessoaPage() {
        return "alterarDadosPessoa"; // Nome do arquivo: alterarDadosPessoa.html
    }

    @PostMapping("/alterar-dados-pessoa")
    public String processarAlterarDadosPessoa(@RequestParam String email, @RequestParam String nome, @RequestParam String novoEmail, Model model) {
        PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);

        if (pessoa != null) {
            pessoa.setNome(nome);
            pessoa.setEmail(novoEmail);
            pessoaComDeficienciaService.savePessoaComDeficiencia(pessoa);
            model.addAttribute("message", "Dados alterados com sucesso!");
            return "redirect:/success";
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarDadosPessoa"; // Nome do arquivo: alterarDadosPessoa.html
        }
    }

    // Método para gerar uma senha aleatória
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
