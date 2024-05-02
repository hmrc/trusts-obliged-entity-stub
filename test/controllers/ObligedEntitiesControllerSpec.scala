/*
 * Copyright 2024 HM Revenue & Customs
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

import models.{DesValidationError, FailedValidation, SuccessfulValidation}
import org.scalatest.matchers.must.Matchers._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers._
import service.ValidationService

import scala.concurrent.Future

class ObligedEntitiesControllerSpec extends SpecBase {

  private val obligedEntitiesSchema = "/resources/schemas/API1584_schema_1.2.0.json"
  private val obligedEntitiesValidator = new ValidationService().get(obligedEntitiesSchema)

  private val SUT = app.injector.instanceOf[ObligedEntitiesController]

  private val URN_TYPE = "URN"
  private val UTR_TYPE = "UTR"

  "getObligedEntity By Utr" should {
    "return OK with valid processed payload for 2134514321" in testObligedEntitiesUtr("2134514321")
    "return OK with valid processed payload for 1000000001" in testObligedEntitiesUtr("1000000001")
    "return OK with valid processed payload for 1000000002" in testObligedEntitiesUtr("1000000002")
    "return OK with valid processed payload for 1000000003" in testObligedEntitiesUtr("1000000003")
    "return OK with valid processed payload for 1000000005" in testObligedEntitiesUtr("1000000005")
    "return OK with valid processed payload for 1000000006" in testObligedEntitiesUtr("1000000006")
    "return OK with valid processed payload for 1000000007" in testObligedEntitiesUtr("1000000007")
    "return OK with valid processed payload for 1000000008" in testObligedEntitiesUtr("1000000008")
    "return OK with valid processed payload for 1000000009" in testObligedEntitiesUtr("1000000009")
    "return OK with valid processed payload for 1000000010" in testObligedEntitiesUtr("1000000010")
    "return OK with valid processed payload for 1000000011" in testObligedEntitiesUtr("1000000011")
    "return OK with valid processed payload for 1000000012" in testObligedEntitiesUtr("1000000012")
    "return OK with valid processed payload for 1000000013" in testObligedEntitiesUtr("1000000013")
    "return OK with valid processed payload for 1000000101" in testObligedEntitiesUtr("1000000101")
    "return OK with valid processed payload for 1000000102" in testObligedEntitiesUtr("1000000102")
    "return OK with valid processed payload for 1000000103" in testObligedEntitiesUtr("1000000103")
    "return OK with valid processed payload for 1234567890" in testObligedEntitiesUtr("1234567890")
    "return OK with valid processed payload for 1234567891" in testObligedEntitiesUtr("1234567891")
    "return OK with valid processed payload for 1234567892" in testObligedEntitiesUtr("1234567892")
    "return OK with valid processed payload for 1234567893" in testObligedEntitiesUtr("1234567893")
    "return OK with valid processed payload for 1234567894" in testObligedEntitiesUtr("1234567894")
    "return OK with valid processed payload for 1234567895" in testObligedEntitiesUtr("1234567895")
    "return OK with valid processed payload for 1234567896" in testObligedEntitiesUtr("1234567896")
    "return OK with valid processed payload for 1234567897" in testObligedEntitiesUtr("1234567897")
    "return OK with valid processed payload for 1234567898" in testObligedEntitiesUtr("1234567898")
    "return OK with valid processed payload for 1234567899" in testObligedEntitiesUtr("1234567899")
    "return OK with valid processed payload for 1274834715" in testObligedEntitiesUtr("1274834715")
    "return OK with valid processed payload for 3000000001" in testObligedEntitiesUtr("3000000001")
    "return OK with valid processed payload for 3000000002" in testObligedEntitiesUtr("3000000002")
    "return OK with valid processed payload for 3000000003" in testObligedEntitiesUtr("3000000003")
    "return OK with valid processed payload for 3000000004" in testObligedEntitiesUtr("3000000004")
    "return OK with valid processed payload for 3000000005" in testObligedEntitiesUtr("3000000005")
    "return OK with valid processed payload for 3000000006" in testObligedEntitiesUtr("3000000006")
    "return OK with valid processed payload for 3000000007" in testObligedEntitiesUtr("3000000007")
    "return OK with valid processed payload for 3000000008" in testObligedEntitiesUtr("3000000008")
    "return OK with valid processed payload for 3000000009" in testObligedEntitiesUtr("3000000009")
    "return OK with valid processed payload for 3000000010" in testObligedEntitiesUtr("3000000010")
    "return OK with valid processed payload for 3000000012" in testObligedEntitiesUtr("3000000012")
    "return OK with valid processed payload for 5174384721" in testObligedEntitiesUtr("5174384721")
    "return OK with valid processed payload for 5000000000" in testObligedEntitiesUtr("5000000000")

    "obliged entities not available for provided utr" in {
      val resultJson = getObligedEntitiesAsJson("0000000404", UTR_TYPE, NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return Bad Reguest for invalid id" in {
      val resultJson = getObligedEntitiesAsJson("0000000400AAAAA", UTR_TYPE, BAD_REQUEST)

      (resultJson \ "code").as[String] mustBe "INVALID_ID"
      (resultJson \ "reason").as[String] mustBe "Submission has not passed validation. Invalid parameter idValue."
    }

    "return a FailedValidation when given an invalid Json" in {
      val resultJson = Json.toJson(
        """{
          |"key":"invalid json"
          |}""".stripMargin
      )

      val validationResult = obligedEntitiesValidator.validateAgainstSchema(resultJson.toString())
      validationResult mustBe FailedValidation(
        "Invalid Json",
        0,
        List(
          DesValidationError("""instance type (string) does not match any allowed primitive type (allowed: ["object"])""", "/")
        )
      )
    }

    "return a FailedValidation when not given Json" in {
      val resultJson = "Invalid Json"

      val validationResult = obligedEntitiesValidator.validateAgainstSchema(resultJson)
      validationResult mustBe FailedValidation("Not JSON", 0, List())
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

    "return OK with valid processed payload for XATRUST80000001" in {
      testObligedEntitiesUrn("XATRUST80000001")
    }

    "return OK with valid processed payload for NTTRUST00000001" in {
      testObligedEntitiesUrn("NTTRUST00000001")
    }

    "obliged entities not available for provided urn" in {
      val resultJson = getObligedEntitiesAsJson("0000000404AAAAA", URN_TYPE, NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return Bad Reguest for invalid id" in {
      val resultJson = getObligedEntitiesAsJson("1000000001", URN_TYPE, BAD_REQUEST)

      (resultJson \ "code").as[String] mustBe "INVALID_ID"
      (resultJson \ "reason").as[String] mustBe "Submission has not passed validation. Invalid parameter idValue."
    }

  }

  "getObligedEntity Failure" should {
    "return Bad Reguest for invalid type" in {
      val resultJson = getObligedEntitiesAsJson("0000000400AAAAA", "XXXX", BAD_REQUEST)

      (resultJson \ "code").as[String] mustBe "INVALID_IDTYPE"
      (resultJson \ "reason").as[String] mustBe "Submission has not passed validation. Invalid parameter idType."
    }

    "return Unprocessable Entity Error when des having internal errors" in {
      val resultJson = getObligedEntitiesAsJson("0000000422", UTR_TYPE, UNPROCESSABLE_ENTITY)

      (resultJson \ "code").as[String] mustBe "BUSINESS_VALIDATION"
      (resultJson \ "reason").as[String] mustBe "The remote end point has indicated the request could not be processed."
    }


    "return Internal Server Error when des having internal errors" in {
      val resultJson = getObligedEntitiesAsJson("0000000500", UTR_TYPE, INTERNAL_SERVER_ERROR)

      (resultJson \ "code").as[String] mustBe "SERVER_ERROR"
      (resultJson \ "reason").as[String] mustBe "IF is currently experiencing problems that require live service intervention."
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val resultJson = getObligedEntitiesAsJson("0000000503", UTR_TYPE, SERVICE_UNAVAILABLE)

      (resultJson \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (resultJson \ "reason").as[String] mustBe "Dependent systems are currently not responding."
    }

    "return 403 forbidden when env header is not present" in {
      val result = getObligedEntitiesAsResponseWithoutHeader("0000000503", UTR_TYPE, ENVIRONMENT_HEADER)

      status(result) must be(FORBIDDEN)
    }

    "return 401 unauthorized when auth header is not present" in {
      val result = getObligedEntitiesAsResponseWithoutHeader("0000000503", UTR_TYPE, TOKEN_HEADER)

      status(result) must be(UNAUTHORIZED)
    }

    "return 403 forbidden when CID header is not present" in {
      val result = getObligedEntitiesAsResponseWithoutHeader("0000000503", UTR_TYPE, CORRELATIONID_HEADER)

      status(result) must be(FORBIDDEN)
    }

    "return 403 forbidden when env header is invalid" in {
      val result = getObligedEntitiesAsResponse("0000000503", UTR_TYPE, (ENVIRONMENT_HEADER, "XXX"))

      status(result) must be(FORBIDDEN)
    }

    "return 401 unauthorized when auth header is invalid" in {
      val result = getObligedEntitiesAsResponse("0000000503", UTR_TYPE, (TOKEN_HEADER, "XXX"))

      status(result) must be(UNAUTHORIZED)
    }

    "return 403 forbidden when CID header is invalid" in {
      val result = getObligedEntitiesAsResponse("0000000503", UTR_TYPE, (CORRELATIONID_HEADER, "XXX"))

      status(result) must be(FORBIDDEN)
    }

  }

  private def getObligedEntitiesAsJson(id: String, idType: String, expectedResult: Int): JsValue = {
    val request = createGetRequestWithHeaders(s"/trusts/obliged-entities/$idType/$id", validHeaders)
    val result = SUT.getObligedEntity(id, idType).apply(request)
    status(result) must be(expectedResult)
    contentType(result).get mustBe "application/json"
    contentAsJson(result)
  }

  private def getObligedEntitiesAsValidatedJson(id: String, idType: String): JsValue = {
    val resultJson = getObligedEntitiesAsJson(id, idType, OK)

    val validationResult = obligedEntitiesValidator.validateAgainstSchema(resultJson.toString)
    validationResult mustBe SuccessfulValidation

    resultJson
  }

  private def testObligedEntitiesUtr(utr: String) = {
    val resultJson = getObligedEntitiesAsValidatedJson(utr, UTR_TYPE)

    (resultJson \ "identifiers" \ "utr").as[String] mustBe utr
  }

  private def testObligedEntitiesUrn(urn: String) = {
    val resultJson = getObligedEntitiesAsValidatedJson(urn, URN_TYPE)

    (resultJson \ "identifiers" \ "urn").as[String] mustBe urn
  }

  private def getObligedEntitiesAsResponse(id: String, idType: String, overrideHeader: (String, String)): Future[Result] = {
    val newHeaders = validHeaders + overrideHeader
    val request = createGetRequestWithHeaders(s"/trusts/obliged-entities/$idType/$id", newHeaders)
    val result = SUT.getObligedEntity(id, idType).apply(request)
    result
  }

  private def getObligedEntitiesAsResponseWithoutHeader(id: String, idType: String, headerToDelete: String): Future[Result] = {
    val newHeaders = validHeaders - headerToDelete
    val request = createGetRequestWithHeaders(s"/trusts/obliged-entities/$idType/$id", newHeaders)
    val result = SUT.getObligedEntity(id, idType).apply(request)
    result
  }
}
