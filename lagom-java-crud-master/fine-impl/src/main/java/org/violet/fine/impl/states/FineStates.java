package org.violet.fine.impl.states;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.fine.api.Fine;

import java.util.Optional;

@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class FineStates implements CompressedJsonable {
    Optional<Fine> fine;
    String timestamp;
}
