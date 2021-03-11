/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest


abstract class SpecBase extends PlaySpec with GuiceOneServerPerSuite {

  val ENVIRONMENT_HEADER = "Environment"
  val TOKEN_HEADER = "Authorization"
  val CORRELATIONID_HEADER = "CorrelationId"

  val CONTENT_TYPE_HEADER = ("Content-type", "application/json")


  def createGetRequestWithValidHeaders(url : String ) :FakeRequest[AnyContentAsEmpty.type ] = {
    FakeRequest("GET", url)
      .withHeaders((ENVIRONMENT_HEADER, "dev"),
        (TOKEN_HEADER, "Bearer 11"),
        (CORRELATIONID_HEADER, "cd7a4033-ae84-4e18-861d-9d62c6741e87") )
  }

}
