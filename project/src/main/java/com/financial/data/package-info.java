/**
 * Provides the data layer components for the financial application, focusing on
 * data ingestion and persistence.
 * <p>
 * This package is responsible for:
 * <ul>
 * <li><b>Data Ingestion:</b> Parsing standardized CSV files through {@code DataInput}
 * to populate the system with budget records.</li>
 * <li><b>Persistence:</b> Managing the SQLite database lifecycle, schema creation,
 * and CRUD operations for budget entries via {@code SQLiteManager}.</li>
 * <li><b>Synchronization:</b> Ensuring that in-memory objects and the physical
 * database remains consistent during mass imports or batch updates.</li>
 * </ul>
 */
package com.financial.data;
