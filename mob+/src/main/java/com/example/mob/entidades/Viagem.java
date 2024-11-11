package com.example.mob.entidades;

import jakarta.persistence.*;

@Entity
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pontoPartida;
    private String pontoFinal;

    private Double partidaLatitude;
    private Double partidaLongitude;
    private Double destinoLatitude;
    private Double destinoLongitude;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    // Relacionamento com a entidade PessoaComDeficiencia
    @ManyToOne
    @JoinColumn(name = "pessoa_com_deficiencia_id")
    private PessoaComDeficiencia pessoaComDeficiencia;

    private String status;

    // Construtor padrão
    public Viagem() {}

    // Construtor com todos os parâmetros
    public Viagem(String pontoPartida, String pontoFinal, Double partidaLatitude, Double partidaLongitude, Double destinoLatitude, Double destinoLongitude) {
        this.pontoPartida = pontoPartida;
        this.pontoFinal = pontoFinal;
        this.partidaLatitude = partidaLatitude;
        this.partidaLongitude = partidaLongitude;
        this.destinoLatitude = destinoLatitude;
        this.destinoLongitude = destinoLongitude;
    }

    // Construtor com apenas ponto de partida e ponto final
    public Viagem(String pontoPartida, String pontoFinal) {
        this.pontoPartida = pontoPartida;
        this.pontoFinal = pontoFinal;
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

    public String getPontoFinal() {
        return pontoFinal;
    }

    public void setPontoFinal(String pontoFinal) {
        this.pontoFinal = pontoFinal;
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
}
