package org.violet.driver.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.driver.api.Driver;

import java.util.Optional;

@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class DriverStates implements CompressedJsonable {
    Optional<Driver> driver;
    String timestamp;
}
