package financaPessoal.services;

import financaPessoal.models.ContaModel;
import financaPessoal.models.TransacaoModel;
import financaPessoal.repository.ContaRepository;
import financaPessoal.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public TransacaoModel adicionarTransacao(TransacaoModel transacao) {
        ContaModel conta = contaRepository.findById(transacao.getConta().getIdConta())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        double saldo = conta.getSaldo();
        if(transacao.getTipo().equals("despesa")) {
            conta.setSaldo(saldo -= transacao.getValor());
        } else if(transacao.getTipo().equals("receita")) {
            conta.setSaldo(saldo += transacao.getValor());
        } else {
            throw new IllegalArgumentException("Tipo de transação inválido: " + transacao.getTipo());
        }
        contaRepository.save(conta);
        return transacaoRepository.save(transacao);
    }
}
