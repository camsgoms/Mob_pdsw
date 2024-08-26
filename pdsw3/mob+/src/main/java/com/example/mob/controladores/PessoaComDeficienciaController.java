package com.example.mob.controladores;

import com.example.mob.servicos.PessoaComDeficienciaService;
import com.example.mob.entidades.PessoaComDeficiencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pessoa-deficiencia")
public class PessoaComDeficienciaController {

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    @PostMapping("/cadastrar")
    public String cadastrarPessoaComDeficiencia(
        @RequestParam("nome") String nome,
        @RequestParam("tipoDeficiencia") String tipoDeficiencia,
        @RequestParam("descricao") String descricao,
        @RequestParam("outroParametro") Long outroParametro,
        Model model
    ) {
        PessoaComDeficiencia pessoa = new PessoaComDeficiencia();
        pessoa.setNome(nome);
        pessoa.setTipoDeficiencia(tipoDeficiencia);
        pessoa.setDescricao(descricao);
        // Configurar o outroParametro conforme necessário

        pessoaComDeficienciaService.savePessoaComDeficiencia(pessoa);

        model.addAttribute("mensagem", "Pessoa com deficiência cadastrada com sucesso!");
        return "resultadoCadastro"; // Retorne o nome da view adequada
    }
}
