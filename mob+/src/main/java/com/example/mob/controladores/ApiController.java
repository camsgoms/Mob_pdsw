package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.PessoaComDeficienciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {


	    @Autowired
	    private PessoaComDeficienciaService pessoaComDeficienciaService;

	    @Autowired
	    private MotoristaService motoristaService;

	    /**
	     * Verifica se o email pertence a uma pessoa com deficiência ou a um motorista.
	     * 
	     * @param email Email a ser verificado.
	     * @return Mapa com informações sobre o tipo de usuário.
	     */
	    @GetMapping("/api/check-email")
	    public Map<String, Boolean> checkEmail(@RequestParam String email) {
	        Map<String, Boolean> response = new HashMap<>();

	        // Verifica se o email pertence a uma pessoa com deficiência
	        PessoaComDeficiencia pessoa = pessoaComDeficienciaService.findByEmail(email);
	        response.put("isPessoaComDeficiencia", pessoa != null);

	        // Verifica se o email pertence a um motorista
	        Motorista motorista = motoristaService.findByEmail(email);
	        response.put("isMotorista", motorista != null);

	        return response;
	    }
	

    }

