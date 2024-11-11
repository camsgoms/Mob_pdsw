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

import jakarta.servlet.http.HttpSession;

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
        return "redirect:/loginONGCRAS.html"; // Redireciona para o arquivo na pasta static
    }

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
        Ong ong = ongService.findByEmail(email);  // Buscar ONG pelo email

        if (ong != null) {
            model.addAttribute("ong", ong);  // Adiciona a ONG ao modelo
        } else {
            model.addAttribute("error", "ONG não encontrada");
        }

        return "success";  // Nome do arquivo: success.html
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

    @GetMapping("/alterar-senha")
    public String alterarSenhaPage() {
        return "alterarSenha";
    }

    @PostMapping("/alterar-senha")
    public String processarAlterarSenha(
            @RequestParam String email, 
            @RequestParam String novaSenha, 
            HttpSession session, 
            Model model) {
        
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);

        if (ong != null) {
            ong.setSenha(novaSenha);
            ongService.saveOng(ong);  // Atualiza no banco de dados

            // Atualizar o email da sessão, se necessário
            session.setAttribute("emailLogado", ong.getEmail());  // Atualiza o e-mail na sessão, caso tenha sido alterado
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/success";  // Redireciona para a página de sucesso da ONG
        } else if (cras != null) {
            cras.setSenha(novaSenha);
            crasService.saveCras(cras);  // Atualiza no banco de dados

            // Atualizar o email da sessão, se necessário
            session.setAttribute("emailLogado", cras.getEmail());  // Atualiza o e-mail na sessão, caso tenha sido alterado
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/success-cras";  // Redireciona para a página de sucesso do CRAS
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarSenha";  // Redireciona para a página de alteração de senha em caso de erro
        }
    }


    @GetMapping("/alterar-dados")
    public String alterarDadosPage() {
        return "alterarDados";
    }

    @PostMapping("/alterar-dados")
    public String processarAlterarDados(@RequestParam String email, @RequestParam String nome, @RequestParam String novoEmail, Model model, HttpSession session) {
        Ong ong = ongService.findByEmail(email);
        Cras cras = crasService.findByEmail(email);

        if (ong != null) {
            ong.setNome(nome);
            ong.setEmail(novoEmail);
            ongService.saveOng(ong);
            session.setAttribute("emailLogado", novoEmail); // Atualiza o e-mail na sessão
            model.addAttribute("message", "Dados alterados com sucesso!");
            return "redirect:/success"; // Redireciona para a página de sucesso da ONG
        } else if (cras != null) {
            cras.setNome(nome);
            cras.setEmail(novoEmail);
            crasService.saveCras(cras);
            session.setAttribute("emailLogado", novoEmail); // Atualiza o e-mail na sessão
            model.addAttribute("message", "Dados alterados com sucesso!");
            return "redirect:/success-cras"; // Redireciona para a página de sucesso do CRAS
        } else {
            model.addAttribute("error", "Email não encontrado!");
            return "alterarDados"; // Retorna à página de alteração se o e-mail não for encontrado
        }

    }

    @GetMapping("/sessao-encerrada")
    public String sessaoEncerradaPage() {
        return "sessao-encerrada"; // Nome do arquivo: sessao-encerrada.html
    }

    @GetMapping("/sessao-encerrada-pessoa")
    public String sessaoEncerradaPessoaPage() {
        return "sessao-encerrada-pessoa"; // Nome do arquivo: sessao-encerrada-pessoa.html
    }

    // Cadastro e visualização de pessoas com deficiência para ONG
    @GetMapping("/cadastrar-pessoa-deficiencia")
    public String cadastrarPessoaComDeficienciaPage() {
        return "cadastrarPessoaComDeficiencia"; // Nome do arquivo HTML
    }

    @PostMapping("/cadastrar-pessoa-deficiencia")
    public String processarCadastroPessoaComDeficiencia(@RequestParam String nome, @RequestParam String email, @RequestParam String deficiencia, HttpSession session, Model model) {
        String emailLogado = (String) session.getAttribute("emailLogado");
        Ong ong = ongService.findByEmail(emailLogado);

        if (ong == null) {
            model.addAttribute("error", "ONG não encontrada.");
            return "error";
        }

        // Criar uma nova pessoa com deficiência
        PessoaComDeficiencia novaPessoa = new PessoaComDeficiencia();
        novaPessoa.setNome(nome);
        novaPessoa.setEmail(email);
        novaPessoa.setDeficiencia(deficiencia);
        novaPessoa.setOng(ong); // Associar a ONG à pessoa

        // Gerar e definir uma senha aleatória
        String senhaGerada = generateRandomPassword();
        novaPessoa.setSenha(senhaGerada);

        // Salvar a nova pessoa
        pessoaComDeficienciaService.savePessoaComDeficiencia(novaPessoa);

        model.addAttribute("message", "Cadastro realizado com sucesso!");
        return "redirect:/success";
    }

    @GetMapping("/visualizar-pessoas-cadastradas")
    public String visualizarPessoasCadastradas(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");

        Ong ong = ongService.findByEmail(email);

        if (ong != null) {
            // Recupera as pessoas cadastradas pela ONG
            model.addAttribute("pessoas", pessoaComDeficienciaService.findByOng(ong));
        } else {
            model.addAttribute("error", "Nenhuma entidade encontrada.");
        }

        return "visualizar-pessoas-cadastradas"; // Nome do arquivo HTML que exibirá a lista
    }

    // Cadastro e visualização de pessoas com deficiência para CRAS
    @GetMapping("/cadastrar-pessoa-deficiencia-cras")
    public String cadastrarPessoaComDeficienciaCRASPage() {
        return "cadastrar-pessoa-deficiencia-cras"; // Nome correto do arquivo HTML
    }

    @PostMapping("/cadastrar-pessoa-deficiencia-cras")
    public String processarCadastroPessoaComDeficienciaCRAS(@RequestParam String nome, @RequestParam String email, @RequestParam String deficiencia, HttpSession session, Model model) {
        String emailLogado = (String) session.getAttribute("emailLogado");
        Cras cras = crasService.findByEmail(emailLogado);

        if (cras == null) {
            model.addAttribute("error", "CRAS não encontrado.");
            return "error";
        }

        // Criar uma nova pessoa com deficiência
        PessoaComDeficiencia novaPessoa = new PessoaComDeficiencia();
        novaPessoa.setNome(nome);
        novaPessoa.setEmail(email);
        novaPessoa.setDeficiencia(deficiencia);
        novaPessoa.setCras(cras); // Associar o CRAS à pessoa

        // Gerar e definir uma senha aleatória
        String senhaGerada = generateRandomPassword();
        novaPessoa.setSenha(senhaGerada);

        // Salvar a nova pessoa
        pessoaComDeficienciaService.savePessoaComDeficiencia(novaPessoa);

        model.addAttribute("message", "Cadastro realizado com sucesso!");
        return "redirect:/success-cras";
    }

    @GetMapping("/visualizar-pessoas-cadastradas-cras")
    public String visualizarPessoasCadastradasCRAS(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailLogado");

        Cras cras = crasService.findByEmail(email);

        if (cras != null) {
            model.addAttribute("cras", cras); // Certifique-se de adicionar o objeto 'cras' ao modelo
            model.addAttribute("pessoas", pessoaComDeficienciaService.findByCras(cras)); // Pessoas cadastradas
        } else {
            model.addAttribute("error", "Nenhuma entidade encontrada.");
        }

        return "visualizar-pessoas-cadastradas-cras"; // Nome do arquivo HTML que exibirá a lista
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
