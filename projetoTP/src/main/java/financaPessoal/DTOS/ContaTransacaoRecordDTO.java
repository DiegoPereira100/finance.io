package financaPessoal.DTOS;

import financaPessoal.models.ContaModel;
import financaPessoal.models.TransacaoModel;

public record ContaTransacaoRecordDTO(ContaModel conta, TransacaoModel transacao) {
}
