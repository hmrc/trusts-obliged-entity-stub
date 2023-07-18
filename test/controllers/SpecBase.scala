/*
 * Copyright 2023 HM Revenue & Customs
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

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.when
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.AuditModule
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.http.connector.AuditResult.Success
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.bootstrap.backend.filters.DefaultBackendAuditFilter

import scala.concurrent.{ExecutionContext, Future}

abstract class SpecBase extends AnyWordSpec with GuiceOneAppPerSuite {

  protected val ENVIRONMENT_HEADER = "Environment"
  protected val TOKEN_HEADER = "Authorization"
  protected val CORRELATIONID_HEADER = "CorrelationId"

  protected val validHeaders: Map[String, String] = Map(
    (ENVIRONMENT_HEADER, "dev"),
    (TOKEN_HEADER, "Bearer 11"),
    (CORRELATIONID_HEADER, "cd7a4033-ae84-4e18-861d-9d62c6741e87")
  )

  def createGetRequestWithValidHeaders(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", url).withHeaders(validHeaders.toSeq: _*)

  def createGetRequestWithHeaders(url: String, headers: Map[String, String]): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", url)
      .withHeaders(headers.toSeq: _*)

  val mockAuditConnector: AuditConnector = Mockito.mock(classOf[AuditConnector])

  when(mockAuditConnector.sendEvent(any[DataEvent])(any[HeaderCarrier], any[ExecutionContext]))
    .thenReturn(Future.successful(Success))

  override lazy val app: Application = GuiceApplicationBuilder()
    .disable(classOf[AuditModule], classOf[DefaultBackendAuditFilter])
    .configure(
      "metrics.enabled" -> false,
      "microservice.metrics.graphite.enabled" -> false
    )
    .overrides(bind[AuditConnector].to(mockAuditConnector))
    .build()

}
