/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers

import play.api.libs.json.JsValue
import play.api.mvc.ControllerComponents
import models.SuccessfulValidation
import play.api.test.Helpers._
import service.ValidationService

class ObligedEntitiesControllerSpec extends SpecBase {
  private implicit val cc: ControllerComponents = app.injector.instanceOf[ControllerComponents]
  private val headerValidatorAction: HeaderValidatorAction = app.injector.instanceOf[HeaderValidatorAction]

  private val obligedEntitiesSchema = "/resources/schemas/Response_Schema-v1.0.0.json"
  private val obligedEntitiesValidator = new ValidationService().get(obligedEntitiesSchema)

  private val SUT = app.injector.instanceOf[ObligedEntitiesController]

  "ObligedEntitiesController getObligedEntity" should {
    "return OK with valid processed payload for 2134514321" in {
      testObligedEntities("2134514321")
    }

    "obliged entities not available for provided utr" in {
      val resultJson = getObligedEntitesAsJson("0000000404", NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }


    "return Internal Server Error when des having internal errors" in {
      val resultJson = getObligedEntitesAsJson("0000000500", INTERNAL_SERVER_ERROR)

      (resultJson \ "code").as[String] mustBe "SERVER_ERROR"
      (resultJson \ "reason").as[String] mustBe "IF is currently experiencing problems that require live service intervention"
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val resultJson = getObligedEntitesAsJson("0000000503", SERVICE_UNAVAILABLE)

      (resultJson \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (resultJson \ "reason").as[String] mustBe "Dependent systems are currently not responding"
    }

  }

  private def getObligedEntitesAsJson(utr: String, expectedResult: Int): JsValue = {
    val request = createGetRequestWithValidHeaders(s"/trusts/obliged-entities/UTR/$utr")
    val result = SUT.getObligedEntity(utr).apply(request)
    status(result) must be(expectedResult)
    contentType(result).get mustBe "application/json"
    contentAsJson(result)
  }

  private def getObligedEntitesAsValidatedJson(utr: String): JsValue = {
    val resultJson = getObligedEntitesAsJson(utr, OK)

    val validationResult = obligedEntitiesValidator.validateAgainstSchema(resultJson.toString)
    validationResult mustBe SuccessfulValidation

    resultJson
  }

  private def testObligedEntities(utr: String) = {
    val resultJson = getObligedEntitesAsValidatedJson(utr)

    (resultJson \ "identifiers" \ "utr").as[String] mustBe utr
  }
}
