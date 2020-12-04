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

  private val URN_TYPE = "URN"
  private val UTR_TYPE = "UTR"

  "getObligedEntity By Utr" should {
    "return OK with valid processed payload for 2134514321" in {
      testObligedEntitiesUtr("2134514321")
    }

    "return OK with valid processed payload for 1000000001" in {
      testObligedEntitiesUtr("1000000001")
    }

    "return OK with valid processed payload for 1000000002" in {
      testObligedEntitiesUtr("1000000002")
    }

    "return OK with valid processed payload for 1000000003" in {
      testObligedEntitiesUtr("1000000003")
    }

    "return OK with valid processed payload for 1000000007" in {
      testObligedEntitiesUtr("1000000007")
    }

    "return OK with valid processed payload for 1000000008" in {
      testObligedEntitiesUtr("1000000008")
    }

    "return OK with valid processed payload for 1000000010" in {
      testObligedEntitiesUtr("1000000010")
    }

    "return OK with valid processed payload for 1000000011" in {
      testObligedEntitiesUtr("1000000011")
    }

    "return OK with valid processed payload for 1000000101" in {
      testObligedEntitiesUtr("1000000101")
    }

    "return OK with valid processed payload for 1000000102" in {
      testObligedEntitiesUtr("1000000102")
    }

    "return OK with valid processed payload for 1000000103" in {
      testObligedEntitiesUtr("1000000103")
    }

    "return OK with valid processed payload for 5174384721" in {
      testObligedEntitiesUtr("5174384721")
    }

    "obliged entities not available for provided utr" in {
      val resultJson = getObligedEntitesAsJson("0000000404", UTR_TYPE, NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }
  }

  "getObligedEntity By Urn" should {
    "return OK with valid processed payload for 0000000001AAAAA" in {
      testObligedEntitiesUrn("0000000001AAAAA")
    }

    "return OK with valid processed payload for 0000000002AAAAA" in {
      testObligedEntitiesUrn("0000000002AAAAA")
    }

    "return OK with valid processed payload for 0000000003AAAAA" in {
      testObligedEntitiesUrn("0000000003AAAAA")
    }

    "return OK with valid processed payload for 0000000004AAAAA" in {
      testObligedEntitiesUrn("0000000004AAAAA")
    }

    "return OK with valid processed payload for 1234567890AAAAA" in {
      testObligedEntitiesUrn("1234567890AAAAA")
    }

    "obliged entities not available for provided urn" in {
      val resultJson = getObligedEntitesAsJson("0000000404AAAAA", URN_TYPE, NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }
  }

 "getObligedEntity Failure" should {

    "return Internal Server Error when des having internal errors" in {
      val resultJson = getObligedEntitesAsJson("0000000500", UTR_TYPE, INTERNAL_SERVER_ERROR)

      (resultJson \ "code").as[String] mustBe "SERVER_ERROR"
      (resultJson \ "reason").as[String] mustBe "IF is currently experiencing problems that require live service intervention"
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val resultJson = getObligedEntitesAsJson("0000000503", UTR_TYPE, SERVICE_UNAVAILABLE)

      (resultJson \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (resultJson \ "reason").as[String] mustBe "Dependent systems are currently not responding"
    }

  }

  private def getObligedEntitesAsJson(id: String, idType: String, expectedResult: Int): JsValue = {
    val request = createGetRequestWithValidHeaders(s"/trusts/obliged-entities/$idType/$id")
    val result = SUT.getObligedEntity(id).apply(request)
    status(result) must be(expectedResult)
    contentType(result).get mustBe "application/json"
    contentAsJson(result)
  }

  private def getObligedEntitesAsValidatedJson(id: String, idType: String): JsValue = {
    val resultJson = getObligedEntitesAsJson(id, idType, OK)

    val validationResult = obligedEntitiesValidator.validateAgainstSchema(resultJson.toString)
    validationResult mustBe SuccessfulValidation

    resultJson
  }

  private def testObligedEntitiesUtr(utr: String) = {
    val resultJson = getObligedEntitesAsValidatedJson(utr, UTR_TYPE)

    (resultJson \ "identifiers" \ "utr").as[String] mustBe utr
  }

  private def testObligedEntitiesUrn(urn: String) = {
    val resultJson = getObligedEntitesAsValidatedJson(urn, URN_TYPE)

    (resultJson \ "identifiers" \ "urn").as[String] mustBe urn
  }
}
