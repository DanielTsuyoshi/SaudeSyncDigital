package com.saudesync.repository;

import com.saudesync.model.DadoSaude;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadoSaudeRepository extends JpaRepository<DadoSaude, Long> {
}
