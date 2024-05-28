package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsultas;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//*A classe AgendaDEConsulta é uma classe de serviço
// Deve ser anotada com @Service para que o sprimg posso injeta-la e possamoos usar no controller
//Classes Service tem o objetivo de realizar regras de negócio e validações da aplicação

@Service
public class AgendaDeConsultas {

    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorAgendamentoDeConsultas> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsultas> validadoresCancelamento;


    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if (!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente não existe");
        }

        if ((dados.idMedico()!=null) &&   (!medicoRepository.existsById(dados.idMedico()))){
            throw new ValidacaoException("Id do médico é nulo ou não existe");
        }

        validadores.forEach(v-> v.validar(dados));

        //medico escolher medico foi criado pq as escolha do médico é opcional 
        var medico = escolherMedico(dados);
        if (medico==null){
            throw new ValidacaoException("Não existe médico disponpivel nessa data");
        }

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        Consulta consultaNova = new Consulta(medico,paciente,dados.data());
        consultaRepository.save(consultaNova);
        return new DadosDetalhamentoConsulta(consultaNova);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {

        System.out.printf(" O id do médico é:           " + dados.idMedico()     +" -------------");
        if (dados.idMedico()!=null){
            return  medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade()==null){
            throw  new ValidacaoException("Especialidade é obrigatória quando o Médico não é informado");
        }
        return medicoRepository.escolherMedicoAleatoricoLivreNaData(dados.especialidade(),dados.data());
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

}
