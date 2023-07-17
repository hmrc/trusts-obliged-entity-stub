/*
 * Copyright 2023 HM Revenue & Customs
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

import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.LogLevel.ERROR
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.Logging
import models.{DesValidationError, FailedValidation, SuccessfulValidation, ValidationResult}

import scala.io.Source
import scala.jdk.CollectionConverters.IterableHasAsScala

class ValidationService () {

  private val factory = JsonSchemaFactory.byDefault()

  def get(schemaFile: String): Validator = {
    val source = Source.fromFile(getClass.getResource(schemaFile).getPath)
    val schemaJsonFileString = source.mkString
    source.close()
    val schemaJson = JsonLoader.fromString(schemaJsonFileString)
    val schema = factory.getJsonSchema(schemaJson)
    new Validator(schema)
  }

}

class Validator(schema: JsonSchema) extends Logging {

  private val jsonErrorMessageTag = "message"
  private val jsonErrorInstanceTag = "instance"
  private val jsonErrorPointerTag = "pointer"

  def validateAgainstSchema(input: String): ValidationResult = {

    try {
      val json: JsonNode = doNotAllowDuplicatedProperties(input)

      val validationOutput: ProcessingReport = schema.validate(json, true)

      if (validationOutput.isSuccess) {
        SuccessfulValidation
      } else {
        val validationErrors = getValidationErrors(validationOutput)
        val failedValidation = FailedValidation("Invalid Json", 0, validationErrors)
        logger.info(s"[validateAgainstSchema] Failed schema validation with validation errors: ${failedValidation.toString}")
        failedValidation
      }
    }
    catch {
      case ex: Exception =>
        logger.error(s"[validateAgainstSchema] Error validating Json request against Schema: ${ex.getMessage}")
        FailedValidation("Not JSON", 0, Nil)
    }
  }


  private def getValidationErrors(validationOutput: ProcessingReport): Seq[DesValidationError] = {
    validationOutput.asScala.toList.filter(m => m.getLogLevel == ERROR).map(m => {
      val error = m.asJson()
      val message = error.findValue(jsonErrorMessageTag).asText("")
      val location = error.findValue(jsonErrorInstanceTag).at(s"/$jsonErrorPointerTag").asText()
      val locations = error.findValues(jsonErrorInstanceTag)
      logger.error(s"[getValidationErrors] Failed at locations : $locations")
      DesValidationError(message, if (location == "") "/" else location)
    })
  }

  private def doNotAllowDuplicatedProperties(jsonNodeAsString: String): JsonNode = {
    val objectMapper: ObjectMapper = new ObjectMapper()
    objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)

    val jsonFactory: JsonFactory = objectMapper.getFactory
    val jsonParser: JsonParser = jsonFactory.createParser(jsonNodeAsString)

    objectMapper.readTree(jsonParser)

    JsonLoader.fromString(jsonNodeAsString)
  }
}
