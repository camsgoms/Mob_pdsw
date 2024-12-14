package com.example.mob.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;
    private String veiculo;
    private String telefone;

    @Column(nullable = false)
    private boolean disponivel = false; // Inicializando como indisponível por padrão

    @ElementCollection
    @CollectionTable(name = "necessidades_motorista", joinColumns = @JoinColumn(name = "motorista_id"))
    @Column(name = "necessidade")
    private List<String> necessidadesEspeciais = new ArrayList<>();

    // Construtor padrão (necessário para o JPA)
    public Motorista() {}

    // Construtor com todos os parâmetros necessários
    public Motorista(String nome, String email, String senha, String veiculo, String telefone, List<String> necessidadesEspeciais) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.veiculo = veiculo;
        this.telefone = telefone;
        this.disponivel = false; // Motorista começa como indisponível
        this.necessidadesEspeciais = necessidadesEspeciais != null ? necessidadesEspeciais : new ArrayList<>();
    }

    // Getters e Setters
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<String> getNecessidadesEspeciais() {
        return necessidadesEspeciais;
    }

    public void setNecessidadesEspeciais(List<String> necessidadesEspeciais) {
        this.necessidadesEspeciais = necessidadesEspeciais != null ? necessidadesEspeciais : new ArrayList<>();
    }
}
