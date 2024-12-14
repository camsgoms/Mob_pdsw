package com.example.mob.entidades;

<<<<<<< HEAD
import jakarta.persistence.Column;
=======
>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
<<<<<<< HEAD

    private String cnpj;

    private String cidade;

    private String bairro;

    private String contato;

    private String tipo;

    @Column(unique = true, nullable = false) // Garantir unicidade e que o campo não seja nulo
    private String email;

    private String senha; // Atributo para senha
=======
    private String cnpj;
    private String cidade;
    private String bairro;
    private String contato;
    private String tipo;
    private String email;
    private String senha; // Adicionando o atributo senha
>>>>>>> de3de605a8be45961874f858a0848d667f0adfd7

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
