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
        //sample
        case "2134514321" =>
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
