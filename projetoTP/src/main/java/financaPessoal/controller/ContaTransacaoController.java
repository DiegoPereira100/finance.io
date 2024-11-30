package financaPessoal.controller;

import financaPessoal.DTOS.ContaTransacaoRecordDTO;
import financaPessoal.DTOS.TransacaoRecordDTO;
import financaPessoal.models.ContaTransacaoModel;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.ContaTransacaoRepository;
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
public class ContaTransacaoController {
    @Autowired
    ContaTransacaoRepository contaTransacaoRepository;
    @PostMapping("/contaTransacao")
    public ResponseEntity<ContaTransacaoModel> salvarContaTransacao(@RequestBody @Valid ContaTransacaoRecordDTO contaTransacaoRecordDTO){
        var contaTransacaoModel = new ContaTransacaoModel();
        BeanUtils.copyProperties(contaTransacaoRecordDTO, contaTransacaoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaTransacaoRepository.save(contaTransacaoModel));
    }
}
