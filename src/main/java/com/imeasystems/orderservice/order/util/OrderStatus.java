package com.imeasystems.orderservice.order.util;

public enum OrderStatus {

    /**
     * Order has been created but not yet processed
     */
    PENDING,

    /**
     * Payment is being processed
     */
    PROCESSING,

    /**
     * Payment has been successfully processed
     */
    PAID,

    /**
     * Order has been shipped
     */
    SHIPPED,

    /**
     * Order has been successfully delivered
     */
    DELIVERED,

    /**
     * Order has been canceled
     */
    CANCELED,

    /**
     * Payment has been refunded
     */
    REFUNDED;
}
