package financaPessoal.repository;

import financaPessoal.models.TransacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository <TransacaoModel, UUID>{
}
