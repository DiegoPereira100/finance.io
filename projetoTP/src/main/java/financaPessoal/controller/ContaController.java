package financaPessoal.controller;

import financaPessoal.DTOS.ContaRecordDTO;
import financaPessoal.models.ContaModel;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.ContaRepository;
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

@RestController
@Tag(name = "Conta Controller", description = "Gerenciamento de contas e transações")
public class ContaController {

    @Autowired
    ContaRepository contaRepository;

    @Operation(summary = "Salvar uma nova conta", description = "Cria uma nova conta no sistema.")
    @PostMapping("/conta")
    public ResponseEntity<ContaModel> salvarConta(
            @RequestBody @Valid ContaRecordDTO contaRecordDTO) {
        var contaModel = new ContaModel();
        BeanUtils.copyProperties(contaRecordDTO, contaModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaRepository.save(contaModel));
    }

    @Operation(summary = "Listar todas as contas", description = "Retorna uma lista de todas as contas cadastradas.")
    @GetMapping("/conta")
    public ResponseEntity<List<ContaModel>> getAllContas() {
        return ResponseEntity.status(HttpStatus.OK).body(contaRepository.findAll());
    }

    @Operation(summary = "Buscar uma conta por ID", description = "Retorna os detalhes de uma conta específica.")
    @GetMapping("/conta/{id}")
    public ResponseEntity<Object> getOneConta(
            @Parameter(description = "ID da conta a ser buscada") @PathVariable(value = "id") UUID id) {
        Optional<ContaModel> conta0 = contaRepository.findById(id);
        if (conta0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(conta0.get());
    }

    @Operation(summary = "Atualizar uma conta", description = "Atualiza as informações de uma conta existente.")
    @PutMapping("/conta/{id}")
    public ResponseEntity<Object> updateConta(
            @Parameter(description = "ID da conta a ser atualizada") @PathVariable(value = "id") UUID id,
            @RequestBody @Valid ContaRecordDTO contaRecordDTO) {
        Optional<ContaModel> contaO = contaRepository.findById(id);
        if (contaO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }
        var contaModel = contaO.get();
        BeanUtils.copyProperties(contaRecordDTO, contaModel);
        return ResponseEntity.status(HttpStatus.OK).body(contaRepository.save(contaModel));
    }

    @Operation(summary = "Excluir uma conta", description = "Remove uma conta do sistema.")
    @DeleteMapping("/conta/{id}")
    public ResponseEntity<Object> deleteConta(
            @Parameter(description = "ID da conta a ser excluída") @PathVariable(value = "id") UUID id) {
        Optional<ContaModel> contaOptional = contaRepository.findById(id);
        if (contaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        contaRepository.delete(contaOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Conta excluída com sucesso");
    }

    @Operation(summary = "Adicionar transação a uma conta", description = "Adiciona uma transação (receita ou despesa) a uma conta específica.")
    @PostMapping("/conta/{id}/transacao")
    public ResponseEntity<Object> addTransacao(
            @Parameter(description = "ID da conta onde será adicionada a transação") @PathVariable UUID id,
            @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> contaOptional = contaRepository.findById(id);

        if (contaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
        }

        ContaModel conta = contaOptional.get();

        if (transacao.getTipo().equals("receita")) {
            conta.atualizarSaldo(transacao.getValor());
        } else if (transacao.getTipo().equals("despesa")) {
            conta.atualizarSaldo(-transacao.getValor());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de transação não aceita");
        }
        transacao.setConta(conta);
        conta.getTransacoes().add(transacao);

        contaRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transação adicionada com sucesso.");
    }
}
