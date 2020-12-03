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

  private val displayTrustsSchema = "/resources/schemas/display-estates-3.0.json"
  private val displayValidator = new ValidationService().get(displayTrustsSchema)

  private val SUT = app.injector.instanceOf[ObligedEntitiesController]

  "ObligedEntitiesController getObligedEntity" should {

    "return OK with valid processed payload for 2000000000" in {
      testProcessedEstate("2000000000")
    }

  }

  private def getEstateAsJson(utr: String, expectedResult: Int): JsValue = {
    val request = createGetRequestWithValidHeaders(s"/trusts/obliged-entities/UTR/$utr")
    val result = SUT.getObligedEntity(utr).apply(request)
    status(result) must be(expectedResult)
    contentType(result).get mustBe "application/json"
    contentAsJson(result)
  }

  private def getEstateAsValidatedJson(utr: String): JsValue = {
    val resultJson = getEstateAsJson(utr, OK)

    val validationResult = displayValidator.validateAgainstSchema(resultJson.toString)
    validationResult mustBe SuccessfulValidation

    resultJson
  }

  private def testProcessedEstate(utr: String) = {
    val resultJson = getEstateAsValidatedJson(utr)

    (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Processed"
    (resultJson \ "trustOrEstateDisplay" \ "applicationType").as[String] mustBe "02"
    (resultJson \ "trustOrEstateDisplay" \ "matchData" \ "utr").as[String] mustBe utr
  }

  private def testReturnsOtherStatus(utr: String, status: String) = {
    val resultJson = getEstateAsJson(utr, OK)

    (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe status
    (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
  }
}
