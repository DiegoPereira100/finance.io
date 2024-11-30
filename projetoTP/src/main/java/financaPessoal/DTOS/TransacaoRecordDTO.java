package financaPessoal.DTOS;

import jakarta.validation.constraints.NotNull;

public record TransacaoRecordDTO(String descricao, @NotNull double valor, @NotNull String tipo, String data) {

}
