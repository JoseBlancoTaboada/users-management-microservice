package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;

public record DatabaseConfig(
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode) {
  private static final String URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?sslMode=%s&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  private static final String POSTGRES_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s?sslmode=%s";

  public String buildJdbcUrl() {
    return String.format(URL_TEMPLATE, host, port, databaseName, sslMode);
  }

  public String buildPostgresJdbcUrl() {
    return String.format(
        POSTGRES_URL_TEMPLATE, host, port, databaseName, toPostgresSslMode(sslMode));
  }

  /**
   * PostgreSQL usa otros nombres que MySQL para el modo SSL. Se aceptan ambos para que la misma
   * variable DB_SSL_MODE sirva con cualquiera de los dos motores.
   */
  static String toPostgresSslMode(final String mode) {
    final String normalized = mode == null ? "" : mode.trim().toLowerCase(Locale.ROOT);
    return switch (normalized) {
      case "", "disabled" -> "disable";
      case "preferred" -> "prefer";
      case "required" -> "require";
      case "verify_ca" -> "verify-ca";
      case "verify_identity" -> "verify-full";
      default -> normalized;
    };
  }
}
