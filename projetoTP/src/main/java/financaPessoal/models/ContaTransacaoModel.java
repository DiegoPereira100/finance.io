package financaPessoal.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "Conta-transacao")
public class ContaTransacaoModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idContaTransacao;
    private ContaModel conta;
    private TransacaoModel transacao;

    public UUID getIdContaTransacao() {
        return idContaTransacao;
    }

    public void setIdContaTransacao(UUID idContaTransacao) {
        this.idContaTransacao = idContaTransacao;
    }

    public ContaModel getConta() {
        return conta;
    }

    public void setConta(ContaModel conta) {
        this.conta = conta;
    }

    public TransacaoModel getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoModel transacao) {
        this.transacao = transacao;
    }
}
