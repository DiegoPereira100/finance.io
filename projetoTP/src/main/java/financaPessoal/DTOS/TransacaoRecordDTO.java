package financaPessoal.DTOS;

import jakarta.validation.constraints.NotNull;

public record TransacaoRecordDTO(java.util.UUID idTransacao, String descricao, @NotNull double valor, @NotNull String tipo, String data, String nomeConta){

}
