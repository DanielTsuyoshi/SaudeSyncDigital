package com.saudesync.service;

import com.saudesync.model.ConsultaPaciente;
import com.saudesync.repository.ConsultaPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaPacienteService {

    @Autowired
    private ConsultaPacienteRepository consultaPacienteRepository;

    public List<ConsultaPaciente> getAllConsultasPacientes() {
        return consultaPacienteRepository.findAll();
    }

    public Optional<ConsultaPaciente> getConsultaPacienteById(Long id) {
        return consultaPacienteRepository.findById(id);
    }

    public ConsultaPaciente saveConsultaPaciente(ConsultaPaciente consultaPaciente) {
        return consultaPacienteRepository.save(consultaPaciente);
    }

    public void deleteConsultaPaciente(Long id) {
        consultaPacienteRepository.deleteById(id);
    }
}
