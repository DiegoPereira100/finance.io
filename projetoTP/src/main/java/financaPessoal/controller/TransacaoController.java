package financaPessoal.controller;

import financaPessoal.DTOS.TransacaoRecordDTO;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.TransacaoRepository;
import financaPessoal.services.TransacaoService;
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
public class TransacaoController {
    @Autowired
    TransacaoRepository transacaoRepository;

    private final TransacaoService transacaoService;
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transacao")
    public ResponseEntity<TransacaoModel> salvarTransacao(@RequestBody @Valid TransacaoRecordDTO transacaoRecordDTO) {
        var transacaoModel = new TransacaoModel();
        BeanUtils.copyProperties(transacaoRecordDTO, transacaoModel);
        TransacaoModel transacaoSalva = transacaoService.adicionarTransacao(transacaoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoSalva);
    }

    @GetMapping("/transacao")
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

    @GetMapping("/transacao/{id}")
    public ResponseEntity<Object> getOneTransacao(@PathVariable(value = "id") UUID id){
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if(transacao0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transacao0.get());
    }

    @PutMapping("/transacao/{id}")
    public ResponseEntity<Object> updateTransacao(@PathVariable(value = "id") UUID id,
                                                    @RequestBody TransacaoRecordDTO transacaoRecordDTO){
    Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
    if(transacao0.isEmpty()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
    }
    var transacaoModel = transacao0.get();
        transacaoModel.setDescricao(transacaoRecordDTO.descricao());
    return ResponseEntity.status(HttpStatus.OK).body(transacaoRepository.save(transacaoModel));
    }
    @DeleteMapping("/transacao/{id}")
    public ResponseEntity<Object> deleteTransacao(@PathVariable(value = "id") UUID id){
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if(transacao0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        transacaoRepository.delete(transacao0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Transação exluida com sucesso");
        }
}
