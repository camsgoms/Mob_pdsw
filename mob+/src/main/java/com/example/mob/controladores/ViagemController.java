package com.example.mob.controladores;

import com.example.mob.entidades.Motorista;
import com.example.mob.entidades.PessoaComDeficiencia;
import com.example.mob.entidades.Viagem;
import com.example.mob.servicos.MotoristaService;
import com.example.mob.servicos.PessoaComDeficienciaService;
import com.example.mob.servicos.ViagemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ViagemController {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private PessoaComDeficienciaService pessoaComDeficienciaService;

    @GetMapping("/realizar-viagem")
    public String realizarViagem(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            PessoaComDeficiencia pessoaLogada = pessoaComDeficienciaService.findByIdWithDeficiencias(usuarioId);
            if (pessoaLogada == null) {
                model.addAttribute("error", "Usuário não encontrado.");
                return "redirect:/login";
            }

            model.addAttribute("pessoa", pessoaLogada);

            List<Motorista> motoristasDisponiveis = motoristaService.listarMotoristasCompatíveis(pessoaLogada);
            model.addAttribute("motoristasDisponiveis", motoristasDisponiveis);

            return "realizar-viagem";
        }

        model.addAttribute("error", "Usuário não autenticado.");
        return "redirect:/login";
    }

    @GetMapping("/visualizar-viagens")
    public String visualizarViagens(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        String usuarioTipo = (String) session.getAttribute("usuarioTipo");

        if (usuarioId != null && "motorista".equals(usuarioTipo)) {
            Motorista motorista = motoristaService.buscarMotoristaPorId(usuarioId);
            if (motorista != null) {
                List<Viagem> viagens = viagemService.buscarViagensPorMotorista(motorista.getId());
                model.addAttribute("viagens", viagens);
                return "visualizar-viagens";
            }
        }

        model.addAttribute("error", "Usuário não autenticado ou permissão insuficiente.");
        return "redirect:/login";
    }

    @GetMapping("/visualizar-viagens-usuario")
    public String visualizarViagensUsuario(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            List<Viagem> viagens = viagemService.buscarViagensPorPessoaComDeficiencia(usuarioId);
            model.addAttribute("viagens", viagens);
            return "visualizar-viagens-usuario";
        }

        return "redirect:/login";
    }

    @PostMapping("/viagens/salvar")
    @ResponseBody
    public ResponseEntity<String> salvarViagem(@RequestParam String pontoPartida,
                                               @RequestParam String pontoDestino,
                                               @RequestParam Long motoristaId,
                                               @RequestParam(required = false) String descricaoNecessidades,
                                               HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        PessoaComDeficiencia pessoaLogada = pessoaComDeficienciaService.findByIdWithDeficiencias(usuarioId);
        if (pessoaLogada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com deficiência não encontrada!");
        }

        Motorista motorista = motoristaService.buscarMotoristaPorId(motoristaId);
        if (motorista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Motorista não encontrado!");
        }

        // Validação de tamanho dos campos
        if (pontoPartida.length() > 255) {
            pontoPartida = pontoPartida.substring(0, 255);
        }
        if (pontoDestino.length() > 255) {
            pontoDestino = pontoDestino.substring(0, 255);
        }

        // Define os requisitos adicionais
        String requisitos = descricaoNecessidades != null && !descricaoNecessidades.trim().isEmpty()
                ? descricaoNecessidades.trim()
                : "Nenhuma descrição fornecida";

        // Criando e salvando a viagem
        Viagem viagem = new Viagem(pontoPartida, pontoDestino);
        viagem.setMotorista(motorista);
        viagem.setPessoaComDeficiencia(pessoaLogada);
        viagem.setStatus("Aguardando início");
        viagem.setDescricaoNecessidades(requisitos);

        try {
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem salva com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a viagem: " + e.getMessage());
        }
    }

    @PostMapping("/corridas/{id}/aceitar")
    public ResponseEntity<String> aceitarViagem(@PathVariable Long id, HttpSession session) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");

        if (motorista == null) {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId != null) {
                motorista = motoristaService.buscarMotoristaPorId(usuarioId);
                if (motorista != null) {
                    session.setAttribute("motoristaLogado", motorista);
                }
            }
        }

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

    @PostMapping("/corridas/{id}/rejeitar")
    public String rejeitarViagemERedirecionar(@PathVariable Long id, HttpSession session, Model model) {
        Motorista motorista = (Motorista) session.getAttribute("motoristaLogado");

        if (motorista == null) {
            model.addAttribute("error", "Motorista não autenticado.");
            return "redirect:/login";
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);
        if (viagem != null) {
            if (viagem.getMotorista() != null && viagem.getMotorista().getId().equals(motorista.getId())) {
                viagem.setStatus("Disponível");
                viagem.setMotorista(null);
                viagemService.salvarViagem(viagem);

                // Busca a lista de motoristas disponíveis e redireciona para a tela de "realizar-viagem"
                Long usuarioId = (Long) session.getAttribute("usuarioId");
                if (usuarioId != null) {
                    PessoaComDeficiencia pessoaLogada = pessoaComDeficienciaService.findByIdWithDeficiencias(usuarioId);
                    if (pessoaLogada != null) {
                        List<Motorista> motoristasDisponiveis = motoristaService.listarMotoristasCompatíveis(pessoaLogada);
                        model.addAttribute("pessoa", pessoaLogada);
                        model.addAttribute("motoristasDisponiveis", motoristasDisponiveis);
                        return "realizar-viagem";
                    }
                }
            } else {
                model.addAttribute("error", "Motorista não associado a esta viagem.");
            }
        } else {
            model.addAttribute("error", "Viagem não encontrada.");
        }

        return "redirect:/visualizar-viagens";
    }

    @PostMapping("/corridas/{id}/confirmar-finalizacao")
    public ResponseEntity<String> confirmarFinalizacao(@PathVariable Long id,
                                                       @RequestParam boolean confirmacao,
                                                       HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        Viagem viagem = viagemService.buscarViagemPorId(id);

        if (viagem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        }

        if (confirmacao) {
            viagem.setStatus("Finalizada");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem finalizada com sucesso!");
        } else {
            viagem.setStatus("Em andamento");
            viagemService.salvarViagem(viagem);
            return ResponseEntity.ok("Viagem continuará em andamento");
        }
    }
}
