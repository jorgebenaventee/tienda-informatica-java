package dev.clownsinformatics.tiendajava.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The id of the order", example = "60f0a9b9e8c9a72e8c7f0b1a")
    private ObjectId id = new ObjectId();

    @NotNull(message = "The idUser is required")
    @Schema(description = "The id of the user that made the order", example = "1")
    private Long idUser;

    @NotNull(message = "The client is required")
    @Schema(description = "The client that made the order", example = "{\"id\": 1, \"name\": \"Maria\"}")
    private ClientResponse client;

    @NotNull(message = "The order should have at least one order line")
    @Schema(description = "The order lines of the order", example = "[{\"idProduct\": 1, \"quantity\": 2}]")
    private List<OrderLine> orderLines;

    @Builder.Default
    @Schema(description = "The total items of the order", example = "2")
    private Integer totalItems = 0;

    @Builder.Default
    @Schema(description = "The total of the order", example = "100.0")
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default
    @Schema(description = "The date when the order was created", example = "2021-07-16T20:00:00.000Z")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    @Schema(description = "The date when the order was updated", example = "2021-07-16T20:00:00.000Z")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    @Schema(description = "If the order was deleted", example = "false")
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
