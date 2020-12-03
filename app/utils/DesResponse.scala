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

package utils

import play.api.libs.json.{JsValue, Json}


object DesResponse {

  val jsonResponse400: JsValue = Json.parse(
    s"""
       |{
       | "code": "INVALID_PAYLOAD",
       | "reason": "Submission has not passed validation. Invalid Payload."
       |}""".stripMargin)

  val jsonResponse503: JsValue = Json.parse(
    s"""
       |{
       | "code": "SERVICE_UNAVAILABLE",
       | "reason": "Dependent systems are currently not responding"
       |}""".stripMargin)

  val jsonResponse500: JsValue = Json.parse(
    s"""
       |{
       | "code": "SERVER_ERROR",
       | "reason": "DES is currently experiencing problems that require live service intervention"
       |}""".stripMargin)


  val jsonResponseInvalidUtr: JsValue = Json.parse(
    s"""
       |{
       | "code": "INVALID_UTR",
       | "reason": "Submission has not passed validation. Invalid parameter UTR."
       |}""".stripMargin)

  val jsonResponseInvalidRegime: JsValue = Json.parse(
    s"""
       |{
       | "code": "INVALID_REGIME",
       | "reason": "The remote endpoint has indicated that the REGIME provided is invalid."
       |}""".stripMargin)


  val jsonResponseResourceNotFound: JsValue = Json.parse(
    s"""
       |{
       | "code": "RESOURCE_NOT_FOUND",
       | "reason": "The remote endpoint has indicated that no resource can be returned for the UTR provided."
       |}""".stripMargin)


}
