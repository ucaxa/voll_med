package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

//Classe criada para implementa que um
@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsultas {


    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora,dataConsulta).toMinutes();

        if (diferencaEmMinutos<30){
            throw new ValidacaoException("Consulta deve ser agendada com antecedência minima de 30 minutos");
        }

        }

}
