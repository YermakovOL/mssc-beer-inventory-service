package guru.sfg.beer.inventory.service.services.listeners;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocationService;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocationOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateRequestListener {
    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)

    public void getAllocateOrderRequest(AllocateOrderRequest allocateOrderRequest) {
        AllocationOrderResponse response = new AllocationOrderResponse();x

        try {
            Boolean successAllocation = allocationService.allocateOrder(allocateOrderRequest.getBeerOrderDto());

            response.setAllocationPending(!successAllocation);
        } catch (Exception e) {
            log.error("AllocationError:"+e.getMessage());
            response.setAllocationError(true);
        }
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE_RESPONSE, response);
    }
}
