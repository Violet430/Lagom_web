package org.violet.illegal.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.illegal.api.Illegal;

import java.util.Optional;
@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class IllegalStates implements CompressedJsonable {
    Optional<Illegal> illegal;
    String timestamp;
}
