package financaPessoal.controller;

import financaPessoal.models.ContaModel;
import financaPessoal.repository.ContaRepository;
import financaPessoal.services.ConsomeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conta")
public class GeminiController {

    @Autowired
    private ContaRepository contaRepository;

    private final ConsomeAPI consomeAPI;

    @Autowired
    public GeminiController(ConsomeAPI consomeAPI) {
        this.consomeAPI = consomeAPI;
    }

    /**
     * Endpoint para analisar as transações de uma conta
     * @param id Identificador da conta
     * @return Resposta da API externa com análise das transações
     */
    @PostMapping("/{id}/analise")
    public ResponseEntity<Object> gemini(@PathVariable UUID id) {
        try {
            // Verifica se a conta existe no banco
            Optional<ContaModel> contaOptional = contaRepository.findById(id);
            if (contaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
            }

            ContaModel conta = contaOptional.get();

            // Formata as transações da conta em uma string legível
            String transacoesFormatadas = conta.getTransacoes()
                    .stream()
                    .map(transacao -> String.format(
                            "Descrição: %s, Tipo: %s, Valor: %.2f, Data: %s",
                            transacao.getDescricao(),
                            transacao.getTipo(),
                            transacao.getValor(),
                            transacao.getData()
                    ))
                    .collect(Collectors.joining("; "));

            // Gera a pergunta para a API usando as transações formatadas
            String pergunta = "Faça uma analise de como esse usuario poderia economizar, o quanto ele gastou e o que ele poderia melhorar na questão financeira: " + transacoesFormatadas;

            // Chama o service ConsomeAPI para fazer a requisição
            String resposta = ConsomeAPI.fazerPergunta(pergunta);

            // Retorna a resposta da API
            return ResponseEntity.status(HttpStatus.OK).body(resposta);

        } catch (IOException | InterruptedException e) {
            // Trata possíveis erros durante a chamada à API
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao consumir a API externa: " + e.getMessage());
        }
    }
}

