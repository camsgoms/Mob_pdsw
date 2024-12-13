package com.example.mob.entidades;

import jakarta.persistence.*;

@Entity
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pontoPartida;
    private String pontoDestino;

    private Double partidaLatitude;
    private Double partidaLongitude;
    private Double destinoLatitude;
    private Double destinoLongitude;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @ManyToOne
    @JoinColumn(name = "pessoa_com_deficiencia_id")
    private PessoaComDeficiencia pessoaComDeficiencia;

    private String status;
    private String requisitosAdicionais;  // Necessidades especiais do usuário
    private String deficiencia;  // Tipo de deficiência

    @Column(length = 500)  // Add this field for description
    private String descricaoNecessidades;

    // Construtor padrão
    public Viagem() {}

    // Construtor com todos os parâmetros
    public Viagem(String pontoPartida, String pontoDestino, Double partidaLatitude, Double partidaLongitude,
                  Double destinoLatitude, Double destinoLongitude, String requisitosAdicionais) {
        this.pontoPartida = pontoPartida;
        this.pontoDestino = pontoDestino;
        this.partidaLatitude = partidaLatitude;
        this.partidaLongitude = partidaLongitude;
        this.destinoLatitude = destinoLatitude;
        this.destinoLongitude = destinoLongitude;
        this.requisitosAdicionais = requisitosAdicionais;
    }

    // Construtor com apenas ponto de partida e ponto de destino
    public Viagem(String pontoPartida, String pontoDestino) {
        this.pontoPartida = pontoPartida;
        this.pontoDestino = pontoDestino;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPontoPartida() {
        return pontoPartida;
    }

    public void setPontoPartida(String pontoPartida) {
        this.pontoPartida = pontoPartida;
    }

    public String getPontoDestino() {
        return pontoDestino;
    }

    public void setPontoDestino(String pontoDestino) {
        this.pontoDestino = pontoDestino;
    }

    public Double getPartidaLatitude() {
        return partidaLatitude;
    }

    public void setPartidaLatitude(Double partidaLatitude) {
        this.partidaLatitude = partidaLatitude;
    }

    public Double getPartidaLongitude() {
        return partidaLongitude;
    }

    public void setPartidaLongitude(Double partidaLongitude) {
        this.partidaLongitude = partidaLongitude;
    }

    public Double getDestinoLatitude() {
        return destinoLatitude;
    }

    public void setDestinoLatitude(Double destinoLatitude) {
        this.destinoLatitude = destinoLatitude;
    }

    public Double getDestinoLongitude() {
        return destinoLongitude;
    }

    public void setDestinoLongitude(Double destinoLongitude) {
        this.destinoLongitude = destinoLongitude;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public PessoaComDeficiencia getPessoaComDeficiencia() {
        return pessoaComDeficiencia;
    }

    public void setPessoaComDeficiencia(PessoaComDeficiencia pessoaComDeficiencia) {
        this.pessoaComDeficiencia = pessoaComDeficiencia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequisitosAdicionais() {
        return requisitosAdicionais;
    }

    public void setRequisitosAdicionais(String requisitosAdicionais) {
        this.requisitosAdicionais = requisitosAdicionais;
    }

    public String getDeficiencia() {
        return deficiencia;
    }

    public void setDeficiencia(String deficiencia) {
        this.deficiencia = deficiencia;
    }

    public String getDescricaoNecessidades() {
        return descricaoNecessidades;
    }

    public void setDescricaoNecessidades(String descricaoNecessidades) {
        this.descricaoNecessidades = descricaoNecessidades;
    }
}
