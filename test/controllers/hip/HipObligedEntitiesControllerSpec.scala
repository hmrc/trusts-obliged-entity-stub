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

package controllers.hip

import controllers.SpecBase
import models.SuccessfulValidation
import org.scalatest.matchers.must.Matchers.*
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.JsValue
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import service.ValidationService
import utils.HipResponse.*

import scala.concurrent.Future

class HipObligedEntitiesControllerSpec extends SpecBase with Matchers {

  private val obligedEntitiesSchema =
    "/resources/schemas/hip/ObligedEntitiesSuccessResponse_EPID1755_TRS_openapi_v0.1.7.json"

  private val obligedEntitiesValidator = new ValidationService().get(obligedEntitiesSchema)

  private val SUT = app.injector.instanceOf[HipObligedEntitiesController]

  private val URN_TYPE = "URN"
  private val UTR_TYPE = "UTR"

  "getObligedEntity By Utr" should {
    "return OK with success-wrapped payload for 2134514321" in testObligedEntitiesUtr("2134514321")
    "return OK with success-wrapped payload for 1000000001" in testObligedEntitiesUtr("1000000001")
    "return OK with success-wrapped payload for 1000000002" in testObligedEntitiesUtr("1000000002")
    "return OK with success-wrapped payload for 1000000003" in testObligedEntitiesUtr("1000000003")
    "return OK with success-wrapped payload for 1000000005" in testObligedEntitiesUtr("1000000005")
    "return OK with success-wrapped payload for 1000000006" in testObligedEntitiesUtr("1000000006")
    "return OK with success-wrapped payload for 1000000007" in testObligedEntitiesUtr("1000000007")
    "return OK with success-wrapped payload for 1000000008" in testObligedEntitiesUtr("1000000008")
    "return OK with success-wrapped payload for 1000000009" in testObligedEntitiesUtr("1000000009")
    "return OK with success-wrapped payload for 1000000010" in testObligedEntitiesUtr("1000000010")
    "return OK with success-wrapped payload for 1000000011" in testObligedEntitiesUtr("1000000011")
    "return OK with success-wrapped payload for 1000000012" in testObligedEntitiesUtr("1000000012")
    "return OK with success-wrapped payload for 1000000013" in testObligedEntitiesUtr("1000000013")
    "return OK with success-wrapped payload for 1000000101" in testObligedEntitiesUtr("1000000101")
    "return OK with success-wrapped payload for 1000000102" in testObligedEntitiesUtr("1000000102")
    "return OK with success-wrapped payload for 1000000103" in testObligedEntitiesUtr("1000000103")
    "return OK with success-wrapped payload for 1234567890" in testObligedEntitiesUtr("1234567890")
    "return OK with success-wrapped payload for 1234567891" in testObligedEntitiesUtr("1234567891")
    "return OK with success-wrapped payload for 1234567892" in testObligedEntitiesUtr("1234567892")
    "return OK with success-wrapped payload for 1234567893" in testObligedEntitiesUtr("1234567893")
    "return OK with success-wrapped payload for 1234567894" in testObligedEntitiesUtr("1234567894")
    "return OK with success-wrapped payload for 1234567895" in testObligedEntitiesUtr("1234567895")
    "return OK with success-wrapped payload for 1234567896" in testObligedEntitiesUtr("1234567896")
    "return OK with success-wrapped payload for 1234567897" in testObligedEntitiesUtr("1234567897")
    "return OK with success-wrapped payload for 1234567898" in testObligedEntitiesUtr("1234567898")
    "return OK with success-wrapped payload for 1234567899" in testObligedEntitiesUtr("1234567899")
    "return OK with success-wrapped payload for 1274834715" in testObligedEntitiesUtr("1274834715")
    "return OK with success-wrapped payload for 3000000001" in testObligedEntitiesUtr("3000000001")
    "return OK with success-wrapped payload for 3000000002" in testObligedEntitiesUtr("3000000002")
    "return OK with success-wrapped payload for 3000000003" in testObligedEntitiesUtr("3000000003")
    "return OK with success-wrapped payload for 3000000004" in testObligedEntitiesUtr("3000000004")
    "return OK with success-wrapped payload for 3000000005" in testObligedEntitiesUtr("3000000005")
    "return OK with success-wrapped payload for 3000000006" in testObligedEntitiesUtr("3000000006")
    "return OK with success-wrapped payload for 3000000007" in testObligedEntitiesUtr("3000000007")
    "return OK with success-wrapped payload for 3000000008" in testObligedEntitiesUtr("3000000008")
    "return OK with success-wrapped payload for 3000000009" in testObligedEntitiesUtr("3000000009")
    "return OK with success-wrapped payload for 3000000010" in testObligedEntitiesUtr("3000000010")
    "return OK with success-wrapped payload for 3000000012" in testObligedEntitiesUtr("3000000012")
    "return OK with success-wrapped payload for 5174384721" in testObligedEntitiesUtr("5174384721")
    "return OK with success-wrapped payload for 5000000000" in testObligedEntitiesUtr("5000000000")
    "return OK with success-wrapped payload for 1000000017 without schema validation" in {
      val resultJson = getObligedEntitiesAsJson("1000000017", UTR_TYPE, OK)

      (resultJson \ "success" \ "identifiers" \ "utr").as[String] mustBe "1000000017"
    }
  }

  "getObligedEntity By Urn" should {
    "return OK with success-wrapped payload for 0000000001AAAAA" in
      testObligedEntitiesUrn("0000000001AAAAA")

    "return OK with success-wrapped payload for 0000000002AAAAA" in
      testObligedEntitiesUrn("0000000002AAAAA")

    "return OK with success-wrapped payload for 0000000003AAAAA" in
      testObligedEntitiesUrn("0000000003AAAAA")

    "return OK with success-wrapped payload for 0000000004AAAAA" in
      testObligedEntitiesUrn("0000000004AAAAA")

    "return OK with success-wrapped payload for 1234567890AAAAA" in
      testObligedEntitiesUrn("1234567890AAAAA")

    "return OK with success-wrapped payload for XATRUST80000001" in
      testObligedEntitiesUrn("XATRUST80000001")

    "return OK with success-wrapped payload for NTTRUST00000001" in
      testObligedEntitiesUrn("NTTRUST00000001")
  }

  "getObligedEntity Failure" should {

    "return 400 when headers are missing" in {
      val request = FakeRequest("GET", s"/etmp/RESTAdapter/trustsandestates/obliged-entities/$UTR_TYPE/0000000503")
      val result  = SUT.getObligedEntity("0000000503", UTR_TYPE).apply(request)

      status(result) must be(BAD_REQUEST)
    }

    "return 400 when X-Originating-System is invalid" in {
      val result = getObligedEntitiesAsResponse(("X-Originating-System", "XXX"))

      status(result) must be(BAD_REQUEST)
    }

    "return 400 when X-Receipt-Date is invalid" in {
      val result = getObligedEntitiesAsResponse(("X-Receipt-Date", "21/07/2026"))

      status(result) must be(BAD_REQUEST)
    }

    "return 400 when Authorization is not Basic" in {
      val result = getObligedEntitiesAsResponse(("Authorization", "Bearer 11"))

      status(result) must be(BAD_REQUEST)
    }

    "return Bad Request for invalid type" in {
      val resultJson = getObligedEntitiesAsJson(id = "0000000400AAAAA", idType = "XXXX", expectedResult = BAD_REQUEST)

      resultJson mustBe jsonResponse400
    }

    "return Bad Request for invalid id" in {
      val resultJson = getObligedEntitiesAsJson(id = "0000000400AAAAA", idType = UTR_TYPE, expectedResult = BAD_REQUEST)

      resultJson mustBe jsonResponse400
    }

    "return Bad Request for id 0000000400" in {
      val resultJson = getObligedEntitiesAsJson(id = "0000000400", idType = UTR_TYPE, expectedResult = BAD_REQUEST)

      resultJson mustBe jsonResponse400
    }

    "return 422 with the expected payload for each business validation scenario" in {
      val scenarios = List(
        ("0000422000", json422ResponseInvalidIdTypeOrIdValue),
        ("0000422003", json422ResponseRequestNotProcessed),
        ("0000422999", json422ResponseTechnicalError)
      )

      scenarios.foreach { case (id, expectedJson) =>
        getObligedEntitiesAsJson(id, UTR_TYPE, UNPROCESSABLE_ENTITY) mustBe expectedJson
      }
    }

    "return 503 with the expected payload" in {
      getObligedEntitiesAsJson("0000000503", UTR_TYPE, SERVICE_UNAVAILABLE) mustBe jsonResponse503
    }

    "return Internal Server Error when HIP has internal errors" in {
      val resultJson = getObligedEntitiesAsJson("0000000500", UTR_TYPE, INTERNAL_SERVER_ERROR)

      resultJson mustBe jsonResponse500
    }

    "return 400" in {
      val resultJson = getObligedEntitiesAsJson("0000000400", UTR_TYPE, BAD_REQUEST)

      resultJson mustBe jsonResponse400
    }
  }

  private def getObligedEntitiesAsJson(id: String, idType: String, expectedResult: Int): JsValue = {
    val request = createHipGetRequest(s"/etmp/RESTAdapter/trustsandestates/obliged-entities/$idType/$id")
    val result  = SUT.getObligedEntity(id, idType).apply(request)
    status(result)            must be(expectedResult)
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

    (resultJson \ "success" \ "identifiers" \ "utr").as[String] mustBe utr
  }

  private def testObligedEntitiesUrn(urn: String) = {
    val resultJson = getObligedEntitiesAsValidatedJson(urn, URN_TYPE)

    (resultJson \ "success" \ "identifiers" \ "urn").as[String] mustBe urn
  }

  private def getObligedEntitiesAsResponse(overrideHeader: (String, String)): Future[Result] = {
    val newHeaders = validHipHeaders + overrideHeader
    val request    = createHipGetRequestWithHeaders(
      s"/etmp/RESTAdapter/trustsandestates/obliged-entities/$UTR_TYPE/0000000503",
      newHeaders
    )
    SUT.getObligedEntity("0000000503", UTR_TYPE).apply(request)
  }

  private def createHipGetRequest(url: String): FakeRequest[AnyContentAsEmpty.type] =
    createHipGetRequestWithHeaders(url, validHipHeaders)

  private def createHipGetRequestWithHeaders(
    url: String,
    headers: Map[String, String]
  ): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", url).withHeaders(headers.toSeq*)

}
