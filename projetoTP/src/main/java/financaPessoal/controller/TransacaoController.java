package financaPessoal.controller;

import financaPessoal.DTOS.TransacaoRecordDTO;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.TransacaoRepository;
import financaPessoal.services.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacao")
@Tag(name = "Transações", description = "Gerenciamento de transações financeiras")
public class TransacaoController {

    @Autowired
    TransacaoRepository transacaoRepository;

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    @Operation(summary = "Salvar uma nova transação", description = "Cria e salva uma nova transação financeira.")
    public ResponseEntity<TransacaoModel> salvarTransacao(
            @RequestBody @Valid @Parameter(description = "Dados da transação", required = true) TransacaoRecordDTO transacaoRecordDTO) {
        var transacaoModel = new TransacaoModel();
        BeanUtils.copyProperties(transacaoRecordDTO, transacaoModel);
        TransacaoModel transacaoSalva = transacaoService.adicionarTransacao(transacaoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoSalva);
    }

    @GetMapping
    @Operation(summary = "Listar todas as transações", description = "Retorna uma lista com todas as transações financeiras.")
    public ResponseEntity<List<TransacaoRecordDTO>> getAllTransacao() {
        List<TransacaoRecordDTO> transacoesDTO = transacaoRepository.findAll()
                .stream()
                .map(transacao -> new TransacaoRecordDTO(
                        transacao.getIdTransacao(),
                        transacao.getDescricao(),
                        transacao.getValor(),
                        transacao.getTipo(),
                        transacao.getData(),
                        transacao.getConta() != null ? transacao.getConta().getNome() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(transacoesDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma transação por ID", description = "Retorna os detalhes de uma transação específica pelo seu identificador.")
    public ResponseEntity<Object> getOneTransacao(
            @PathVariable(value = "id") @Parameter(description = "ID da transação", required = true) UUID id) {
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if (transacao0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transacao0.get());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma transação", description = "Atualiza os dados de uma transação financeira existente.")
    public ResponseEntity<Object> updateTransacao(
            @PathVariable(value = "id") @Parameter(description = "ID da transação", required = true) UUID id,
            @RequestBody @Valid @Parameter(description = "Dados atualizados da transação", required = true) TransacaoRecordDTO transacaoRecordDTO) {
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if (transacao0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        var transacaoModel = transacao0.get();
        transacaoModel.setDescricao(transacaoRecordDTO.descricao());
        return ResponseEntity.status(HttpStatus.OK).body(transacaoRepository.save(transacaoModel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma transação", description = "Remove uma transação financeira do sistema pelo seu identificador.")
    public ResponseEntity<Object> deleteTransacao(
            @PathVariable(value = "id") @Parameter(description = "ID da transação", required = true) UUID id) {
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if (transacao0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        transacaoRepository.delete(transacao0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Transação excluída com sucesso");
    }
}
