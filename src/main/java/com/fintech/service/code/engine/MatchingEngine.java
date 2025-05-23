package com.fintech.service.code.engine;


import com.fintech.service.code.model.Order;
import com.fintech.service.code.model.Trade;

import java.util.List;

public interface MatchingEngine {
    List<Trade> match(Order incomingOrder);
}