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

package utils

import play.api.libs.json.{JsValue, Json}

object HipResponse {

  def success(payload: JsValue): JsValue = Json.obj("success" -> payload)

  val jsonResponse400: JsValue = Json.parse(s"""
       |{
       |  "origin": "HIP",
       |  "response": {
       |    "failures": [
       |      {
       |        "type": "Type of Failure",
       |        "reason": "Reason for Failure"
       |      }
       |    ]
       |  }
       |}""".stripMargin)

  val json422ResponseInvalidIdTypeOrIdValue: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2001-12-17T09:30:47Z",
       |    "errorId": "000",
       |    "text": "UTR or URN is invalid"
       |  }
       |}""".stripMargin)

  val json422ResponseRequestNotProcessed: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2001-12-17T09:30:47Z",
       |    "errorId": "003",
       |    "text": "Request could not be processed"
       |  }
       |}""".stripMargin)

  val json422ResponseTechnicalError: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2001-12-17T09:30:47Z",
       |    "errorId": "999",
       |    "text": "Technical Error"
       |  }
       |}""".stripMargin)

  val jsonResponse500: JsValue = Json.parse(s"""
       |{
       |  "origin": "HoD",
       |  "response": {
       |    "error": {
       |      "code": "500",
       |      "logID": "00000000000000000000000000000000",
       |      "message": "String"
       |    }
       |  }
       |}""".stripMargin)

  val jsonResponse503: JsValue = Json.parse(
    """
      |{
      |  "origin": "HIP",
      |  "response": {
      |    "failures": [
      |      {
      |        "type": "string",
      |        "reason": "string"
      |      }
      |    ]
      |  }
      |}
      |""".stripMargin
  )

}
