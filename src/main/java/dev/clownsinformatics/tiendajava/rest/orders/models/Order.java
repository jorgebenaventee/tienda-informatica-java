package dev.clownsinformatics.tiendajava.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("orders")
@TypeAlias("Order")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "The idUser is required")
    private Long idUser;

    @NotNull(message = "The client is required")
    private ClientResponse client;

    @NotNull(message = "The order should have at least one order line")
    private List<OrderLine> orderLines;

    @Builder.Default
    private Integer totalItems = 0;

    @Builder.Default
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isDeleted = false;

    @JsonProperty("id")
    public String get_id() {
        return this.id.toHexString();
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        this.totalItems = orderLines != null ? orderLines.size() : 0;
        this.total = orderLines != null ? orderLines.stream().mapToDouble(OrderLine::getTotal).sum() : 0.0;
    }
}
