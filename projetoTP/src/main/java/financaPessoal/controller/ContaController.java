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
}
