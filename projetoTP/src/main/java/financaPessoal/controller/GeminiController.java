package financaPessoal.controller;

import financaPessoal.models.ContaModel;
import financaPessoal.repository.ContaRepository;
import financaPessoal.services.ConsomeAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conta")
@Tag(name = "Gemini Controller", description = "Controlador para análise financeira usando uma API externa.")
public class GeminiController {

    @Autowired
    private ContaRepository contaRepository;

    private final ConsomeAPI consomeAPI;

    @Autowired
    public GeminiController(ConsomeAPI consomeAPI) {
        this.consomeAPI = consomeAPI;
    }

    @Operation(
            summary = "Analisar todas as contas do usuário",
            description = "Gera uma análise financeira consolidada com base nas transações de todas as contas do usuário, identificando oportunidades de economia e pontos de melhoria financeira."
    )
    @PostMapping("/analise-geral")
    public ResponseEntity<Object> gemini() {
        try {
            // Busca todas as contas no banco de dados
            List<ContaModel> contas = contaRepository.findAll();

            if (contas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma conta encontrada.");
            }

            // Formata as transações de todas as contas em uma única string
            String transacoesFormatadas = contas.stream()
                    .flatMap(conta -> conta.getTransacoes().stream())
                    .map(transacao -> String.format(
                            "Descrição: %s, Tipo: %s, Valor: %.2f, Data: %s",
                            transacao.getDescricao(),
                            transacao.getTipo(),
                            transacao.getValor(),
                            transacao.getData()
                    ))
                    .collect(Collectors.joining("; "));

            // Gera a pergunta para a API usando as transações formatadas
            String pergunta = "forneça uma visão geral sobre como o usuário pode economizar, pontos de melhoria financeira, e hábitos que ele pode adotar: " + transacoesFormatadas + ", um detalhe importante é que deve ser um texto corrido, como um arquivo txt, não pode ter titulo, nem quebra de linha, nem nada de especial, apenas o texto em si";

            // Chama o serviço ConsomeAPI para fazer a requisição
            String resposta = ConsomeAPI.fazerPergunta(pergunta);

            String respostaFormatada = ConsomeAPI.formatarResposta(resposta);

            return ResponseEntity.status(HttpStatus.OK).body(respostaFormatada);

        } catch (IOException | InterruptedException e) {
            // Trata possíveis erros durante a chamada à API
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao consumir a API externa: " + e.getMessage());
        }
    }
}
