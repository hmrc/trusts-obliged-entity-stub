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

import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait HeaderValidator {

  val CORRELATIONID_HEADER = "CorrelationId"
  val ENVIRONMENT_HEADER = "Environment"
  val TOKEN_HEADER = "Authorization"
  private val VALID_TOKEN_REGEX = "^(Bearer (.*))$".r
  private val VALID_ENV_REGEX = "^(dev)$".r
  private val VALID_CORRELATIONID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$".r


  def isEnvironmentValid(request: Request[_]): Boolean = {
    val environment = request.headers.get(ENVIRONMENT_HEADER).getOrElse("Invalid")
    VALID_ENV_REGEX.findFirstIn(environment).isDefined
  }

  def isTokenValid(request: Request[_]): Boolean = {
    val tokenValue = request.headers.get(TOKEN_HEADER).getOrElse("Invalid")
    VALID_TOKEN_REGEX.findFirstIn(tokenValue).isDefined
  }

  def isCorrelationIdValid(request: Request[_]): Boolean = {
    val correlationId = request.headers.get(CORRELATIONID_HEADER).getOrElse("Invalid")
    VALID_CORRELATIONID_REGEX.findFirstIn(correlationId).isDefined
  }

}

class HeaderValidatorAction @Inject()(parser: BodyParsers.Default)
                                     (implicit val ec: ExecutionContext) extends ActionBuilderImpl(parser) with HeaderValidator {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    lazy val (envValid, tokenValid, corrValid) = (isEnvironmentValid(request), isTokenValid(request), isCorrelationIdValid(request))

    (envValid, tokenValid, corrValid) match {
      case (false, _, _) => Future.successful(Results.Forbidden)
      case (_, false, _) => Future.successful(Results.Unauthorized)
      case (_, _, false) => Future.successful(Results.Forbidden)
      case _ => block(request)
    }
  }
}
