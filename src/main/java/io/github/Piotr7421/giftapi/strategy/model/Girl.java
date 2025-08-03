package io.github.Piotr7421.giftapi.strategy.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import io.github.Piotr7421.giftapi.model.Kid;

@Entity
@DiscriminatorValue("GIRL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Girl extends Kid {

    private String skirtColor;
}
