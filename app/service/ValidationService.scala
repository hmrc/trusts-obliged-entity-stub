/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.networknt.schema.{Error, InputFormat, Schema, SchemaRegistry, SpecificationVersion}
import play.api.Logging
import models.{DesValidationError, FailedValidation, SuccessfulValidation, ValidationResult}

import java.io.InputStream
import scala.io.Source
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.util.Using

class ValidationService() {

  def get(schemaFile: String): Validator = {
    val resource = resourceAsString(schemaFile)
      .getOrElse(throw new RuntimeException("Missing schema: " + schemaFile))

    val schema = SchemaRegistry
      .withDefaultDialect(SpecificationVersion.DRAFT_4)
      .getSchema(resource)

    new Validator(schema)
  }

  private def resourceAsString(resourcePath: String): Option[String] =
    resourceAsInputStream(resourcePath).flatMap { is =>
      Using(Source.fromInputStream(is))(_.getLines().mkString("\n")).toOption
    }

  private def resourceAsInputStream(resourcePath: String): Option[InputStream] =
    Option(getClass.getResourceAsStream(resourcePath))

}

class Validator(schema: Schema) extends Logging {

  def validateAgainstSchema(input: String): ValidationResult =
    try {
      val json: JsonNode                = toJsonNodeWithoutDuplicates(input)
      val validationOutput: List[Error] = schema.validate(json.toString, InputFormat.JSON).asScala.toList

      if (validationOutput.isEmpty) {
        SuccessfulValidation
      } else {
        val validationErrors = getValidationErrors(validationOutput)
        val failedValidation = FailedValidation("Invalid Json", 0, validationErrors)
        logger.info(
          s"[validateAgainstSchema] Failed schema validation with validation errors: ${failedValidation.toString}"
        )
        failedValidation
      }
    } catch {
      case ex: Exception =>
        logger.error(s"[validateAgainstSchema] Error validating Json request against Schema: ${ex.getMessage}")
        FailedValidation("Not JSON", 0, Nil)
    }

  private def getValidationErrors(validationOutput: List[Error]): Seq[DesValidationError] =
    validationOutput.map { error =>
      val message  = error.getMessage
      val location = error.getInstanceLocation.toString
      logger.error(s"[getValidationErrors] Failed at location : $location")
      DesValidationError(message, if (location == "") "/" else location)
    }

  private def toJsonNodeWithoutDuplicates(jsonNodeAsString: String): JsonNode = {
    val objectMapper: ObjectMapper = new ObjectMapper()
    objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)
    objectMapper.readTree(jsonNodeAsString)
  }

}
