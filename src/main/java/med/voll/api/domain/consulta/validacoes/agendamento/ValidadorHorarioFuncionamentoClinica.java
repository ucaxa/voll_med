package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsultas {

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour()<7;
        var depoisDoFechamentoDClinica = dataConsulta.getHour()>18;
        if (domingo || antesDaAberturaDaClinica || depoisDoFechamentoDClinica){
            throw new ValidacaoException("Consulta não pode ser marcada fora do expediente");
        }
    }
}
