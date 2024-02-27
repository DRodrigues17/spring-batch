package com.drodrigues17.batch.repository;

import com.drodrigues17.batch.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
