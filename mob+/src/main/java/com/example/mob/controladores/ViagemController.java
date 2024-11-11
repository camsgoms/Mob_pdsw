package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.Viagem;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.PessoaComDeficienciaService;
import com.example.mob.servicos.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ViagemController {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    // Página para realizar viagem (Pessoa com Deficiência), onde a pessoa pode selecionar um motorista disponível
    @GetMapping("/realizar-viagem")
    public String realizarViagem(Model model) {
        List<Motorista> motoristasDisponiveis = motoristaService.listarMotoristasDisponiveis();
        model.addAttribute("motoristasDisponiveis", motoristasDisponiveis);
        return "realizar-viagem";  // Nome do template Thymeleaf
    }

    // Método para visualizar as viagens da pessoa com deficiência
    @GetMapping("/visualizar-viagens-usuario")
    public String visualizarViagensUsuario(HttpSession session, Model model) {
        PessoaComDeficiencia pessoaLogada = (PessoaComDeficiencia) session.getAttribute("pessoaComDeficienciaLogada");

        if (pessoaLogada != null) {
            List<Viagem> viagens = viagemService.buscarViagensPorPessoaComDeficiencia(pessoaLogada.getId());
            model.addAttribute("viagens", viagens);
            return "visualizar-viagens-usuario";  // Nome do template Thymeleaf
        } else {
            return "redirect:/login";  // Redireciona para o login
        }
    }

    // Página para visualizar as viagens associadas ao motorista logado
    @GetMapping("/visualizar-viagens")
    public String visualizarViagens(HttpSession session, Model model) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");
        if (motorista != null) {
            List<Viagem> viagens = viagemService.buscarViagensPorMotorista(motorista.getId());
            model.addAttribute("viagens", viagens);
            return "visualizar-viagens";  // Nome do template Thymeleaf para motoristas
        } else {
            return "redirect:/motoristas/login";  // Redireciona para o login se o motorista não estiver logado
        }
    }

    // Método para salvar uma viagem (Pessoa com Deficiência)
    @PostMapping("/viagens/salvar")
    @ResponseBody
    public ResponseEntity<String> salvarViagem(@RequestParam String pontoPartida,
                                               @RequestParam String pontoFinal,
                                               @RequestParam Long motoristaId,
                                               HttpSession session) {
        PessoaComDeficiencia pessoaLogada = (PessoaComDeficiencia) session.getAttribute("pessoaComDeficienciaLogada");
        if (pessoaLogada == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
        if (motorista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motorista não encontrado!");
        }

        PessoaComDeficiencia pessoaComDeficiencia = pessoaComDeficienciaService.findById(pessoaLogada.getId());

        if (pessoaComDeficiencia == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com Deficiência não encontrada!");
        }

        Viagem viagem = new Viagem(pontoPartida, pontoFinal);
        viagem.setMotorista(motorista);
        viagem.setPessoaComDeficiencia(pessoaComDeficiencia);
        viagem.setStatus("Aguardando início");

        viagemService.salvarViagem(viagem);
        return ResponseEntity.ok("Viagem salva com sucesso!");
    }

    // Método para aceitar uma viagem (Motorista)
    @PostMapping("/corridas/{id}/aceitar")
    public ResponseEntity<String> aceitarViagem(@PathVariable Long id, HttpSession session) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");
        if (motorista == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Motorista não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null) {
            viagem.setMotorista(motorista);
            viagem.setStatus("Em andamento");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem aceita com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        }
    }

    // Método para rejeitar uma viagem (Motorista)
    @PostMapping("/corridas/{id}/rejeitar")
    public ResponseEntity<String> rejeitarViagem(@PathVariable Long id, HttpSession session) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");
        if (motorista == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Motorista não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null && viagem.getMotorista() != null && viagem.getMotorista().getId().equals(motorista.getId())) {
            viagem.setMotorista(null);
            viagem.setStatus("Disponível");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem rejeitada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada ou motorista inválido");
        }
    }

    // Método para finalizar a viagem pelo motorista
    @PostMapping("/corridas/{id}/finalizar")
    public ResponseEntity<String> finalizarViagemPeloMotorista(@PathVariable Long id, HttpSession session) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");
        if (motorista == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Motorista não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null && viagem.getMotorista() != null && viagem.getMotorista().getId().equals(motorista.getId())) {
            viagem.setStatus("Finalizada pelo motorista");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem marcada como finalizada pelo motorista, aguardando confirmação do usuário.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada ou motorista inválido");
        }
    }

    // Método para o usuário confirmar a finalização da viagem
    @PostMapping("/corridas/{id}/confirmar-finalizacao")
    public ResponseEntity<String> confirmarFinalizacao(@PathVariable Long id, @RequestParam boolean confirmacao, HttpSession session) {
        PessoaComDeficiencia pessoa = (PessoaComDeficiencia) session.getAttribute("pessoaComDeficienciaLogada");
        if (pessoa == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null && viagem.getPessoaComDeficiencia().getId().equals(pessoa.getId())) {
            if (confirmacao) {
                viagem.setStatus("Finalizada");
                viagemService.salvarViagem(viagem);
                return ResponseEntity.ok("Viagem finalizada com sucesso!");
            } else {
                viagem.setStatus("Em andamento");
                viagemService.salvarViagem(viagem);
                return ResponseEntity.ok("A viagem continuará em andamento.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada ou usuário inválido");
        }
    }

    // Método para solicitar um novo motorista (Pessoa com Deficiência)
    @PostMapping("/corridas/{id}/solicitar-novo")
    public ResponseEntity<String> solicitarNovoMotorista(@PathVariable Long id, HttpSession session) {
        PessoaComDeficiencia pessoaLogada = (PessoaComDeficiencia) session.getAttribute("pessoaComDeficienciaLogada");
        if (pessoaLogada == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null && viagem.getStatus().equals("Disponível")) {
            viagem.setStatus("Aguardando motorista");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Solicitação de novo motorista realizada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada ou status inválido");
        }
    }
}
