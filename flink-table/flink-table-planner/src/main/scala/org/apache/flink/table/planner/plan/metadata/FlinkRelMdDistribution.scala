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
package org.apache.flink.table.planner.plan.metadata

import org.apache.flink.table.planner.JHashMap
import org.apache.flink.table.planner.plan.`trait`.{FlinkRelDistribution, FlinkRelDistributionTraitDef}
import org.apache.flink.table.planner.plan.metadata.FlinkMetadata.FlinkDistribution

import org.apache.calcite.rel._
import org.apache.calcite.rel.core.{Calc, Exchange, Sort, TableScan}
import org.apache.calcite.rel.metadata._
import org.apache.calcite.rex.RexInputRef
import org.apache.calcite.util.mapping.Mappings

import scala.collection.JavaConversions._

/**
 * FlinkRelMdDistribution supplies a default implementation of
 * [[FlinkRelMetadataQuery.flinkDistribution]] for the standard logical algebra.
 */
class FlinkRelMdDistribution private extends MetadataHandler[FlinkDistribution] {

  override def getDef: MetadataDef[FlinkDistribution] = FlinkDistribution.DEF

  def flinkDistribution(scan: TableScan, mq: RelMetadataQuery): FlinkRelDistribution = {
    val statsDistribution = scan.getTable.getDistribution
    if (
      statsDistribution != null &&
      statsDistribution.getTraitDef.equals(FlinkRelDistributionTraitDef.INSTANCE)
    ) {
      statsDistribution.asInstanceOf[FlinkRelDistribution]
    } else {
      getFlinkDistribution(scan)
    }
  }

  def flinkDistribution(calc: Calc, mq: RelMetadataQuery): FlinkRelDistribution = {
    val fmq = FlinkRelMetadataQuery.reuseOrCreate(mq)
    val input = calc.getInput
    val distribution = fmq.flinkDistribution(input)
    val mapInToOutPos = new JHashMap[Integer, Integer]
    val projects = calc.getProgram.getProjectList.map(calc.getProgram.expandLocalRef)
    // Build an input to output position map.
    projects.zipWithIndex.foreach {
      case (project, idx) =>
        project match {
          case ref: RexInputRef => mapInToOutPos.put(ref.getIndex, idx)
          case _ => // ignore
        }
    }
    // FIXME transmit one possible distribution.
    // for example "select a, a, sum(b) group by a"， here only transmit hash[1], not hash[0].
    val mapping =
      Mappings.target(mapInToOutPos, input.getRowType.getFieldCount, calc.getRowType.getFieldCount)
    distribution.apply(mapping)
  }

  def flinkDistribution(sort: Sort, mq: RelMetadataQuery): FlinkRelDistribution = {
    FlinkRelDistribution.SINGLETON
  }

  def flinkDistribution(exchange: Exchange, mq: RelMetadataQuery): FlinkRelDistribution = {
    val distribution = exchange.getDistribution
    if (
      distribution != null &&
      distribution.getTraitDef.equals(FlinkRelDistributionTraitDef.INSTANCE)
    ) {
      distribution.asInstanceOf[FlinkRelDistribution]
    } else {
      getFlinkDistribution(exchange)
    }
  }

  private def getFlinkDistribution(relNode: RelNode): FlinkRelDistribution = {
    relNode.getTraitSet.getTrait(FlinkRelDistributionTraitDef.INSTANCE)
  }

  def flinkDistribution(rel: RelNode, mq: RelMetadataQuery): FlinkRelDistribution = {
    getFlinkDistribution(rel)
  }
}

object FlinkRelMdDistribution {

  private val INSTANCE = new FlinkRelMdDistribution

  val SOURCE: RelMetadataProvider =
    ReflectiveRelMetadataProvider.reflectiveSource(FlinkMetadata.FlinkDistribution.METHOD, INSTANCE)

}
