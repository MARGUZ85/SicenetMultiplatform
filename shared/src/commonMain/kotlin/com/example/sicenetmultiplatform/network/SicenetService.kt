package com.example.sicenetmultiplatform.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SicenetService {

    private val SERVICE_URL = "https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx"
    private var currentUrl = SERVICE_URL

    private val client = HttpClient {
        install(HttpCookies) {
            // Uses an in-memory cookie storage
        }
        install(HttpRedirect) {
            checkHttpMethod = false
            allowHttpsDowngrade = false
        }
        followRedirects = false
    }

    suspend fun login(user: String, pass: String): String? {
        val soapAction = "\"http://tempuri.org/accesoLogin\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <accesoLogin xmlns="http://tempuri.org/">
      <strMatricula>$user</strMatricula>
      <strContrasenia>$pass</strContrasenia>
      <tipoUsuario>ALUMNO</tipoUsuario>
    </accesoLogin>
  </soap:Body>
</soap:Envelope>"""
        currentUrl = SERVICE_URL
        return makeNetworkCall(soapBody, soapAction)
    }

    suspend fun getProfile(): String? {
        val soapAction = "\"http://tempuri.org/getAlumnoAcademicoWithLineamiento\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
  </soap:Body>
</soap:Envelope>"""
        return makeNetworkCall(soapBody, soapAction)
    }

    suspend fun getCargaAcademica(): String? {
        val soapAction = "\"http://tempuri.org/getCargaAcademicaByAlumno\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getCargaAcademicaByAlumno xmlns="http://tempuri.org/" />
  </soap:Body>
</soap:Envelope>"""
        return makeNetworkCall(soapBody, soapAction)
    }

    suspend fun getCardex(): String? {
        val soapAction = "\"http://tempuri.org/getAllKardexConPromedioByAlumno\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getAllKardexConPromedioByAlumno xmlns="http://tempuri.org/">
      <aluLineamiento>1</aluLineamiento>
    </getAllKardexConPromedioByAlumno>
  </soap:Body>
</soap:Envelope>"""
        return makeNetworkCall(soapBody, soapAction)
    }

    suspend fun getCalifUnidades(): String? {
        val soapAction = "\"http://tempuri.org/getCalifUnidadesByAlumno\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
  </soap:Body>
</soap:Envelope>"""
        return makeNetworkCall(soapBody, soapAction)
    }

    suspend fun getCalifFinales(): String? {
        val soapAction = "\"http://tempuri.org/getAllCalifFinalByAlumnos\""
        val soapBody = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getAllCalifFinalByAlumnos xmlns="http://tempuri.org/">
      <bytModEducativo>1</bytModEducativo>
    </getAllCalifFinalByAlumnos>
  </soap:Body>
</soap:Envelope>"""
        return makeNetworkCall(soapBody, soapAction)
    }

    private suspend fun makeNetworkCall(soapBody: String, soapAction: String): String? {
        return try {
            var response = client.post(currentUrl) {
                header("SOAPAction", soapAction)
                setBody(soapBody)
                contentType(ContentType.Text.Xml.withParameter("charset", "utf-8"))
            }

            if (response.status.value in 300..399) {
                val location = response.headers["Location"]
                if (location != null && location.contains("AspxAutoDetectCookieSupport=1")) {
                    val redirectUrl = if (location.startsWith("http")) {
                        location
                    } else {
                        URLBuilder(SERVICE_URL).takeFrom(location).buildString()
                    }
                    currentUrl = redirectUrl
                    
                    response = client.post(currentUrl) {
                        header("SOAPAction", soapAction)
                        setBody(soapBody)
                        contentType(ContentType.Text.Xml.withParameter("charset", "utf-8"))
                    }
                }
            }

            if (response.status.isSuccess()) {
                response.bodyAsText()
            } else {
                response.bodyAsText()
            }
        } catch (e: Exception) {
            println("SicenetService Exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
