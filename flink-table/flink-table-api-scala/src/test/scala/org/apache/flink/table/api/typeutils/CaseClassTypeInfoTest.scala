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
package org.apache.flink.table.api.typeutils

import org.apache.flink.api.common.serialization.SerializerConfig
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.common.typeutils.{TypeInformationTestBase, TypeSerializer}

/** Test for [[CaseClassTypeInfo]]. */
class CaseClassTypeInfoTest extends TypeInformationTestBase[CaseClassTypeInfo[_]] {

  override protected def getTestData: Array[CaseClassTypeInfo[_]] = Array(
    new CaseClassTypeInfo[(Int, String)](
      classOf[(Int, String)],
      Array(),
      Array(BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO),
      Array("_1", "_2")) {
      override def createSerializer(config: SerializerConfig): TypeSerializer[(Int, String)] = ???
    },
    new CaseClassTypeInfo[(Int, Boolean)](
      classOf[(Int, Boolean)],
      Array(),
      Array(BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.BOOLEAN_TYPE_INFO),
      Array("_1", "_2")) {
      override def createSerializer(config: SerializerConfig): TypeSerializer[(Int, Boolean)] = ???
    }
  )
}
