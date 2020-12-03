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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request, Result}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.JsonUtils.jsonFromFile
import utils.DesResponse._

import scala.concurrent.Future

@Singleton()
class ObligedEntitiesController @Inject()(headerValidator: HeaderValidatorAction)(cc: ControllerComponents)
    extends BackendController(cc) with HeaderValidator {

  def getObligedEntity(utr: String): Action[AnyContent] = headerValidator.async { implicit request =>
    if(isUtrValid(utr)) {
      utr match {
        //trusts
        case "2134514321" | "1234567890" | "1234567891" | "1234567892" | "1234567893" | "1234567894" | "1234567895" | "1234567896" | "1234567897" | "1234567898" | "1234567899" | "1000000005" | "2134514321" | "5174384721" | "1274834715" =>
          jsonResult(utr)
        case "1000000006" | "1000000007" | "1000000008" | "1000000009" | "1000000010" | "1000000011" | "1000000012" | "1000000013" | "5000000000" =>
          jsonResult(utr)
        //trust utr for bad request & unauthorised tax enrolment
        case "0000000400" | "0000000401" =>
          jsonResult(utr)
        //trust utr to trigger Duplicate submission, Service unavailable & Server error response from trusts-variations-stub
        case "1000000409" | "1000000500" | "1000000503"=>
          jsonResult(utr)
        // In Processing, Closed and Pending Closure
        case "1111111111" | "1111111112" | "1111111113" =>
          jsonResult(utr)
        // Parked, Obsoleted and Suspended
        case "1111111114" | "1111111115" | "1111111116" =>
          jsonResult(utr)
        // Not enough data
        case "6666666666" =>
          jsonResult(utr)
        case "4000000000" | "4000000001" | "4000000002" | "4000000003" | "4000000004" | "4000000005" | "4000000006" | "4000000007"
             | "4000000008" | "4000000009" | "4000000010" =>
          jsonResult(utr)
        case "0000000500" =>
          Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" =>
          Future.successful(ServiceUnavailable(jsonResponse503))
        case _ =>
          Future.successful(NotFound(jsonResponseResourceNotFound))
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }

  private val utrRegex = "^[0-9]{10}$".r
  def isUtrValid(utr: String): Boolean = utrRegex.findFirstIn(utr).isDefined

  def jsonResult(filename: String)(implicit request: Request[AnyContent]): Future[Result] = {
    val path = s"/resources/$filename.json"
    Future.successful(Ok(jsonFromFile(path)).
      withHeaders(request.headers.get(CORRELATIONID_HEADER).
        map((CORRELATIONID_HEADER, _)).toSeq: _*))
  }
}
