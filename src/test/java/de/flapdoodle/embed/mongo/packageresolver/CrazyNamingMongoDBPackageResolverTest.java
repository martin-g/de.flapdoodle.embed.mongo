package de.flapdoodle.embed.mongo.packageresolver;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.TestUtils;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.store.DistributionPackage;
import de.flapdoodle.os.CommonArchitecture;
import de.flapdoodle.os.OS;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CrazyNamingMongoDBPackageResolverTest {

  private final CrazyNamingMongoDBPackageResolver testee = new CrazyNamingMongoDBPackageResolver(Command.Mongo, distribution -> {
    throw new IllegalArgumentException("should not be called");
  });

  @Test
  public void matchKnownVersions() {
    assertThatDistributionPackageFor(Version.V4_2_13, OS.Windows, CommonArchitecture.X86_64)
            .matchesPath("https://fastdl.mongodb.org/win32/mongodb-win32-x86_64-2012plus-4.2.13.zip");

    assertThatDistributionPackageFor(Version.V4_0_12, OS.Windows, CommonArchitecture.X86_64)
            .matchesPath("https://fastdl.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-4.0.12.zip");
  }

  private WithDistributionPackage assertThatDistributionPackageFor(Version version, OS os, CommonArchitecture architecture) {
    return new WithDistributionPackage(testee.packageFor(TestUtils.distributionOf(version, os, architecture)));
  }

  private class WithDistributionPackage {
    private final DistributionPackage result;

    public WithDistributionPackage(DistributionPackage result) {
      this.result = result;
    }

    public void matchesPath(String path) {
      assertThat(result.archivePath()).isEqualTo(path);
    }
  }
}