package de.flapdoodle.embed.mongo.packageresolver;

import de.flapdoodle.os.*;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface PlatformMatch extends DistributionMatch {
  Optional<Version> version();
  Optional<CPUType> cpuType();
  Optional<BitSize> bitSize();
  Optional<OS> os();

  static ImmutablePlatformMatch.Builder builder() {
    return ImmutablePlatformMatch.builder();
  }

  static ImmutablePlatformMatch any() {
    return builder().build();
  }

  static ImmutablePlatformMatch withOs(OS os) {
    return any().withOs(os);
  }

  @Override
  @Value.Auxiliary
  default boolean match(de.flapdoodle.embed.process.distribution.Distribution distribution) {
    return match(this, distribution);
  }

  static boolean match(PlatformMatch match, de.flapdoodle.embed.process.distribution.Distribution distribution) {
    if (dontMatch(match.os(), distribution.platform().operatingSystem())) return false;
    if (dontMatch(match.cpuType(), distribution.platform().architecture().cpuType())) return false;
    if (dontMatch(match.bitSize(), distribution.platform().architecture().bitSize())) return false;
    if (dontMatch(match.version(), distribution.platform().version())) return false;
    return true;
  }

  static <T extends Enum<T>> boolean dontMatch(Optional<T> match, T value) {
    return match.isPresent() && match.get()!=value;
  }

  static <T> boolean dontMatch(Optional<T> match, Optional<T> value) {
    return match.isPresent() && value.isPresent() && !match.get().equals(value.get());
  }
}
