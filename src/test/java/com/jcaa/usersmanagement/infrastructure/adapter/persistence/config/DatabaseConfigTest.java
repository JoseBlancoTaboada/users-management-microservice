package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DatabaseConfigTest {

  private static final String HOST = "mysql.example.com";
  private static final int PORT = 15425;
  private static final String DATABASE = "crud_usuarios";
  private static final String USERNAME = "avnadmin";
  private static final String PASSWORD = "secret";
  private static final String SSL_MODE = "REQUIRED";

  @Test
  void shouldBuildJdbcUrlWithConfiguredSslMode() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(HOST, PORT, DATABASE, USERNAME, PASSWORD, SSL_MODE);

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(jdbcUrl)
        .isEqualTo(
            "jdbc:mysql://mysql.example.com:15425/crud_usuarios"
                + "?sslMode=REQUIRED&serverTimezone=UTC&allowPublicKeyRetrieval=true");
  }

  @Test
  void shouldBuildPostgresJdbcUrlTranslatingMysqlSslModeNames() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig("db.example.com", 5432, "postgres", USERNAME, PASSWORD, "REQUIRED");

    // Act
    final String jdbcUrl = config.buildPostgresJdbcUrl();

    // Assert
    assertThat(jdbcUrl).isEqualTo("jdbc:postgresql://db.example.com:5432/postgres?sslmode=require");
  }

  @Test
  void shouldKeepNativePostgresSslModeNames() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig("db.example.com", 5432, "postgres", USERNAME, PASSWORD, "verify-full");

    // Act + Assert
    assertThat(config.buildPostgresJdbcUrl()).endsWith("?sslmode=verify-full");
  }

  @Test
  void shouldMapEachMysqlSslModeToItsPostgresEquivalent() {
    // Act + Assert
    assertThat(DatabaseConfig.toPostgresSslMode("DISABLED")).isEqualTo("disable");
    assertThat(DatabaseConfig.toPostgresSslMode("PREFERRED")).isEqualTo("prefer");
    assertThat(DatabaseConfig.toPostgresSslMode("REQUIRED")).isEqualTo("require");
    assertThat(DatabaseConfig.toPostgresSslMode("VERIFY_CA")).isEqualTo("verify-ca");
    assertThat(DatabaseConfig.toPostgresSslMode("VERIFY_IDENTITY")).isEqualTo("verify-full");
    assertThat(DatabaseConfig.toPostgresSslMode(null)).isEqualTo("disable");
  }
}
