package com.example.mob.repositorios;

import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.entidades.Ong;
import com.example.mob.entidades.Cras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PessoaComDeficienciaRepository extends JpaRepository<PessoaComDeficiencia, Long> {

    /**
     * Busca uma pessoa com deficiência pelo email.
     * 
     * @param email O email da pessoa.
     * @return PessoaComDeficiencia correspondente ao email.
     */
    PessoaComDeficiencia findByEmail(String email);

    /**
     * Busca uma pessoa com deficiência pelo ID e carrega suas deficiências.
     * 
     * @param id O ID da pessoa.
     * @return PessoaComDeficiencia com deficiências carregadas.
     */
    @Query("SELECT p FROM PessoaComDeficiencia p LEFT JOIN FETCH p.deficiencias WHERE p.id = :id")
    PessoaComDeficiencia findByIdWithDeficiencias(@Param("id") Long id);

    /**
     * Busca pessoas cadastradas por uma ONG específica.
     * 
     * @param ong A ONG relacionada.
     * @return Lista de pessoas com deficiência cadastradas na ONG.
     */
    List<PessoaComDeficiencia> findByOng(Ong ong);

    /**
     * Busca pessoas cadastradas por um CRAS específico.
     * 
     * @param cras O CRAS relacionado.
     * @return Lista de pessoas com deficiência cadastradas no CRAS.
     */
    List<PessoaComDeficiencia> findByCras(Cras cras);

    /**
     * Verifica se um email pertence a uma pessoa com deficiência sem carregar todos os detalhes.
     * 
     * @param email O email da pessoa.
     * @return true se existir, false caso contrário.
     */
    @Query("SELECT COUNT(p) > 0 FROM PessoaComDeficiencia p WHERE LOWER(p.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);
}
