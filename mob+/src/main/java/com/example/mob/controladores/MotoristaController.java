package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.Viagem;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MotoristaController {

	@Autowired
	private MotoristaService motoristaService;

    // Mapeamento para processar o cadastro do motorista
    @PostMapping("/cadastrar")
    public String cadastrarMotorista(@RequestParam String nome, @RequestParam String email,
                                     @RequestParam String senha, @RequestParam String veiculo,
                                     @RequestParam String telefone, Model model) {
        Motorista motorista = new Motorista(nome, email, senha, veiculo, telefone);
        motoristaService.salvarMotorista(motorista);
        model.addAttribute("message", "Motorista cadastrado com sucesso!");
        return"redirect:/motorista/sucesso";  // Redireciona para a página de login após o cadastro
    }
    
    @GetMapping("/motorista/sucesso")
    public String sucesso() {
    	return "cadastromotorista_feito";
    }

  
}
