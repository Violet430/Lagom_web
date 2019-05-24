package org.violet.car.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.car.api.Car;

import java.util.Optional;

@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class CarStates implements CompressedJsonable {
    Optional<Car> car;
    String timestamp;
}
