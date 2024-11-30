package financaPessoal.controller;

import financaPessoal.DTOS.TransacaoRecordDTO;
import financaPessoal.models.TransacaoModel;
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
public class TransacaoController {
    @Autowired
    TransacaoRepository transacaoRepository;

    @PostMapping("/transacao")
    public ResponseEntity<TransacaoModel> salvarTransacao(@RequestBody @Valid TransacaoRecordDTO transacaoRecordDTO){
        var transacaoModel = new TransacaoModel();
        BeanUtils.copyProperties(transacaoRecordDTO, transacaoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoRepository.save(transacaoModel));
    }

    @GetMapping("/transacao")
    public ResponseEntity<List<TransacaoModel>> getAllTransacao(){
        return ResponseEntity.status(HttpStatus.OK).body(transacaoRepository.findAll());
    }

    @GetMapping("/transacao/{id}")
    public ResponseEntity<Object> getOneTransacao(@PathVariable(value = "id") UUID id){
        Optional<TransacaoModel> transacao0 = transacaoRepository.findById(id);
        if(transacao0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transacao0.get());
    }
}
