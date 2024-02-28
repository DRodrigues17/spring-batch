package com.drodrigues17.springbatch.repository;

import com.drodrigues17.springbatch.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
