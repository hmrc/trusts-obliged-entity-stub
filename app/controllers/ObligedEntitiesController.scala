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

  def getObligedEntity(id: String): Action[AnyContent] = headerValidator.async { implicit request =>
    if(isIdValid(id)) {
      id match {
        //sample
        case "2134514321" =>
          jsonResult(id)
        // 5mld taxable trusts
        case "1000000001" | "1000000002" | "1000000003" | "1000000007" | "1000000008" | "1000000101" | "1000000102" | "5174384721" | "1000000011" =>
          jsonResult(id)
        // 4MLD taxable trust registered, first time played back under 5MLD. User needs to answer additional questions
        case "1000000010" =>
          jsonResult(id)
        // 5mld non-taxable trusts with URN
        case "1234567890AAAAA" | "0000000001AAAAA" | "0000000002AAAAA" | "0000000003AAAAA" | "0000000004AAAAA" =>
          jsonResult(id)
        // 5mld non-taxable trusts with UTR
        case "1000000103" =>
          jsonResult(id)
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
  private val urnRegex = "^[0-9A-Z]{15}$".r
  def isUtrValid(utr: String): Boolean = utrRegex.findFirstIn(utr).isDefined

  def isUrnValid(urn: String): Boolean = urnRegex.findFirstIn(urn).isDefined

  def isIdValid(id: String): Boolean = isUtrValid(id) || isUrnValid(id)

  def jsonResult(filename: String)(implicit request: Request[AnyContent]): Future[Result] = {
    val path = s"/resources/$filename.json"
    Future.successful(Ok(jsonFromFile(path)).
      withHeaders(request.headers.get(CORRELATIONID_HEADER).
        map((CORRELATIONID_HEADER, _)).toSeq: _*))
  }
}
