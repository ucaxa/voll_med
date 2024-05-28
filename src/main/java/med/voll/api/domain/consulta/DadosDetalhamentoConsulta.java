package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
    public DadosDetalhamentoConsulta(Consulta consultaNova) {
        this(consultaNova.getId(), consultaNova.getMedico().getId(),consultaNova.getPaciente().getId(),consultaNova.getData());
    }
}
