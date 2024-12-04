package financaPessoal.controller;

import financaPessoal.DTOS.ContaRecordDTO;
import financaPessoal.DTOS.TransacaoRecordDTO;
import financaPessoal.models.ContaModel;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.ContaRepository;
import financaPessoal.repository.TransacaoRepository;
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
public class ContaController {
    @Autowired
    ContaRepository contaRepository;

    @PostMapping("/conta")
    public ResponseEntity<ContaModel> salvarConta(@RequestBody @Valid ContaRecordDTO contaRecordDTO){
        var contaModel = new ContaModel();
        BeanUtils.copyProperties(contaRecordDTO, contaModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaRepository.save(contaModel));
    }
    @GetMapping("/conta")
    public ResponseEntity<List<ContaModel>> getAllContas(){
        return ResponseEntity.status(HttpStatus.OK).body(contaRepository.findAll());
    }
    @GetMapping("/conta/{id}")
    public ResponseEntity<Object> getOneConta(@PathVariable(value = "id") UUID id){
        Optional<ContaModel> conta0 = contaRepository.findById(id);
        if(conta0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(conta0.get());
    }
    @PutMapping("/conta/{id}")
    public ResponseEntity<Object> updateConta(@PathVariable(value = "id") UUID id,
                                              @RequestBody @Valid ContaRecordDTO contaRecordDTO) {
        Optional<ContaModel> contaO = contaRepository.findById(id);
        if (contaO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }
        var contaModel = contaO.get();
        BeanUtils.copyProperties(contaRecordDTO, contaModel);
        return ResponseEntity.status(HttpStatus.OK).body(contaRepository.save(contaModel));
    }

    @DeleteMapping("/conta/{id}")
    public ResponseEntity<Object> deleteConta(@PathVariable(value = "id") UUID id) {
        Optional<ContaModel> contaOptional = contaRepository.findById(id);
        if (contaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        contaRepository.delete(contaOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Conta excluída com sucesso");
    }
    @PostMapping("/conta/{id}/transacao")
    public ResponseEntity<Object> addTransacao(@PathVariable UUID id, @RequestBody TransacaoModel transacao) {
        Optional<ContaModel> contaOptional = contaRepository.findById(id);

        if (contaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
        }

        ContaModel conta = contaOptional.get();

        if(transacao.getTipo().equals("receita")) {
            conta.atualizarSaldo(transacao.getValor());
        }
       else if(transacao.getTipo().equals("despesa")){
            conta.atualizarSaldo(-transacao.getValor());
        }
       else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de transação não aceita");
        }
        transacao.setConta(conta);
        conta.getTransacoes().add(transacao);

        contaRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transação adicionada com sucesso.");
    }
}
