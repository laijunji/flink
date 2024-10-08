/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.client.config;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.docs.Documentation;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.table.client.cli.parser.SyntaxHighlightStyle;

/** Options used in sql client. */
@PublicEvolving
public class SqlClientOptions {
    private SqlClientOptions() {}

    // Execution options

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH_STREAMING)
    public static final ConfigOption<Integer> EXECUTION_MAX_TABLE_RESULT_ROWS =
            ConfigOptions.key("sql-client.execution.max-table-result.rows")
                    .intType()
                    .defaultValue(1000_000)
                    .withDescription(
                            "The number of rows to cache when in the table mode. If the number of rows exceeds the "
                                    + "specified value, it retries the row in the FIFO style.");

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH_STREAMING)
    public static final ConfigOption<ResultMode> EXECUTION_RESULT_MODE =
            ConfigOptions.key("sql-client.execution.result-mode")
                    .enumType(ResultMode.class)
                    .defaultValue(ResultMode.TABLE)
                    .withDescription("Determines how the query result should be displayed.");

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH_STREAMING)
    public static final ConfigOption<Boolean> VERBOSE =
            ConfigOptions.key("sql-client.verbose")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "Determine whether to output the verbose output to the console. If set the option true, it will print the exception stack. Otherwise, it only output the cause.");

    // Display options

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH)
    public static final ConfigOption<Boolean> DISPLAY_QUERY_TIME_COST =
            ConfigOptions.key("sql-client.display.print-time-cost")
                    .booleanType()
                    .defaultValue(true)
                    .withDescription(
                            "Determine whether to display the time consumption of the query. By default, no query time cost will be displayed.");

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH_STREAMING)
    public static final ConfigOption<String> DISPLAY_DEFAULT_COLOR_SCHEMA =
            ConfigOptions.key("sql-client.display.color-schema")
                    .stringType()
                    .defaultValue(SyntaxHighlightStyle.BuiltInStyle.DEFAULT.name())
                    .withDescription(
                            "SQL highlight color schema to be used at SQL client. Possible values: 'default', 'dark', 'light', 'chester', 'vs2010', 'solarized', 'obsidian', 'geshi'");

    @Documentation.TableOption(execMode = Documentation.ExecMode.BATCH_STREAMING)
    public static final ConfigOption<Boolean> DISPLAY_SHOW_LINE_NUMBERS =
            ConfigOptions.key("sql-client.display.show-line-numbers")
                    .booleanType()
                    .defaultValue(Boolean.FALSE)
                    .withDescription(
                            "Determines whether there should be shown line numbers in multiline SQL or not.");
}
