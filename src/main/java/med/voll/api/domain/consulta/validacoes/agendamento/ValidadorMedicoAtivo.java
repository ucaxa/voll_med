package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsultas {

    @Autowired
    MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados){

        if (dados.idMedico()==null){
            return;
        }

        var medico = medicoRepository.getReferenceById(dados.idMedico());


        if (medico.getAtivo()!=true){
            throw new ValidacaoException("Consulta não pode ser agdendada para um médico inativo");
        }
    }
}
