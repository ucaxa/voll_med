package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDePacienteComConsultaNaMesmaData implements ValidadorAgendamentoDeConsultas {

    @Autowired
    ConsultaRepository consultaRepository;

    public void validar(DadosAgendamentoConsulta dados){

      var primeiroHoario = dados.data().withHour(7);
      var ultimoHorario = dados.data().withHour(18);
      var pacienteComOutraConsultaNoDia = consultaRepository.existsByPacienteIdAndDataBetween(dados.idPaciente(),primeiroHoario,ultimoHorario);


        if (pacienteComOutraConsultaNoDia){
            throw new ValidacaoException("Paciente já Possui outra consulta agenda para a data solicitada");
        }
    }
}
