package com.example.mob.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pessoa_com_deficiencia")
public class PessoaComDeficiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String descricao;   // Descrição adicional sobre a necessidade especial
    private String documento;
    private String telefone;
    private String endereco;
    private String dataNascimento;
    private String nomeResponsavel;
    private String telefoneResponsavel;
    private String observacoes;
    private String senha;

    @ElementCollection
    @CollectionTable(name = "pessoa_deficiencias", joinColumns = @JoinColumn(name = "pessoa_id"))
    private List<String> deficiencias = new ArrayList<>(); // Adicionado para múltiplas deficiências

    @ManyToOne
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @ManyToOne
    @JoinColumn(name = "cras_id")
    private Cras cras;

    // Getter e Setter para descricao
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Adicionando o método `getNecessidadesRequeridas`
    public List<String> getNecessidadesRequeridas() {
        List<String> necessidades = new ArrayList<>();
        if (deficiencias != null) {
            necessidades.addAll(deficiencias);
        }
        return necessidades;
    }

    // Getters e Setters para o atributo 'deficiencias'
    public List<String> getDeficiencias() {
        return deficiencias;
    }

    public void setDeficiencias(List<String> deficiencias) {
        this.deficiencias = deficiencias;
    }

    // Método para verificar se uma deficiência está na lista
    public boolean hasDeficiencia(String deficiencia) {
        return deficiencias != null && deficiencias.contains(deficiencia.toUpperCase());
    }

    // Outros Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public Cras getCras() {
        return cras;
    }

    public void setCras(Cras cras) {
        this.cras = cras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PessoaComDeficiencia that = (PessoaComDeficiencia) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(email, that.email) &&
               Objects.equals(telefone, that.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, telefone);
    }
}
