package com.fintech.service.code.websocket;

import com.fintech.service.code.model.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TradeWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendTradeUpdate(Trade trade) {
        messagingTemplate.convertAndSend("/topic/trades/" + trade.getSymbol(), trade);
    }

}

