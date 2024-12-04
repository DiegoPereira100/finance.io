package financaPessoal.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "Transacao")
public class TransacaoModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTransacao;
    private String descricao;
    private double valor;
    private String tipo;
    private String data;
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    @JsonBackReference
    private ContaModel conta;

    public ContaModel getConta() {
        return conta;
    }

    public void setConta(ContaModel conta) {
        this.conta = conta;
    }

    public UUID getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(UUID idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void atualizarSaldo(double valor, String tipo){
        if(tipo.equals("despesa")) {
            this.conta.atualizarSaldo(-valor);
        }
        else{
            this.conta.atualizarSaldo(valor);
        }
    }
}
