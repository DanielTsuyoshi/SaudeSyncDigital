package com.saudesync.service;

import com.saudesync.model.ConsultaServico;
import com.saudesync.repository.ConsultaServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaServicoService {

    @Autowired
    private ConsultaServicoRepository consultaServicoRepository;

    public List<ConsultaServico> getAllConsultaServicos() {
        return consultaServicoRepository.findAll();
    }

    public Optional<ConsultaServico> getConsultaServicoById(Long id) {
        return consultaServicoRepository.findById(id);
    }

    public ConsultaServico saveConsultaServico(ConsultaServico consultaServico) {
        return consultaServicoRepository.save(consultaServico);
    }

    public void deleteConsultaServico(Long id) {
        consultaServicoRepository.deleteById(id);
    }
}
