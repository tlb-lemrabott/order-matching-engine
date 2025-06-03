package com.fintech.service.code.repository;

import com.fintech.service.code.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<Trade> findTopBySymbolOrderByTimestampDesc(String symbol);

    @Query(value = "SELECT * FROM trades WHERE symbol = :symbol ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<Trade> findTopNBySymbolOrderByTimestampDesc(@Param("symbol") String symbol, @Param("limit") int limit);


}
