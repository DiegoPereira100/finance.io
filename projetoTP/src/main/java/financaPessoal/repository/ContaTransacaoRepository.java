package financaPessoal.repository;

import financaPessoal.models.ContaTransacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContaTransacaoRepository extends JpaRepository<ContaTransacaoModel, UUID> {
}
