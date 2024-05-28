package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteInativo implements ValidadorAgendamentoDeConsultas {

    @Autowired
    PacienteRepository pacienteRepository;

    public void validar(DadosAgendamentoConsulta dados){
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());


        if (paciente.getAtivo()!=true){
            throw new ValidacaoException("Consulta não pode ser agdendada pra um paciente inativo");
        }
    }

}
