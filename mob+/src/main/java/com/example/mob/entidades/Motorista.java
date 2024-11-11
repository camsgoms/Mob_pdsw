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
    private boolean disponivel = false; // Inicializando com false por padrão

    // Relacionamento One-to-Many com a entidade Viagem
    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Viagem> viagens = new ArrayList<>();

    // Construtor padrão (necessário para o JPA)
    public Motorista() {}

    // Construtor com parâmetros
    public Motorista(String nome, String email, String senha, String veiculo, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.veiculo = veiculo;
        this.telefone = telefone;
        this.disponivel = false; // Novo motorista começa como indisponível
    }

    // Getters e Setters
    public Long getId() {
        return id;
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

    // Getters e Setters para o relacionamento de Viagens
    public List<Viagem> getViagens() {
        return viagens;
    }

    public void setViagens(List<Viagem> viagens) {
        this.viagens = viagens;
    }
}
